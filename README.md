# AthenaConceptMaps [![DOI](https://zenodo.org/badge/355865210.svg)](https://zenodo.org/badge/latestdoi/355865210)




This project creates three FHIR R4 ConceptMaps using data from [OHDSI Athena](https://athena.ohdsi.org/). These ConceptMaps are used for the translatation of ICD9 Codes into Snomed CT or ICD10 GM. The ConceptMaps are created by connecting ICD9 with ICD 10 GM through an intermediate step via SNOMED CT. The maps do not claim to be **complete**.

## Use

1. Head over to [OHDSI Athena](https://athena.ohdsi.org/) and download the following vocabularies:
	- ICD9CM
	- ICD9Proc
	- ICD10PCS
	- ICD10GM
	- SNOMED CT
2. Import the CSV files into a PostgreSQL database and use the CSV Header for the table creating.
3. Clone this project and insert the database credentials.
4. Run! 
