create schema "project_management";

CREATE TABLE "project_management".users(
   id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
   keycloak_id varchar(50) unique,
   first_name varchar(20),
   last_name varchar(20),
   email varchar(255),
   created_at timestamp,
   updated_at timestamp
);

CREATE TABLE "project_management".organizations(
    id uuid primary key default gen_random_uuid(),
    name varchar(30),
    slug varchar(255) unique ,
    created_at timestamp,
    updated_at timestamp
);

CREATE TABLE "project_management".teams(
    id uuid primary key default  gen_random_uuid(),
    org_id uuid references "project_management".organizations(id),
    name varchar(30),
    created_at timestamp,
    updated_at timestamp
);

CREATE TYPE team_member_role AS ENUM ('OWNER', 'MANAGER', 'MEMBER');

CREATE TABLE "project_management".team_members(
  id uuid primary key default  gen_random_uuid(),
  team_id uuid references "project_management".teams(id),
  user_id uuid references "project_management".users(id),
  role team_member_role NOT NULL DEFAULT 'MEMBER',
  UNIQUE (team_id, user_id)
)