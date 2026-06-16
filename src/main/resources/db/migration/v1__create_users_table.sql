CREATE TABLE users(
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  first_name varchar(20),
  last_name varchar(20),
  email varchar(255),
  created_at timestamp,
  updated_at timestamp
);