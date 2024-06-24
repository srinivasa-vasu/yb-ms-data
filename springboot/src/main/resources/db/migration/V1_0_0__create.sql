CREATE EXTENSION IF NOT EXISTS "uuid-ossp" schema public;
CREATE SCHEMA IF NOT EXISTS stodo;
CREATE TABLE IF NOT EXISTS stodo.todo
(
    id           uuid PRIMARY KEY,
    task         VARCHAR(255),
    status       boolean,
    created_at  timestamptz default now()
);

CREATE TABLE IF NOT EXISTS stodo.schema_history_${alt_region} (
    installed_rank int4 NOT NULL primary key,
    "version" varchar(50) NULL,
    description varchar(200) NOT NULL,
    "type" varchar(20) NOT NULL,
    script varchar(1000) NOT NULL,
    checksum int4 NULL,
    installed_by varchar(100) NOT NULL,
    installed_on timestamp DEFAULT now() NOT NULL,
    execution_time int4 NOT NULL,
    success bool NOT NULL
);

create index on stodo.schema_history_${alt_region}(success);
