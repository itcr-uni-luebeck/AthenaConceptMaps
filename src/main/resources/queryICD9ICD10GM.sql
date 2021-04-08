select * from
-- snomed zu idc9
(SELECT "CONCEPT".concept_name, "CONCEPT".concept_code as ICD9CMidConcept, "CONCEPT".vocabulary_id, r1.relationship_id, c2.concept_name, c2.concept_code, c2.vocabulary_id, c2.concept_id as ICD9CMid
from "CONCEPT"
LEFT JOIN (
"CONCEPT_RELATIONSHIP" as r1 FULL OUTER JOIN "CONCEPT" as c2 ON r1.concept_id_2 = c2.concept_id
)
ON "CONCEPT".concept_id = r1.concept_id_1
WHERE "CONCEPT".vocabulary_id = 'ICD9CM' AND c2.vocabulary_id = 'SNOMED' AND "CONCEPT".concept_code LIKE '%.%' AND c2.concept_name NOT like '%pregnancy%') snomedicd9

inner join

-- snomed zu icd10
(SELECT c2.concept_id as ICD10CMid, r1.relationship_id, "CONCEPT".concept_code, "CONCEPT".concept_name, "CONCEPT".vocabulary_id 
from "CONCEPT"
LEFT JOIN (
"CONCEPT_RELATIONSHIP" as r1 left JOIN "CONCEPT" as c2 ON r1.concept_id_2 = c2.concept_id
)
ON "CONCEPT".concept_id = r1.concept_id_1
WHERE "CONCEPT".vocabulary_id = 'ICD10GM' AND c2.vocabulary_id = 'SNOMED' AND "CONCEPT".concept_code LIKE '%.%') snomedicd10

on (ICD9CMid = ICD10CMid) 
ORDER BY ICD9CMidConcept ASC