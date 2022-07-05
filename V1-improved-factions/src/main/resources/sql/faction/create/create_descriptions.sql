-- SET @registry := 'registry';
-- SET @content := 'content';

INSERT INTO faction_descriptions (registry_id, content)
VALUE (@registry, @content)