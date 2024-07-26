-- Add column is_natural_person to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS is_natural_person BOOLEAN;

-- Add column legal_person_identifier to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS legal_person_identifier TEXT;

-- Add column legal_name to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS legal_name TEXT;

-- Add column legal_address to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS legal_address TEXT;

-- Add column tax_reference to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS tax_reference TEXT;

-- Add column family_name to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS family_name TEXT;

-- Add column first_name to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS first_name TEXT;

-- Add column date_of_birth to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS date_of_birth DATE;

-- Add column gender to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS gender TEXT;

-- Add column nationality to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS nationality TEXT;

-- Add column personal_identifier to organizations table
ALTER TABLE proven_ai.organizations
    ADD COLUMN IF NOT EXISTS personal_identifier TEXT;
