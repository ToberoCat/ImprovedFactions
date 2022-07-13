DELETE
FROM factions
WHERE registry_id = @registry;

DELETE
FROM faction_descriptions
WHERE registry_id = @registry;

DELETE
FROM faction_bans
WHERE registry_id = @registry;

DELETE
FROM faction_relations
WHERE registry_id = @registry
   OR relation_registry_id = @registry;

DELETE
FROM faction_settings
WHERE registry_id = @registry;

UPDATE players
SET faction     = NULL,
    member_rank = NULL
WHERE faction = @registry;