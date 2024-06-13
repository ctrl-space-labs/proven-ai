DO $$
BEGIN
    -- Check and add the column document_section_iscc
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                   WHERE table_schema = 'proven_ai'
                   AND table_name = 'audit_permission_of_use_vc'
                   AND column_name = 'document_iscc') THEN
        ALTER TABLE proven_ai.audit_permission_of_use_vc
        ADD COLUMN document_section_iscc TEXT;
    END IF;

    -- Check and add the column processor_organization_did
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                   WHERE table_schema = 'proven_ai'
                   AND table_name = 'audit_permission_of_use_vc'
                   AND column_name = 'processor_organization_did') THEN
        ALTER TABLE proven_ai.audit_permission_of_use_vc
        ADD COLUMN processor_organization_did TEXT;
    END IF;

    -- Check and add the column owner_organization_did
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                   WHERE table_schema = 'proven_ai'
                   AND table_name = 'audit_permission_of_use_vc'
                   AND column_name = 'owner_organization_did') THEN
        ALTER TABLE proven_ai.audit_permission_of_use_vc
        ADD COLUMN owner_organization_did TEXT;
    END IF;

    -- Check and add the column embedding_model
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                   WHERE table_schema = 'proven_ai'
                   AND table_name = 'audit_permission_of_use_vc'
                   AND column_name = 'embedding_model') THEN
        ALTER TABLE proven_ai.audit_permission_of_use_vc
        ADD COLUMN embedding_model TEXT;
    END IF;

    -- Check and add the column datapod_id
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                   WHERE table_schema = 'proven_ai'
                   AND table_name = 'audit_permission_of_use_vc'
                   AND column_name = 'datapod_id') THEN
        ALTER TABLE proven_ai.audit_permission_of_use_vc
        ADD COLUMN datapod_id UUID;
    END IF;
END $$;
