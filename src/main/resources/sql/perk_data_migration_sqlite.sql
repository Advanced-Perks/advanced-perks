-- Drop the temporary table if it exists
DROP TABLE IF EXISTS temp_aggregated_data;

-- Create a temporary table to hold the aggregated data
CREATE TEMPORARY TABLE temp_aggregated_data (
                                                unique_id TEXT PRIMARY KEY,
                                                enabled_perks TEXT,
                                                bought_perks TEXT,
                                                data_hash BLOB
);

-- Insert or replace the temporary table with aggregated data
INSERT OR REPLACE INTO temp_aggregated_data
SELECT
    ep.uuid AS unique_id,
    GROUP_CONCAT(DISTINCT ep.perk) AS enabled_perks,
    substr(GROUP_CONCAT(DISTINCT up.perk), 2) AS bought_perks,
    CAST(0 AS BLOB) AS data_hash
FROM
    enabled_perks ep
        LEFT JOIN unlocked_perks up ON ep.uuid = up.uuid
WHERE
    ep.uuid IS NOT NULL AND ep.uuid != '' AND up.uuid IS NOT NULL AND up.uuid != ''
GROUP BY
    ep.uuid;

-- Insert into ap_data, replace if the unique_id already exists
INSERT OR REPLACE INTO ap_data (unique_id, enabled_perks, bought_perks, data_hash)
SELECT * FROM temp_aggregated_data;
