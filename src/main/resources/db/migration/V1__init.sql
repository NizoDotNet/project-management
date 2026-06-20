CREATE SCHEMA IF NOT EXISTS "project_management";


CREATE TABLE "project_management".users (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    keycloak_id VARCHAR(50) UNIQUE,
    first_name  VARCHAR(50),
    last_name   VARCHAR(50),
    email       VARCHAR(255) NOT NULL UNIQUE,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now()
);


CREATE TABLE "project_management".organizations (
            id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            name       VARCHAR(100) NOT NULL,
            slug       VARCHAR(255) NOT NULL UNIQUE,
            created_at TIMESTAMP NOT NULL DEFAULT now(),
            updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".teams (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id     UUID NOT NULL REFERENCES "project_management".organizations(id) ON DELETE CASCADE,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".team_members (
           id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
           team_id    UUID NOT NULL REFERENCES "project_management".teams(id) ON DELETE CASCADE,
           user_id    UUID NOT NULL REFERENCES "project_management".users(id) ON DELETE CASCADE,
           role       VARCHAR(20) NOT NULL DEFAULT 'MEMBER',
           created_at TIMESTAMP NOT NULL DEFAULT now(),
           updated_at TIMESTAMP NOT NULL DEFAULT now(),
           UNIQUE (team_id, user_id)
);


CREATE TABLE "project_management".projects (
       id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       org_id      UUID NOT NULL REFERENCES "project_management".organizations(id) ON DELETE CASCADE,
       owner_id    UUID NOT NULL REFERENCES "project_management".users(id),
       name        VARCHAR(150) NOT NULL,
       description TEXT,
       status      VARCHAR(20) NOT NULL DEFAULT 'PLANNING',
       start_date  DATE,
       end_date    DATE,
       created_at  TIMESTAMP NOT NULL DEFAULT now(),
       updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".project_members (
              id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
              project_id UUID NOT NULL REFERENCES "project_management".projects(id) ON DELETE CASCADE,
              user_id    UUID NOT NULL REFERENCES "project_management".users(id) ON DELETE CASCADE,
              role       VARCHAR(20) NOT NULL DEFAULT 'DEVELOPER',
              created_at TIMESTAMP NOT NULL DEFAULT now(),
              updated_at TIMESTAMP NOT NULL DEFAULT now(),
              UNIQUE (project_id, user_id)
);


CREATE TABLE "project_management".boards (
     id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     project_id UUID NOT NULL REFERENCES "project_management".projects(id) ON DELETE CASCADE,
     name       VARCHAR(100) NOT NULL,
     type       VARCHAR(20) NOT NULL DEFAULT 'KANBAN',
     created_at TIMESTAMP NOT NULL DEFAULT now(),
     updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".board_columns (
            id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            board_id   UUID NOT NULL REFERENCES "project_management".boards(id) ON DELETE CASCADE,
            name       VARCHAR(100) NOT NULL,
            position   INT NOT NULL,
            created_at TIMESTAMP NOT NULL DEFAULT now(),
            updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".sprints (
      id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      board_id   UUID NOT NULL REFERENCES "project_management".boards(id) ON DELETE CASCADE,
      name       VARCHAR(100) NOT NULL,
      status     VARCHAR(20) NOT NULL DEFAULT 'PLANNED',
      start_date DATE,
      end_date   DATE,
      created_at TIMESTAMP NOT NULL DEFAULT now(),
      updated_at TIMESTAMP NOT NULL DEFAULT now()
);


CREATE TABLE "project_management".milestones (
         id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
         project_id  UUID NOT NULL REFERENCES "project_management".projects(id) ON DELETE CASCADE,
         name        VARCHAR(150) NOT NULL,
         description TEXT,
         due_date    DATE,
         status      VARCHAR(20) NOT NULL DEFAULT 'OPEN',
         created_at  TIMESTAMP NOT NULL DEFAULT now(),
         updated_at  TIMESTAMP NOT NULL DEFAULT now()
);


CREATE TABLE "project_management".tickets (
      id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      board_id     UUID NOT NULL REFERENCES "project_management".boards(id) ON DELETE CASCADE,
      column_id    UUID REFERENCES "project_management".board_columns(id) ON DELETE SET NULL,
      sprint_id    UUID REFERENCES "project_management".sprints(id) ON DELETE SET NULL,
      reporter_id  UUID NOT NULL REFERENCES "project_management".users(id),
      milestone_id UUID REFERENCES "project_management".milestones(id) ON DELETE SET NULL,
      title        VARCHAR(255) NOT NULL,
      description  TEXT,
      type         VARCHAR(20) NOT NULL DEFAULT 'TASK',
      priority     VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
      status       VARCHAR(20) NOT NULL DEFAULT 'BACKLOG',
      due_date     DATE,
      created_at   TIMESTAMP NOT NULL DEFAULT now(),
      updated_at   TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".ticket_assignees (
               id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
               ticket_id  UUID NOT NULL REFERENCES "project_management".tickets(id) ON DELETE CASCADE,
               user_id    UUID NOT NULL REFERENCES "project_management".users(id) ON DELETE CASCADE,
               created_at TIMESTAMP NOT NULL DEFAULT now(),
               updated_at TIMESTAMP NOT NULL DEFAULT now(),
               UNIQUE (ticket_id, user_id)
);

CREATE TABLE "project_management".ticket_comments (
              id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
              ticket_id  UUID NOT NULL REFERENCES "project_management".tickets(id) ON DELETE CASCADE,
              author_id  UUID NOT NULL REFERENCES "project_management".users(id),
              parent_id  UUID REFERENCES "project_management".ticket_comments(id) ON DELETE CASCADE,
              content    TEXT NOT NULL,
              created_at TIMESTAMP NOT NULL DEFAULT now(),
              updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".ticket_attachments (
                 id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                 ticket_id   UUID NOT NULL REFERENCES "project_management".tickets(id) ON DELETE CASCADE,
                 uploaded_by UUID NOT NULL REFERENCES "project_management".users(id),
                 file_name   VARCHAR(255) NOT NULL,
                 file_url    VARCHAR(500) NOT NULL,
                 file_size   BIGINT NOT NULL,
                 created_at  TIMESTAMP NOT NULL DEFAULT now(),
                 updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".ticket_histories (
               id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
               ticket_id  UUID NOT NULL REFERENCES "project_management".tickets(id) ON DELETE CASCADE,
               changed_by UUID NOT NULL REFERENCES "project_management".users(id),
               field_name VARCHAR(50) NOT NULL,
               old_value  TEXT,
               new_value  TEXT,
               created_at TIMESTAMP NOT NULL DEFAULT now(),
               updated_at TIMESTAMP NOT NULL DEFAULT now()
);


CREATE TABLE "project_management".labels (
     id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     project_id UUID NOT NULL REFERENCES "project_management".projects(id) ON DELETE CASCADE,
     name       VARCHAR(50) NOT NULL,
     color      VARCHAR(20) NOT NULL,
     created_at TIMESTAMP NOT NULL DEFAULT now(),
     updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".ticket_labels (
            id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            ticket_id  UUID NOT NULL REFERENCES "project_management".tickets(id) ON DELETE CASCADE,
            label_id   UUID NOT NULL REFERENCES "project_management".labels(id) ON DELETE CASCADE,
            created_at TIMESTAMP NOT NULL DEFAULT now(),
            updated_at TIMESTAMP NOT NULL DEFAULT now(),
            UNIQUE (ticket_id, label_id)
);


CREATE TABLE "project_management".notifications (
            id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            user_id    UUID NOT NULL REFERENCES "project_management".users(id) ON DELETE CASCADE,
            ticket_id  UUID REFERENCES "project_management".tickets(id) ON DELETE SET NULL,
            type       VARCHAR(30) NOT NULL,
            message    TEXT NOT NULL,
            is_read    BOOLEAN NOT NULL DEFAULT FALSE,
            created_at TIMESTAMP NOT NULL DEFAULT now(),
            updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".notification_settings (
                    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                    user_id          UUID NOT NULL UNIQUE REFERENCES "project_management".users(id) ON DELETE CASCADE,
                    on_assign        BOOLEAN NOT NULL DEFAULT TRUE,
                    on_mention       BOOLEAN NOT NULL DEFAULT TRUE,
                    on_status_change BOOLEAN NOT NULL DEFAULT TRUE,
                    on_comment       BOOLEAN NOT NULL DEFAULT TRUE,
                    created_at       TIMESTAMP NOT NULL DEFAULT now(),
                    updated_at       TIMESTAMP NOT NULL DEFAULT now()
);


CREATE INDEX idx_teams_org_id                      ON "project_management".teams(org_id);
CREATE INDEX idx_team_members_team_id               ON "project_management".team_members(team_id);
CREATE INDEX idx_team_members_user_id               ON "project_management".team_members(user_id);

CREATE INDEX idx_projects_org_id                    ON "project_management".projects(org_id);
CREATE INDEX idx_projects_owner_id                  ON "project_management".projects(owner_id);

CREATE INDEX idx_project_members_project_id         ON "project_management".project_members(project_id);
CREATE INDEX idx_project_members_user_id            ON "project_management".project_members(user_id);

CREATE INDEX idx_boards_project_id                  ON "project_management".boards(project_id);
CREATE INDEX idx_board_columns_board_id             ON "project_management".board_columns(board_id);
CREATE INDEX idx_sprints_board_id                   ON "project_management".sprints(board_id);
CREATE INDEX idx_milestones_project_id              ON "project_management".milestones(project_id);

CREATE INDEX idx_tickets_board_id                   ON "project_management".tickets(board_id);
CREATE INDEX idx_tickets_column_id                  ON "project_management".tickets(column_id);
CREATE INDEX idx_tickets_sprint_id                  ON "project_management".tickets(sprint_id);
CREATE INDEX idx_tickets_reporter_id                ON "project_management".tickets(reporter_id);
CREATE INDEX idx_tickets_milestone_id               ON "project_management".tickets(milestone_id);

CREATE INDEX idx_ticket_assignees_ticket_id         ON "project_management".ticket_assignees(ticket_id);
CREATE INDEX idx_ticket_assignees_user_id           ON "project_management".ticket_assignees(user_id);

CREATE INDEX idx_ticket_comments_ticket_id          ON "project_management".ticket_comments(ticket_id);
CREATE INDEX idx_ticket_comments_author_id          ON "project_management".ticket_comments(author_id);
CREATE INDEX idx_ticket_comments_parent_id          ON "project_management".ticket_comments(parent_id);

CREATE INDEX idx_ticket_attachments_ticket_id       ON "project_management".ticket_attachments(ticket_id);
CREATE INDEX idx_ticket_attachments_uploaded_by     ON "project_management".ticket_attachments(uploaded_by);

CREATE INDEX idx_ticket_histories_ticket_id         ON "project_management".ticket_histories(ticket_id);
CREATE INDEX idx_ticket_histories_changed_by        ON "project_management".ticket_histories(changed_by);

CREATE INDEX idx_labels_project_id                  ON "project_management".labels(project_id);
CREATE INDEX idx_ticket_labels_ticket_id            ON "project_management".ticket_labels(ticket_id);
CREATE INDEX idx_ticket_labels_label_id             ON "project_management".ticket_labels(label_id);

CREATE INDEX idx_notifications_user_id              ON "project_management".notifications(user_id);
CREATE INDEX idx_notifications_ticket_id            ON "project_management".notifications(ticket_id);