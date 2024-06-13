-- Add column is_natural_person to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'is_natural_person'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN is_natural_person boolean;
    END IF;
END $$;

-- Add column legalPersonIdentifier to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'legal_person_identifier'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN legal_person_identifier text;
    END IF;
END $$;

-- Add column legalName to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'legal_name'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN legal_name text;
    END IF;
END $$;


-- Add column legalAddress to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'legal_address'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN legal_address text;
    END IF;
END $$;

-- Add column taxReference to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'tax_reference'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN tax_reference text;
    END IF;
END $$;

-- Add column familyName to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'family_name'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN family_name text;
    END IF;
END $$;

-- Add column firstName to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'first_name'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN first_name text;
    END IF;
END $$;

-- Add column dateOfBirth to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'date_of_birth'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN date_of_birth date;
    END IF;
END $$;

-- Add column gender to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'gender'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN gender text;
    END IF;
END $$;


-- Add column nationality to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'nationality'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN nationality text;
    END IF;
END $$;

-- Add column personalIdentifier  to organizations table
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'proven_ai'
        AND table_name = 'organizations'
        AND column_name = 'personal_identifier'
    ) THEN
        ALTER TABLE proven_ai.organizations
        ADD COLUMN personal_identifier text;
    END IF;
END $$;




