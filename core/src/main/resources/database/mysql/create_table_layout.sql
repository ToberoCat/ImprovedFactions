-- Test variables
-- SET @max_len := 10;
-- SET @max_tag := 3;

CREATE SCHEMA IF NOT EXISTS improved_factions;
USE improved_factions;

-- Create faction table
CREATE TABLE IF NOT EXISTS factions
(
    registry_id    varchar(@max_len)             not null, -- Registry max size
    display_name   varchar(@max_len)             not null, -- Display max size
    motd           text    default 'New faction' not null,
    tag            varchar(@max_tag)             not null, -- Tag max size
    open_type      int(3)                        not null,
    frozen         boolean default false         not null,
    permanent      boolean default false         not null,
    created_at     datetime                      not null,
    owner          char(36)                      not null,
    claimed_chunks int     default 0             not null,
    balance        bigint  default 0             not null,
    current_power  int                           not null,
    max_power      int                           not null,
    constraint factions_pk
        primary key (registry_id)
);

-- Create faction description table
create table faction_descriptions
(
    registry_id VARCHAR(@max_len) not null,
    line        int               not null,
    content     TEXT              not null,
    PRIMARY KEY (registry_id, line)
);

-- Create faction bans
create table IF NOT EXISTS faction_bans
(
    registry_id VARCHAR(@max_len) not null,
    banned      CHAR(36)          not null,
    constraint faction_bans_pk
        primary key (banned)
);

-- Faction relation table
create table IF NOT EXISTS faction_relations
(
    registry_id          VARCHAR(@max_len) not null,
    relation_registry_id VARCHAR(@max_len) not null,
    relation_status      INT(3) default 1  not null, -- Only options: 0 - ally, 1 neutral, 2 enemy
    constraint faction_relations_pk
        primary key (relation_registry_id)
);

-- Create settings table
create table IF NOT EXISTS faction_settings
(
    registry_id VARCHAR(@max_len) not null,
    setting     TEXT              not null,
    value       TEXT              null,
    constraint faction_settings_pk
        primary key (registry_id)
);

-- Create players
CREATE TABLE IF NOT EXISTS players
(
    uuid        CHAR(36)          NOT NULL,
    faction     VARCHAR(@max_len) NOT NULL,
    member_rank TEXT              NOT NULL,
    power       DOUBLE            NOT NULL,
    maxPower    DOUBLE            NOT NULL,
    timeout     DATETIME,
    CONSTRAINT players_pk
        PRIMARY KEY (uuid)
);

-- Create player settings
create table IF NOT EXISTS player_settings
(
    uuid    CHAR(36) not null,
    setting TEXT     not null,
    value   TEXT     not null,
    constraint player_settings_pk
        primary key (uuid)
);

-- Create messages
create table IF NOT EXISTS messages
(
    player  char(36)     not null,
    content varchar(255) not null,
    constraint messages_pk
        PRIMARY KEY (player, content)
);

-- Create report table
CREATE TABLE IF NOT EXISTS reports
(
    registry varchar(@max_len) not null,
    reporter char(36)          not null,
    reason   text              not null,
    constraint reports_pk
        primary key (registry, reporter)
);

-- Create invites
create table IF NOT EXISTS ally_invites
(
    sender    varchar(@max_len) not null,
    receiver  varchar(@max_len) not null,
    send_date datetime          not null,
    constraint invites_pk
        primary key (receiver)
);

-- Create data
create table persistent_data
(
    uuid  varchar(36) not null,
    id    char(128)   not null,
    value text        not null,
    constraint persistent_data_pk
        primary key (uuid, id)
);

-- Create claims
create table claims
(
    world    char(128) not null,
    registry char(@max_len)  not null,
    x        int       not null,
    z        int       not null,
    constraint claims_pk
        primary key (world, x, z)
);

-- Create faction permissions
create table faction_permissions
(
    registry char(10) not null,
    `rank` char(64) not null,
    permission char(128) not null,
    constraint faction_permissions_pk
        primary key (`rank`, permission)
);

