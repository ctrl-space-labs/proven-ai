-- insert in registered organizations
INSERT INTO proven_ai.organizations (id, name) VALUES
('c83a1c61-4c79-4c49-8b3e-249e8c40a39f', 'Ctrl+space Labs');


-- insert in data pod
INSERT INTO proven_ai.data_pod (id, organization_id) VALUES
('0c154e6e-04b3-492a-94bf-61b8c5ad1644', 'c83a1c61-4c79-4c49-8b3e-249e8c40a39f');

-- insert acl policies
INSERT INTO proven_ai.acl_policies ( data_pod_id, policy_type_id, policy_option_id) VALUES
( '0c154e6e-04b3-492a-94bf-61b8c5ad1644',
  (select id
   from proven_ai.policy_types
   where name = 'USAGE_POLICY'),
  (select id
   from proven_ai.policy_options
   where name = 'ONLINE_COURSE'));

Insert into proven_ai.acl_policies ( data_pod_id, policy_type_id, value) VALUES
( '0c154e6e-04b3-492a-94bf-61b8c5ad1644',
 (select id
  from proven_ai.policy_types
  where name = 'ALLOW_LIST'),
 '84c498bb-a516-4015-aadf-4c8f715b6449');


