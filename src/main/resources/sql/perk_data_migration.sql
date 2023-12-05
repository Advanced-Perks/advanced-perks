-- Create a temporary table to hold the aggregated data
CREATE TEMPORARY TABLE temp_aggregated_data (
                                                unique_id VARCHAR(36),
                                                enabled_perks VARCHAR(2048),
                                                bought_perks VARCHAR(2048),
                                                data_hash VARBINARY(16),
                                                PRIMARY KEY (unique_id)
);

-- Insert or update the temporary table with aggregated data
INSERT INTO temp_aggregated_data
SELECT
    ep.uuid AS unique_id,
    GROUP_CONCAT(DISTINCT ep.perk) AS enabled_perks,
    TRIM(LEADING ',' FROM GROUP_CONCAT(DISTINCT up.perk)) AS bought_perks,
    0x0 AS data_hash
FROM
    enabled_perks ep
        LEFT JOIN
    unlocked_perks up ON ep.uuid = up.uuid
WHERE
    ep.uuid IS NOT NULL AND ep.uuid != '' AND up.uuid IS NOT NULL AND up.uuid != ''
GROUP BY
    ep.uuid;

-- Insert into ap_data, update if unique key (unique_id) already exists
INSERT INTO ap_data (unique_id, enabled_perks, bought_perks, data_hash)
SELECT * FROM temp_aggregated_data
ON DUPLICATE KEY UPDATE
                     enabled_perks = VALUES(enabled_perks),
                     bought_perks = VALUES(bought_perks);

-- Drop the temporary table
DROP TEMPORARY TABLE IF EXISTS temp_aggregated_data;