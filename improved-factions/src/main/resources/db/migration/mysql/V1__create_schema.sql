-- Initial ImprovedFactions schema for MySQL/MariaDB.
-- CREATE IF NOT EXISTS keeps the first migration safe for existing installations.

CREATE TABLE IF NOT EXISTS clusters (
    id BINARY(16) NOT NULL PRIMARY KEY,
    centerX DOUBLE NOT NULL DEFAULT 0.0,
    centerY DOUBLE NOT NULL DEFAULT 0.0,
    world VARCHAR(255) NOT NULL,
    type INT NOT NULL,
    type_reference_id INT NOT NULL,
    center_lazy_update BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS factions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(36) NOT NULL,
    owner BINARY(16) NOT NULL,
    accumulated_power INT NOT NULL DEFAULT 0,
    max_power INT NOT NULL DEFAULT 50,
    default_rank INT NOT NULL DEFAULT 0,
    icon_base64 VARCHAR(5000),
    join_type INT NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS faction_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uniqueId BINARY(16) NOT NULL,
    faction_id INT NOT NULL,
    rank_id INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_faction_users_faction FOREIGN KEY (faction_id) REFERENCES factions(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS faction_ranks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    faction_id INT NOT NULL,
    rank_name VARCHAR(50) NOT NULL,
    priority INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS faction_permissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rank_id INT NOT NULL,
    permission VARCHAR(255) NOT NULL,
    allowed BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS faction_bans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    faction INT NOT NULL,
    user INT NOT NULL,
    CONSTRAINT fk_faction_bans_faction FOREIGN KEY (faction) REFERENCES factions(id),
    CONSTRAINT fk_faction_bans_user FOREIGN KEY (user) REFERENCES faction_users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS player_usage_limits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    registry VARCHAR(30) NOT NULL,
    player_id BINARY(16) NOT NULL,
    used INT NOT NULL DEFAULT 0
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS faction_clusters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    faction INT NOT NULL,
    parent_cluster BINARY(16) NOT NULL,
    CONSTRAINT fk_faction_clusters_faction FOREIGN KEY (faction) REFERENCES factions(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS zone_clusters (
    id INT AUTO_INCREMENT PRIMARY KEY,
    zone_type VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS faction_claims (
    id INT AUTO_INCREMENT PRIMARY KEY,
    chunk_x INT NOT NULL,
    chunk_z INT NOT NULL,
    faction_id INT NOT NULL,
    world VARCHAR(50) NOT NULL DEFAULT 'world',
    zone_type VARCHAR(25) NOT NULL DEFAULT 'faction',
    cluster_id BINARY(16),
    CONSTRAINT fk_faction_claims_cluster FOREIGN KEY (cluster_id) REFERENCES clusters(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS faction_invites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inviter_id INT NOT NULL,
    invited_id INT NOT NULL,
    faction_id INT NOT NULL,
    rank_id INT NOT NULL DEFAULT 0,
    expiration_date DATETIME NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS known_offline_players (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(16) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS faction_homes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    z DOUBLE NOT NULL,
    world VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS faction_relations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    source_faction_id INT NOT NULL,
    target_faction_id INT NOT NULL,
    relation_type INT NOT NULL,
    CONSTRAINT fk_faction_relations_source FOREIGN KEY (source_faction_id) REFERENCES factions(id),
    CONSTRAINT fk_faction_relations_target FOREIGN KEY (target_faction_id) REFERENCES factions(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS faction_ally_invites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    source_faction_id INT NOT NULL,
    target_faction_id INT NOT NULL,
    expiration_date DATETIME NOT NULL,
    CONSTRAINT fk_faction_ally_invites_source FOREIGN KEY (source_faction_id) REFERENCES factions(id),
    CONSTRAINT fk_faction_ally_invites_target FOREIGN KEY (target_faction_id) REFERENCES factions(id)
) ENGINE=InnoDB;
