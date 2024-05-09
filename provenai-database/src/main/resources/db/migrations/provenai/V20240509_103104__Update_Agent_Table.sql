ALTER TABLE proven_ai.agents
ADD COLUMN IF NOT EXISTS agent_name TEXT;

ALTER TABLE proven_ai.agents
ALTER COLUMN agent_verifiable_id TYPE TEXT;

ALTER TABLE proven_ai.agent_purpose_of_use_policies
ALTER COLUMN id SET DEFAULT uuid_generate_v4();
