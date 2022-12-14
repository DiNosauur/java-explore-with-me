CREATE TABLE IF NOT EXISTS stats
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app       varchar(100)                            NOT NULL,
    uri       varchar(100)                            NOT NULL,
    ip        varchar(100)                            NOT NULL,
    timestamp TIMESTAMP                               NOT NULL,
    CONSTRAINT pk_stats PRIMARY KEY (id)
);