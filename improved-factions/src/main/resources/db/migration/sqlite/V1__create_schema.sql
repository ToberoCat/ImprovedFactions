-- Initial ImprovedFactions schema for SQLite.
-- All statements are idempotent so existing installations can be baselined safely.

CREATE TABLE IF NOT EXISTS clusters (
    id BINARY(16) NOT NULL PRIMARY KEY,
    centerX DOUBLE NOT NULL DEFAULT 0.0,
    centerY DOUBLE NOT NULL DEFAULT 0.0,
    world VARCHAR(255) NOT NULL,
    type INTEGER NOT NULL,
    type_reference_id INTEGER NOT NULL,
    center_lazy_update BOOLEAN NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS factions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(36) NOT NULL,
    owner BINARY(16) NOT NULL,
    accumulated_power INTEGER NOT NULL DEFAULT 0,
    max_power INTEGER NOT NULL DEFAULT 50,
    default_rank INTEGER NOT NULL DEFAULT 0,
    icon_base64 VARCHAR(5000),
    join_type INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS faction_users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    uniqueId BINARY(16) NOT NULL,
    faction_id INTEGER NOT NULL,
    rank_id INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (faction_id) REFERENCES factions(id)
);

CREATE TABLE IF NOT EXISTS faction_ranks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    faction_id INTEGER NOT NULL,
    rank_name VARCHAR(50) NOT NULL,
    priority INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS faction_permissions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    rank_id INTEGER NOT NULL,
    permission VARCHAR(255) NOT NULL,
    allowed BOOLEAN NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS faction_bans (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    faction INTEGER NOT NULL,
    user INTEGER NOT NULL,
    FOREIGN KEY (faction) REFERENCES factions(id),
    FOREIGN KEY (user) REFERENCES faction_users(id)
);

CREATE TABLE IF NOT EXISTS player_usage_limits (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    registry VARCHAR(30) NOT NULL,
    player_id BINARY(16) NOT NULL,
    used INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS faction_clusters (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    faction INTEGER NOT NULL,
    parent_cluster BINARY(16) NOT NULL,
    FOREIGN KEY (faction) REFERENCES factions(id)
);

CREATE TABLE IF NOT EXISTS zone_clusters (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    zone_type VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS faction_claims (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    chunk_x INTEGER NOT NULL,
    chunk_z INTEGER NOT NULL,
    faction_id INTEGER NOT NULL,
    world VARCHAR(50) NOT NULL DEFAULT 'world',
    zone_type VARCHAR(25) NOT NULL DEFAULT 'faction',
    cluster_id BINARY(16),
    FOREIGN KEY (cluster_id) REFERENCES clusters(id)
);

CREATE TABLE IF NOT EXISTS faction_invites (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    inviter_id INTEGER NOT NULL,
    invited_id INTEGER NOT NULL,
    faction_id INTEGER NOT NULL,
    rank_id INTEGER NOT NULL DEFAULT 0,
    expiration_date TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS known_offline_players (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(16) NOT NULL
);

CREATE TABLE IF NOT EXISTS faction_homes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    z DOUBLE NOT NULL,
    world VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS faction_relations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    source_faction_id INTEGER NOT NULL,
    target_faction_id INTEGER NOT NULL,
    relation_type INTEGER NOT NULL,
    FOREIGN KEY (source_faction_id) REFERENCES factions(id),
    FOREIGN KEY (target_faction_id) REFERENCES factions(id)
);

CREATE TABLE IF NOT EXISTS faction_ally_invites (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    source_faction_id INTEGER NOT NULL,
    target_faction_id INTEGER NOT NULL,
    expiration_date TEXT NOT NULL,
    FOREIGN KEY (source_faction_id) REFERENCES factions(id),
    FOREIGN KEY (target_faction_id) REFERENCES factions(id)
);
