CREATE SCHEMA IF NOT EXISTS "project_management";

CREATE TYPE "project_management".team_member_role
AS ENUM ('OWNER', 'MANAGER', 'MEMBER');

CREATE TABLE "project_management".users(
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           keycloak_id VARCHAR(50) UNIQUE,
                                           first_name VARCHAR(20),
                                           last_name VARCHAR(20),
                                           email VARCHAR(255),
                                           created_at TIMESTAMP NOT NULL DEFAULT now(),
                                           updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".organizations(
                                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                   name VARCHAR(30),
                                                   slug VARCHAR(255) UNIQUE,
                                                   created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                   updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".teams(
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           org_id UUID REFERENCES "project_management".organizations(id),
                                           name VARCHAR(30),
                                           created_at TIMESTAMP NOT NULL DEFAULT now(),
                                           updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".team_members(
                                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                  team_id UUID REFERENCES "project_management".teams(id),
                                                  user_id UUID REFERENCES "project_management".users(id),
                                                  role "project_management".team_member_role NOT NULL DEFAULT 'MEMBER',
                                                  created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                  updated_at TIMESTAMP NOT NULL DEFAULT now(),
                                                  UNIQUE (team_id, user_id)
);

CREATE TABLE "project_management".project(
                                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                             org_id UUID NOT NULL REFERENCES "project_management".organizations(id),
                                             owner_id UUID NOT NULL REFERENCES "project_management".users(id),
                                             name VARCHAR NOT NULL,
                                             description TEXT,
                                             status VARCHAR NOT NULL,
                                             start_date DATE,
                                             end_date DATE,
                                             created_at TIMESTAMP NOT NULL DEFAULT now(),
                                             updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".project_member(
                                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                    project_id UUID NOT NULL
                                                        REFERENCES "project_management".project(id)
                                                            ON DELETE CASCADE,
                                                    user_id UUID NOT NULL
                                                        REFERENCES "project_management".users(id),
                                                    role VARCHAR NOT NULL,
                                                    created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                    updated_at TIMESTAMP NOT NULL DEFAULT now(),
                                                    UNIQUE (project_id, user_id)
);

CREATE TABLE "project_management".board(
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           project_id UUID NOT NULL
                                               REFERENCES "project_management".project(id)
                                                   ON DELETE CASCADE,
                                           name VARCHAR NOT NULL,
                                           type VARCHAR NOT NULL,
                                           created_at TIMESTAMP NOT NULL DEFAULT now(),
                                           updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".board_column(
                                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                  board_id UUID NOT NULL
                                                      REFERENCES "project_management".board(id)
                                                          ON DELETE CASCADE,
                                                  name VARCHAR NOT NULL,
                                                  position INT NOT NULL,
                                                  created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                  updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".sprint(
                                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                            board_id UUID NOT NULL
                                                REFERENCES "project_management".board(id)
                                                    ON DELETE CASCADE,
                                            name VARCHAR NOT NULL,
                                            status VARCHAR NOT NULL,
                                            start_date DATE,
                                            end_date DATE,
                                            created_at TIMESTAMP NOT NULL DEFAULT now(),
                                            updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".milestone(
                                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                               project_id UUID NOT NULL
                                                   REFERENCES "project_management".project(id)
                                                       ON DELETE CASCADE,
                                               name VARCHAR NOT NULL,
                                               description TEXT,
                                               due_date DATE,
                                               status VARCHAR NOT NULL,
                                               created_at TIMESTAMP NOT NULL DEFAULT now(),
                                               updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".ticket(
                                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                            board_id UUID NOT NULL
                                                REFERENCES "project_management".board(id)
                                                    ON DELETE CASCADE,
                                            column_id UUID
                                                          REFERENCES "project_management".board_column(id)
                                                              ON DELETE SET NULL,
                                            sprint_id UUID
                                                          REFERENCES "project_management".sprint(id)
                                                              ON DELETE SET NULL,
                                            reporter_id UUID NOT NULL
                                                REFERENCES "project_management".users(id),
                                            milestone_id UUID
                                                REFERENCES "project_management".milestone(id)
                                                              ON DELETE SET NULL,
                                            title VARCHAR NOT NULL,
                                            description TEXT,
                                            type VARCHAR NOT NULL,
                                            priority VARCHAR NOT NULL,
                                            status VARCHAR NOT NULL,
                                            due_date DATE,
                                            created_at TIMESTAMP NOT NULL DEFAULT now(),
                                            updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".ticket_assignee(
                                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                     ticket_id UUID NOT NULL
                                                         REFERENCES "project_management".ticket(id)
                                                             ON DELETE CASCADE,
                                                     user_id UUID NOT NULL
                                                         REFERENCES "project_management".users(id),
                                                     created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                     updated_at TIMESTAMP NOT NULL DEFAULT now(),
                                                     UNIQUE (ticket_id, user_id)
);

