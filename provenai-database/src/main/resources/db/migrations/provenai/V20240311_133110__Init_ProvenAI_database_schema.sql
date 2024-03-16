CREATE SCHEMA IF NOT EXISTS proven_ai;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";



-------------- Create table for Gendox ----------------

-- Table to store registered AI Agent SSI did:key
CREATE TABLE IF NOT EXISTS gendox_core.wallet_keys
(
    id          UUID                 DEFAULT uuid_generate_v4(),
    organization_id     UUID        NOT NULL, --this is the external system id, eg, the Gendox's Agent User id (UUID)
    private_key TEXT        NOT NULL,
    key_type_id TEXT        NOT NULL, -- EdDSA_Ed25519, ECDSA_Secp256k1, ECDSA_Secp256r1, RSA
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by  UUID        NOT NULL,
    updated_by  UUID        NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (organization_id) REFERENCES gendox_core.organizations (id),
    FOREIGN KEY (key_type_id) REFERENCES gendox_core.types (id)
);


-- Table to store user's DID (a user can be both human and AI Agent)
CREATE TABLE IF NOT EXISTS gendox_core.organization_dids
(
    id         UUID                 DEFAULT uuid_generate_v4(),
    organization_id UUID        NOT NULL,
    key_id     UUID        NULL,     --this is the id of the key in the wallet_keys table, if null, then ProvenAI will not be able to sign credentials on behalf of the user
    did        TEXT        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY (id),
    FOREIGN KEY (organization_id) REFERENCES gendox_core.organizations (id),
);



INSERT INTO gendox_core.types (type_category, name, description)
SELECT 'KEY_TYPE', 'RSA', 'RSA Key Type'
WHERE NOT EXISTS (SELECT 1
                  FROM gendox_core.types
                  WHERE type_category = 'KEY_TYPE'
                    AND name = 'RSA');

INSERT INTO gendox_core.types (type_category, name, description)
SELECT 'KEY_TYPE', 'ECDSA_SECP256K1', 'ECDSA secp256k1 Key Type'
WHERE NOT EXISTS (SELECT 1
                  FROM gendox_core.types
                  WHERE type_category = 'KEY_TYPE'
                    AND name = 'ECDSA_SECP256K1');

INSERT INTO gendox_core.types (type_category, name, description)
SELECT 'KEY_TYPE', 'ECDSA_SECP256R1', 'ECDSA secp256r1 Key Type'
WHERE NOT EXISTS (SELECT 1
                  FROM gendox_core.types
                  WHERE type_category = 'KEY_TYPE'
                    AND name = 'ECDSA_SECP256R1');

INSERT INTO gendox_core.types (type_category, name, description)
SELECT 'KEY_TYPE', 'EDDSA_ED25519', 'EdDSA Ed25519 Key Type'
WHERE NOT EXISTS (SELECT 1
                  FROM gendox_core.types
                  WHERE type_category = 'KEY_TYPE'
                    AND name = 'EDDSA_ED25519');


-------------------------------------------------------

-----------------------------------------------------------
------------------    ProvenAI ACL   ----------------------

-- Table to store registered organizations
-- columns are: id, organization id, created/updated by & at
create table if not exists proven_ai.registered_organizations
(
    id         UUID                 DEFAULT uuid_generate_v4(),
    org_id     UUID        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by UUID        NOT NULL,
    updated_by UUID        NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT registered_organizations_org_id_key UNIQUE (org_id)
);

create table if not exists proven_ai.types
(
    id            bigserial NOT NULL,
    type_category TEXT      NOT NULL,
    name          TEXT      NOT NULL,
    description   TEXT      NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS types_type_category_name_idx ON proven_ai.types (type_category, name);
CREATE INDEX IF NOT EXISTS types_name_idx ON proven_ai.types (name);


-- INSERT INTO proven_ai.types (type_category, name, description)
-- SELECT 'POLICY_CATEGORY', 'USAGE_POLICY', 'Usage Policy, indicate how the content will be used'
-- WHERE NOT EXISTS (SELECT 1
--                   FROM proven_ai.types
--                   WHERE type_category = 'POLICY_CATEGORY'
--                     AND name = 'USAGE_POLICY');
--
-- INSERT INTO proven_ai.types (type_category, name, description)
-- SELECT 'POLICY_CATEGORY', 'ATTRIBUTION_POLICY', 'Attribution Policy, indicate how the content will be attributed'
-- WHERE NOT EXISTS (SELECT 1
--                   FROM proven_ai.types
--                   WHERE type_category = 'POLICY_CATEGORY'
--                     AND name = 'ATTRIBUTION_POLICY');

-- create separate table for policies
CREATE TABLE IF NOT EXISTS proven_ai.policy_types
(
    id          UUID DEFAULT uuid_generate_v4(),
    name        TEXT NOT NULL,
    description TEXT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT policies_name_key UNIQUE (name)
);


INSERT INTO proven_ai.policy_types (name, description)
SELECT 'USAGE_POLICY', 'Usage Policy, indicate how the content will be used'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_types
                  WHERE name = 'USAGE_POLICY');

INSERT INTO proven_ai.policy_types (name, description)
SELECT 'ATTRIBUTION_POLICY', 'Attribution Policy, indicate how the content will be attributed'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_types
                  WHERE name = 'ATTRIBUTION_POLICY');

INSERT INTO proven_ai.policy_types (name, description)
SELECT 'COMPENSATION_POLICY', 'Compensation Policy, indicate how the content will be compensated'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_types
                  WHERE name = 'COMPENSATION_POLICY');

