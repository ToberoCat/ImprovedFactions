DELETE
FROM faction_relations
WHERE registry_id = @registry
   OR relation_registry_id = @registry