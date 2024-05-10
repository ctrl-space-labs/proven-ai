-- Update agent table: Rename column if it exists
DO $$ BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'agent' AND column_name = 'agent_verifiable_id') THEN
        ALTER TABLE agent RENAME COLUMN agent_verifiable_id TO agent_vc_jwt;
    END IF;
END $$;

ALTER TABLE proven_ai.organizations
ADD COLUMN IF NOT EXISTS organization_did TEXT;
