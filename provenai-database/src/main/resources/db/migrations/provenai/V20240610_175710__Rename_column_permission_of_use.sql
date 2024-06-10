DO $$
BEGIN
    -- Check if the column document_section_iscc exists
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
          AND table_name = 'audit_permission_of_use_vc'
          AND column_name = 'document_section_iscc'
    ) THEN
        -- Rename the column to document_iscc
        ALTER TABLE proven_ai.audit_permission_of_use_vc
        RENAME COLUMN document_section_iscc TO document_iscc;
    END IF;
END $$;
