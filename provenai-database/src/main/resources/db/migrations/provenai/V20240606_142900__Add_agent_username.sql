
ALTER TABLE proven_ai.agents
ADD COLUMN IF NOT EXISTS agent_username TEXT NULL;

ALTER TABLE proven_ai.agents
ADD COLUMN IF NOT EXISTS agent_user_id UUID NULL;

comment on column proven_ai.agents.agent_username is 'The username of the agent, in the IDP (Keycloak)';

UPDATE proven_ai.agents
SET agent_username = LOWER(agent_name)
WHERE agent_username IS NULL;

