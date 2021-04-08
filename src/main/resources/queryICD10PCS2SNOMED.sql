SELECT "CONCEPT".concept_name, "CONCEPT".concept_code as ICD9Proc, r1.relationship_id, c2.concept_name, c2.concept_id as SNOMED from "CONCEPT"
LEFT JOIN (
"CONCEPT_RELATIONSHIP" as r1 FULL OUTER JOIN "CONCEPT" as c2 ON r1.concept_id_2 = c2.concept_id
)
ON "CONCEPT".concept_id = r1.concept_id_1
WHERE "CONCEPT".vocabulary_id = 'ICD10PCS' AND c2.vocabulary_id = 'SNOMED' AND (r1.relationship_id = 'Is a' OR r1.relationship_id = 'Maps to')
ORDER BY ICD9Proc ASC