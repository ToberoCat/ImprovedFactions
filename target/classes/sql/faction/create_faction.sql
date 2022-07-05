CREATE TABLE IF NOT EXISTS factions
(
    registry_id    varchar(?)                    not null, -- Registry max size
    display_name   varchar(?)                    not null, -- Display max size
    motd           text    default 'New faction' not null,
    tag            varchar(?)                    not null, -- Tag max size
    description    int                           not null,
    frozen         boolean default false         not null,
    permanent      boolean default false         not null,
    created_at     datetime                      not null,
    owner          char(36)                      not null,
    claimed_chunks int     default 0             not null,
    balance        bigint  default 0             not null,
    current_power  int                           not null,
    max_power      int                           not null,
    members        int                           not null,
    banned         int                           not null,
    allies         int                           not null,
    enemies        int                           not null,
    settings       int                           not null,
    ranks          int                           not null,
    member_ranks   int                           not null,
    constraint factions_pk
        primary key (registry_id)
);