CREATE TABLE "project_management".ticket_comment(
                                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                    ticket_id UUID NOT NULL
                                                        REFERENCES "project_management".ticket(id)
                                                            ON DELETE CASCADE,
                                                    author_id UUID NOT NULL
                                                        REFERENCES "project_management".users(id),
                                                    parent_id UUID
                                                        REFERENCES "project_management".ticket_comment(id)
                                                            ON DELETE CASCADE,
                                                    content TEXT NOT NULL,
                                                    created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".ticket_attachment(
                                                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                       ticket_id UUID NOT NULL
                                                           REFERENCES "project_management".ticket(id)
                                                               ON DELETE CASCADE,
                                                       uploaded_by UUID NOT NULL
                                                           REFERENCES "project_management".users(id),
                                                       file_name VARCHAR NOT NULL,
                                                       file_url VARCHAR NOT NULL,
                                                       file_size BIGINT NOT NULL,
                                                       created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                       updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".ticket_history(
                                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                    ticket_id UUID NOT NULL
                                                        REFERENCES "project_management".ticket(id)
                                                            ON DELETE CASCADE,
                                                    changed_by UUID NOT NULL
                                                        REFERENCES "project_management".users(id),
                                                    field_name VARCHAR NOT NULL,
                                                    old_value TEXT,
                                                    new_value TEXT,
                                                    created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".label(
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           project_id UUID NOT NULL
                                               REFERENCES "project_management".project(id)
                                                   ON DELETE CASCADE,
                                           name VARCHAR NOT NULL,
                                           color VARCHAR NOT NULL,
                                           created_at TIMESTAMP NOT NULL DEFAULT now(),
                                           updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".ticket_label(
                                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                  ticket_id UUID NOT NULL
                                                      REFERENCES "project_management".ticket(id)
                                                          ON DELETE CASCADE,
                                                  label_id UUID NOT NULL
                                                      REFERENCES "project_management".label(id)
                                                          ON DELETE CASCADE,
                                                  created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                  updated_at TIMESTAMP NOT NULL DEFAULT now(),
                                                  UNIQUE (ticket_id, label_id)
);

CREATE TABLE "project_management".notification(
                                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                  user_id UUID NOT NULL
                                                      REFERENCES "project_management".users(id),
                                                  ticket_id UUID
                                                      REFERENCES "project_management".ticket(id)
                                                          ON DELETE CASCADE,
                                                  type VARCHAR NOT NULL,
                                                  message TEXT NOT NULL,
                                                  is_read BOOLEAN NOT NULL DEFAULT FALSE,
                                                  created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                  updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE "project_management".notification_setting(
                                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                          user_id UUID NOT NULL UNIQUE
                                                              REFERENCES "project_management".users(id),
                                                          on_assign BOOLEAN NOT NULL DEFAULT TRUE,
                                                          on_mention BOOLEAN NOT NULL DEFAULT TRUE,
                                                          on_status_change BOOLEAN NOT NULL DEFAULT TRUE,
                                                          on_comment BOOLEAN NOT NULL DEFAULT TRUE,
                                                          created_at TIMESTAMP NOT NULL DEFAULT now(),
                                                          updated_at TIMESTAMP NOT NULL DEFAULT now()
);
