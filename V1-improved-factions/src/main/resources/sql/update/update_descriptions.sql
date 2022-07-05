-- SET @registry := 'registry';
-- SET @content := 'content';

UPDATE faction_descriptions
    SET content = @content
    WHERE registry_id = @registry