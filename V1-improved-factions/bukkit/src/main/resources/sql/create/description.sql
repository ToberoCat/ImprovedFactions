-- SET @registry := 'registry';
-- SET @content := 'content';
-- SET @line := 1;

INSERT INTO faction_descriptions
    VALUE (@registry, @line, @content)