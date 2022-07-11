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
UPDATE factions
SET display_name   = @display,        -- Display
    motd           = @motd,           -- Motd
    tag            = @tag,            -- Tag
    frozen         = @frozen,         -- Frozen
    permanent      = @permanent,      -- Permanent
    created_at     = @created_at,     -- CreatedAt
    owner          = @owner,          -- Owner
    claimed_chunks = @claimed_chunks, -- ClaimedChunks
    balance        = @balance,        -- Balance
    current_power  = @current_power,  -- Current power
    max_power      = @max_power       -- Max power
WHERE registry_id = @registry