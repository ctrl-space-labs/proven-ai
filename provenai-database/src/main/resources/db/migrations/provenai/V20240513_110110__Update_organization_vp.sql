DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'organizations'
        AND column_name = 'verifiable_id_vp'
    ) THEN
        EXECUTE 'ALTER TABLE proven_ai.organizations ALTER COLUMN verifiable_id_vp TYPE TEXT';
    END IF;
END $$;


DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'verifiable_id_vp'
    ) THEN
        ALTER TABLE proven_ai.organizations RENAME COLUMN verifiable_id_vp TO organization_vp_jwt;
    END IF;
END $$;