CREATE SCHEMA IF NOT EXISTS proven_ai;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
ALTER EXTENSION "uuid-ossp" SET SCHEMA public;


-----------------------------------------------------------
------------------    ProvenAI ACL   ----------------------

-- Table to store registered organizations
-- columns are: id, organization id, created/updated by & at
create table if not exists proven_ai.organizations
(
    id                      UUID        NOT NULL,
    name                    TEXT,
    country                 TEXT,
    VAT_number              TEXT,
    verifiable_id_vp JSONB,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by              UUID,
    updated_by              UUID,

    PRIMARY KEY (id)
);

comment on column proven_ai.organizations.id is 'The external organization id from the external system (like Gendox org id)';

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
    id          UUID DEFAULT public.uuid_generate_v4(),
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

-- insert ALLOW_LIST policy
INSERT INTO proven_ai.policy_types (name, description)
SELECT 'ALLOW_LIST', 'Allow List Policy, indicate the list of allowed entities'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_types
                  WHERE name = 'ALLOW_LIST');

-- insert DENY_LIST policy
INSERT INTO proven_ai.policy_types (name, description)
SELECT 'DENY_LIST', 'Deny List Policy, indicate the list of denied entities'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_types
                  WHERE name = 'DENY_LIST');

-- table to store user's access control policy options
CREATE TABLE IF NOT EXISTS proven_ai.policy_options
(
    id             UUID DEFAULT public.uuid_generate_v4(),
    policy_type_id UUID NOT NULL,
    name           TEXT NOT NULL,
    description    TEXT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT policy_options_policy_type_id_name_key UNIQUE (policy_type_id, name),
    FOREIGN KEY (policy_type_id) REFERENCES proven_ai.policy_types (id)
);

INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'USAGE_POLICY'),
       'SCHOOL_ASSISTANT',
       'Allow the content to be used from an AI educator assistant'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_type_id = (SELECT id
                                          FROM proven_ai.policy_types
                                          WHERE name = 'USAGE_POLICY')
                    AND name = 'SCHOOL_ASSISTANT');

INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'USAGE_POLICY'),
       'CORPORATE_ASSISTANT',
       'Allow the content to be used from an AI educator assistant'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_type_id = (SELECT id
                                          FROM proven_ai.policy_types
                                          WHERE name = 'USAGE_POLICY')
                    AND name = 'CORPORATE_ASSISTANT');

INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'USAGE_POLICY'),
       'ONLINE_COURSE',
       'Allow the content to be in an online course'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_type_id = (SELECT id
                                          FROM proven_ai.policy_types
                                          WHERE name = 'USAGE_POLICY')
                    AND name = 'ONLINE_COURSE');

INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'USAGE_POLICY'),
       'GENERAL_ASSISTANT',
       'Allow the content to be use in general assistant AIs'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_type_id = (SELECT id
                                          FROM proven_ai.policy_types
                                          WHERE name = 'USAGE_POLICY')
                    AND name = 'GENERAL_ASSISTANT');


INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'ATTRIBUTION_POLICY'),
       'OWNER_PROFILE',
       'The content will be attributed to the owner profile'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_type_id = (SELECT id
                                          FROM proven_ai.policy_types
                                          WHERE name = 'ATTRIBUTION_POLICY')
                    AND name = 'OWNER_PROFILE');

INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'ATTRIBUTION_POLICY'),
       'ORIGINAL_DOCUMENT',
       'The content will be attributed to the original document'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_type_id = (SELECT id
                                          FROM proven_ai.policy_types
                                          WHERE name = 'ATTRIBUTION_POLICY')
                    AND name = 'ORIGINAL_DOCUMENT');

INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'COMPENSATION_POLICY'),
       'PROPORTIONAL',
       'The content will be compensated proportionally'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_type_id = (SELECT id
                                          FROM proven_ai.policy_types
                                          WHERE name = 'COMPENSATION_POLICY')
                    AND name = 'PROPORTIONAL');

INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'COMPENSATION_POLICY'),
       'FIXED',
       'The content will be compensated with a fixed amount per word token'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_type_id = (SELECT id
                                          FROM proven_ai.policy_types
                                          WHERE name = 'COMPENSATION_POLICY')
                    AND name = 'FIXED');

INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT (SELECT id
        FROM proven_ai.policy_types
        WHERE name = 'COMPENSATION_POLICY'),
       'BLOCKCHAIN_TOKEN',
       'The content will be compensated with a blockchain token (Not implemented yet)'
WHERE NOT EXISTS (SELECT 1
                  FROM proven_ai.policy_options
                  WHERE policy_type_id = (SELECT id
                                          FROM proven_ai.policy_types
                                          WHERE name = 'COMPENSATION_POLICY')
                    AND name = 'BLOCKCHAIN_TOKEN');


-- The root table of ProvenAI to store access control list

CREATE TABLE IF NOT EXISTS proven_ai.data_pod
(
    id              UUID        NOT NULL,
    organization_id UUID        NOT NULL,
    pod_unique_name TEXT, --unique name for the pod, eg, [organization_name]/[project_name]
    host_url       TEXT, -- the host url of the data pod
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      UUID,
    updated_by      UUID,

    PRIMARY KEY (id),
    FOREIGN KEY (organization_id) REFERENCES proven_ai.organizations (id)
);

-- database comments for columns
COMMENT ON COLUMN proven_ai.data_pod.id IS 'The unique id of the data pod. It is an ID provided by the external system (eg. Gendox project id)';
COMMENT ON COLUMN proven_ai.data_pod.organization_id IS 'The registered organization id';

-- Table to store user's access control policies for a data pod
CREATE TABLE IF NOT EXISTS proven_ai.acl_policies
(
    id               UUID                 DEFAULT public.uuid_generate_v4(),
    data_pod_id      UUID        NOT NULL,
    policy_type_id   UUID        NOT NULL,
    policy_option_id UUID,
    value            TEXT, -- an arbitrary field to store the policy value
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by       UUID,
    updated_by       UUID,

    PRIMARY KEY (id),
    FOREIGN KEY (data_pod_id) REFERENCES proven_ai.data_pod (id),
    FOREIGN KEY (policy_type_id) REFERENCES proven_ai.policy_types (id),
    FOREIGN KEY (policy_option_id) REFERENCES proven_ai.policy_options (id)
);


------------------    ProvenAI ACL   ----------------------
-----------------------------------------------------------
-----------------------------------------------------------
-----------    ProvenAI Registered Agents   ---------------


-- table for registered agent
CREATE TABLE IF NOT EXISTS proven_ai.agents
(
    id              UUID        NOT NULL,
    organization_id UUID        NOT NULL,
    agent_verifiable_id        JSONB,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by      UUID,
    updated_by      UUID,

    PRIMARY KEY (id),
    FOREIGN KEY (organization_id) REFERENCES proven_ai.organizations (id)
);

COMMENT ON COLUMN proven_ai.agents.id IS 'The external agent id from the external system (like Gendox agent id)';

-- table for agent's purpose of use policies, also includes in the Actual VC
CREATE TABLE IF NOT EXISTS proven_ai.agent_purpose_of_use_policies
(
    id               UUID        NOT NULL,
    agent_id         UUID        NOT NULL,
    policy_type_id   UUID        NOT NULL,
    policy_option_id UUID        NOT NULL,
    value            TEXT        NOT NULL,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by       UUID,
    updated_by       UUID,

    PRIMARY KEY (id),
    FOREIGN KEY (agent_id) REFERENCES proven_ai.agents (id),
    FOREIGN KEY (policy_type_id) REFERENCES proven_ai.policy_types (id),
    FOREIGN KEY (policy_option_id) REFERENCES proven_ai.policy_options (id)
);

-----------    ProvenAI Registered Agents   ---------------
-----------------------------------------------------------
-----------------------------------------------------------
----------------    ProvenAI Auditing   -------------------


-- table for permission of use VCs (section response for a question) created for a search
CREATE TABLE IF NOT EXISTS proven_ai.audit_permission_of_use_vc
(
    permission_of_use_vc_id   UUID        NOT NULL,
    search_id                 UUID        NOT NULL,
    section_iscc              TEXT        NOT NULL,
    owner_organization_id     UUID        NOT NULL,
    processor_organization_id UUID        NOT NULL,
    tokens                    integer     NOT NULL, -- number of tokens in the section
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    PRIMARY KEY (permission_of_use_vc_id)
);


----------------    ProvenAI Auditing   -------------------
-----------------------------------------------------------
