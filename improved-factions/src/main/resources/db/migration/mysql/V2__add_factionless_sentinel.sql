-- MariaDB enforces the existing faction_users -> factions foreign key. The domain
-- represents a player without a faction with -1, so that value needs a matching
-- internal row. Snapshot queries deliberately exclude this implementation row.
INSERT INTO factions (id, name, owner, accumulated_power, max_power, default_rank, join_type)
SELECT -1, '__factionless__', UNHEX('00000000000000000000000000000000'), 0, 0, 0, 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM factions WHERE id = -1);
