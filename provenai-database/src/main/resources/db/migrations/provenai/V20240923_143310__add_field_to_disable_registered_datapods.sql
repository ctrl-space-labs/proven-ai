-- add column if not exists
ALTER TABLE proven_ai.data_pod
    ADD COLUMN IF NOT EXISTS is_disabled BOOLEAN DEFAULT FALSE;

-- add comment in column
COMMENT ON COLUMN proven_ai.data_pod.is_disabled IS 'Flag to disable a registered data pod for generic Agent search';


