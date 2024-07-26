-- Add the column document_iscc if it does not exist
ALTER TABLE proven_ai.audit_permission_of_use_vc
    ADD COLUMN IF NOT EXISTS document_iscc TEXT;

-- Add the column processor_organization_did if it does not exist
ALTER TABLE proven_ai.audit_permission_of_use_vc
    ADD COLUMN IF NOT EXISTS processor_organization_did TEXT;

-- Add the column owner_organization_did if it does not exist
ALTER TABLE proven_ai.audit_permission_of_use_vc
    ADD COLUMN IF NOT EXISTS owner_organization_did TEXT;

-- Add the column embedding_model if it does not exist
ALTER TABLE proven_ai.audit_permission_of_use_vc
    ADD COLUMN IF NOT EXISTS embedding_model TEXT;

-- Add the column datapod_id if it does not exist
ALTER TABLE proven_ai.audit_permission_of_use_vc
    ADD COLUMN IF NOT EXISTS owner_datapod_id UUID;


-- Add the column processor_agent_id if it does not exist
ALTER TABLE proven_ai.audit_permission_of_use_vc
    ADD COLUMN IF NOT EXISTS processor_agent_id UUID;

-- Alter the table to set the default value for permission_of_use_vc_id
ALTER TABLE proven_ai.audit_permission_of_use_vc
    ALTER COLUMN permission_of_use_vc_id SET DEFAULT public.uuid_generate_v4();