-- table to store user's access control policy options
CREATE TABLE IF NOT EXISTS proven_ai.policy_options
(
    id          UUID DEFAULT uuid_generate_v4(),
    policy_id   UUID NOT NULL,
    name        TEXT NOT NULL,
    description TEXT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT policy_options_policy_id_name_key UNIQUE (policy_id, name),
    FOREIGN KEY (policy_id) REFERENCES proven_ai.policy_types (id)
);

INSERT INTO proven_ai.policy_options (policy_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'USAGE_POLICY'),
       'SCHOOL_ASSISTANT',
       'Allow the content to be used from an AI educator assistant'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_id = (SELECT id
                                     FROM proven_ai.policy_types
                                     WHERE name = 'USAGE_POLICY')
                    AND name = 'SCHOOL_ASSISTANT');

INSERT INTO proven_ai.policy_options (policy_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'USAGE_POLICY'),
       'CORPORATE_ASSISTANT',
       'Allow the content to be used from an AI educator assistant'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_id = (SELECT id
                                     FROM proven_ai.policy_types
                                     WHERE name = 'USAGE_POLICY')
                    AND name = 'CORPORATE_ASSISTANT');

INSERT INTO proven_ai.policy_options (policy_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'USAGE_POLICY'),
       'ONLINE_COURSE',
       'Allow the content to be in an online course'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_id = (SELECT id
                                     FROM proven_ai.policy_types
                                     WHERE name = 'USAGE_POLICY')
                    AND name = 'ONLINE_COURSE');

INSERT INTO proven_ai.policy_options (policy_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'USAGE_POLICY'),
       'GENERAL_ASSISTANT',
       'Allow the content to be use in general assistant AIs'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_id = (SELECT id
                                     FROM proven_ai.policy_types
                                     WHERE name = 'USAGE_POLICY')
                    AND name = 'ASSISTANT_GPT');


INSERT INTO proven_ai.policy_options (policy_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'ATTRIBUTION_POLICY'),
       'OWNER_PROFILE',
       'The content will be attributed to the owner profile'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_id = (SELECT id
                                     FROM proven_ai.policy_types
                                     WHERE name = 'ATTRIBUTION_POLICY')
                    AND name = 'OWNER_PROFILE');

INSERT INTO proven_ai.policy_options (policy_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'ATTRIBUTION_POLICY'),
       'ORIGINAL_DOCUMENT',
       'The content will be attributed to the original document'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_id = (SELECT id
                                     FROM proven_ai.policy_types
                                     WHERE name = 'ATTRIBUTION_POLICY')
                    AND name = 'ORIGINAL_DOCUMENT');

INSERT INTO proven_ai.policy_options (policy_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'COMPENSATION_POLICY'),
       'PROPORTIONAL',
       'The content will be compensated proportionally'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_id = (SELECT id
                                     FROM proven_ai.policy_types
                                     WHERE name = 'COMPENSATION_POLICY')
                    AND name = 'PROPORTIONAL');

INSERT INTO proven_ai.policy_options (policy_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'COMPENSATION_POLICY'),
       'FIXED',
       'The content will be compensated with a fixed amount per word token'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_id = (SELECT id
                                     FROM proven_ai.policy_types
                                     WHERE name = 'COMPENSATION_POLICY')
                    AND name = 'FIXED');

INSERT INTO proven_ai.policy_options (policy_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'COMPENSATION_POLICY'),
       'BLOCKCHAIN_TOKEN',
       'The content will be compensated with a blockchain token (Not implemented yet)'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_id = (SELECT id
                                     FROM proven_ai.policy_types
                                     WHERE name = 'COMPENSATION_POLICY')
                    AND name = 'PER_TOKEN');


-- The root table of ProvenAI to store access control list

CREATE TABLE IF NOT EXISTS proven_ai.acl_groups
(
    id                         UUID                 DEFAULT uuid_generate_v4(),
    registered_organization_id UUID        NOT NULL,
    repository_id              UUID        NOT NULL, --this is the external system id, eg, the Gendox's project id (UUID)
    repository_unique_name     TEXT        NOT NULL, --unique name for the repository, eg, [organization_name]/[project_name]
    created_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by                 UUID        NOT NULL,
    updated_by                 UUID        NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (registered_organization_id) REFERENCES proven_ai.registered_organizations (id),
    CONSTRAINT acl_groups_repository_id_key UNIQUE (repository_id)
);

-- Table to store user's access control policies for a acl_group
CREATE TABLE IF NOT EXISTS proven_ai.acl_policies
(
    id               UUID                 DEFAULT uuid_generate_v4(),
    acl_group_id     UUID        NOT NULL,
    policy_id        UUID        NOT NULL,
    policy_option_id UUID        NOT NULL,
    value            TEXT        NOT NULL, -- an arbitrary field to store the policy value
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by       UUID        NOT NULL,
    updated_by       UUID        NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT acl_policies_acl_group_id_policy_id_policy_option_id_key UNIQUE (acl_group_id, policy_id),
    FOREIGN KEY (acl_group_id) REFERENCES proven_ai.acl_groups (id),
    FOREIGN KEY (policy_id) REFERENCES proven_ai.policy_types (id),
    FOREIGN KEY (policy_option_id) REFERENCES proven_ai.policy_options (id)
);


------------------    ProvenAI ACL   ----------------------
-----------------------------------------------------------
-----------------------------------------------------------
-----------    ProvenAI Registered Agents   ---------------


.....


-----------    ProvenAI Registered Agents   ---------------
-----------------------------------------------------------
-----------------------------------------------------------
----------------    ProvenAI Auditing   -------------------

    ......

----------------    ProvenAI Auditing   -------------------
-----------------------------------------------------------
