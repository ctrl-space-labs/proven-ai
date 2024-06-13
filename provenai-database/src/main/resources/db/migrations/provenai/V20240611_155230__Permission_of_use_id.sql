-- Alter the table to set the default value for permission_of_use_vc_id
ALTER TABLE proven_ai.audit_permission_of_use_vc
ALTER COLUMN permission_of_use_vc_id SET DEFAULT public.uuid_generate_v4();