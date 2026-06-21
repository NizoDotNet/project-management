ALTER TABLE "project_management".organizations
    ADD COLUMN owner_id UUID NOT NULL REFERENCES project_management.users(id);