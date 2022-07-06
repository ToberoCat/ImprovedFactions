-- SET @registry := 'registry';
-- SET @line := 1;
-- SET @content := 'content';

UPDATE faction_descriptions
    SET content = @content
    WHERE registry_id = @registry AND line = @line