CREATE TABLE players
(
    uuid char(36) not null,
    faction varchar(?) not null, -- Registry max size
    constraint players_pk
        primary key (uuid)
);
