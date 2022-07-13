-- SET @registry := 'registry';
-- SET @display := 'display';
-- SET @motd := 'motd';
-- SET @tag := 'tag';
-- SET @frozen := 'frozen';
-- SET @permanent := 'perm';
-- SET @created_at := '2022-01-01';
-- SET @owner := 'my-uuid';
-- SET @claimed_chunks := '69';
-- SET @balance := '420';
-- SET @current_power := '20';
-- SET @max_power := '187';

-- Write factions
INSERT INTO factions
    VALUE (
           @registry, -- Registry
           @display, -- Display
           @motd, -- Motd
           @tag, -- Tag
           @frozen, -- Frozen
           @permanent, -- Permanent
           @created_at, -- CreatedAt
           @owner, -- Owner
           @claimed_chunks, -- ClaimedChunks
           @balance, -- Balance
           @current_power, -- Current power
           @max_power -- Max power
    );