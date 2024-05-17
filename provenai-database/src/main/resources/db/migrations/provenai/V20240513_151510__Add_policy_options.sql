-- Insert ALLOW_AGENT_NAME policy option
INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT pt.id, 'ALLOW_AGENT_NAME', 'Policy option for allowing specific agents access to a data pod'
FROM proven_ai.policy_types pt
WHERE pt.name = 'ALLOW_LIST'
  AND NOT EXISTS (
    SELECT 1
    FROM proven_ai.policy_options po
    WHERE po.policy_type_id = pt.id
      AND po.name = 'ALLOW_AGENT_NAME'
);

-- Insert DENY_AGENT_NAME policy option
INSERT INTO proven_ai.policy_options (policy_type_id, name, description)
SELECT pt.id, 'DENY_AGENT_NAME', 'Policy option for denying specific agents access to a data pod'
FROM proven_ai.policy_types pt
WHERE pt.name = 'DENY_LIST'
  AND NOT EXISTS (
    SELECT 1
    FROM proven_ai.policy_options po
    WHERE po.policy_type_id = pt.id
      AND po.name = 'DENY_AGENT_NAME'
);
