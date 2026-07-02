BEGIN;

create sequence board_columns_position_seq
    owned by project_management.board_columns.position;

alter table project_management.board_columns
    alter column position set default nextval('board_columns_position_seq');


COMMIT;