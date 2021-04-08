package main.java.de.uzl.itcr.AthenaConceptMaps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ConceptMap.ConceptMapGroupComponent;
import org.hl7.fhir.r4.model.ConceptMap.SourceElementComponent;
import org.hl7.fhir.r4.model.ConceptMap.TargetElementComponent;
import org.hl7.fhir.r4.model.Enumerations.ConceptMapEquivalence;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.UriType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class ConceptMapCreator {

	static String url = "jdbc:postgresql://XXX:5432/athena";
	static String user = "XXX";
	static String password = "XXX";

	public static void main(String args[]) {
		createConceptMapICD9ToICD10GM();
		createConceptMapICD9ProcToSCT();
		createConceptMapICD10ProcToSCT();
	}

	public static void createConceptMapICD9ToICD10GM() {
		ConceptMap cm = new ConceptMap();
		cm.setUrl("https://itcr.uni-luebeck.de/fhir/ConceptMap/ICD9_to_ICD10GM");
		cm.setName("ICD9 to ICD10GM");
		cm.setTitle("ICD9 to ICDM10GM - OHSDI Athena");
		cm.setStatus(PublicationStatus.DRAFT);
		cm.setDescription("ConceptMap to map ICD9 to ICD10GM over the SNOMED Mapping created from OHSDI Athena");
		cm.setVersion("2021-03-17");
		cm.setSource(new UriType("http://hl7.org/fhir/sid/icd-9-cm"));
		cm.setTarget(new UriType("http://fhir.de/CodeSystem/dimdi/icd-10-gm"));

		ConceptMapGroupComponent gc = new ConceptMapGroupComponent();
		gc.setSource("http://hl7.org/fhir/sid/icd-9-cm");
		gc.setTarget("http://fhir.de/CodeSystem/dimdi/icd-10-gm");
		HashMap<String, ArrayList<InnerResult>> hashElements = new HashMap<>();
		try {
			ResultSet rs = query("queryICD9ICD10GM.sql");
			while (rs.next()) {
				InnerResult iR = new InnerResult(rs.getString(2), rs.getString(1), rs.getString(5), rs.getString(4));
				if (hashElements.containsKey(iR.sCode)) {
					ArrayList<InnerResult> temp = hashElements.get(iR.sCode);
					temp.add(iR);
					hashElements.put(iR.sCode, temp);
				} else {
					ArrayList<InnerResult> temp = new ArrayList<>();
					temp.add(iR);
					hashElements.put(iR.sCode, temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<SourceElementComponent> elements = new ArrayList<SourceElementComponent>();
		for (String key : hashElements.keySet()) {
			ArrayList<InnerResult> inners = hashElements.get(key);
			SourceElementComponent sc = new SourceElementComponent();
			sc.setCode(inners.get(0).getsCode());
			sc.setDisplay(inners.get(0).getsDisplay());
			for (InnerResult t : inners) {
				TargetElementComponent tc = new TargetElementComponent();
				tc.setCode(t.gettCode());
				tc.setDisplay(t.gettDisplay());
				tc.setEquivalence(ConceptMapEquivalence.RELATEDTO);
				sc.addTarget(tc);
			}
			elements.add(sc);
		}

		gc.setElement(elements);
		ArrayList<ConceptMapGroupComponent> groups = new ArrayList<>();
		groups.add(gc);
		cm.setGroup(groups);
		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();
		String serialized = parser.encodeResourceToString(cm);
		try {
			FileWriter myWriter = new FileWriter("ICD9toICD10GM.json");
			myWriter.write(serialized);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
	}

	public static void createConceptMapICD9ProcToSCT() {
		ConceptMap cm = new ConceptMap();
		cm.setUrl("https://itcr.uni-luebeck.de/fhir/ConceptMap/ICD9Proc_to_SNOMEDCT");
		cm.setName("ICD9 Procedure to SNOMED CT");
		cm.setTitle("ICD9 Procedure to SNOMED CT - OHSDI Athena");
		cm.setStatus(PublicationStatus.DRAFT);
		cm.setDescription(
				"ConceptMap to map ICD9 Procedure to SNOMED CT over the SNOMED Mapping created from OHSDI Athena");
		cm.setVersion("2021-03-17");
		cm.setSource(new UriType("http://hl7.org/fhir/sid/icd-9-cm/procedure"));
		cm.setTarget(new UriType("http://snomed.info/sct"));

		ConceptMapGroupComponent gc = new ConceptMapGroupComponent();
		gc.setSource("http://hl7.org/fhir/sid/icd-9-cm/procedure");
		gc.setTarget("http://snomed.info/sct");
		HashMap<String, ArrayList<InnerResult>> hashElements = new HashMap<>();
		try {
			ResultSet rs = query("queryICD9Pro2SNOMED.sql");
			while (rs.next()) {
				InnerResult iR = new InnerResult(rs.getString(2), rs.getString(1), rs.getString(5), rs.getString(4));
				if (hashElements.containsKey(iR.sCode)) {
					ArrayList<InnerResult> temp = hashElements.get(iR.sCode);
					temp.add(iR);
					hashElements.put(iR.sCode, temp);
				} else {
					ArrayList<InnerResult> temp = new ArrayList<>();
					temp.add(iR);
					hashElements.put(iR.sCode, temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<SourceElementComponent> elements = new ArrayList<SourceElementComponent>();
		for (String key : hashElements.keySet()) {
			ArrayList<InnerResult> inners = hashElements.get(key);
			SourceElementComponent sc = new SourceElementComponent();
			sc.setCode(inners.get(0).getsCode());
			sc.setDisplay(inners.get(0).getsDisplay());
			for (InnerResult t : inners) {
				TargetElementComponent tc = new TargetElementComponent();
				tc.setCode(t.gettCode());
				tc.setDisplay(t.gettDisplay());
				tc.setEquivalence(ConceptMapEquivalence.RELATEDTO);
				sc.addTarget(tc);
			}
			elements.add(sc);
		}

		gc.setElement(elements);
		ArrayList<ConceptMapGroupComponent> groups = new ArrayList<>();
		groups.add(gc);
		cm.setGroup(groups);
		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();
		String serialized = parser.encodeResourceToString(cm);
		try {
			FileWriter myWriter = new FileWriter("ICD9PCS2SCT.json");
			myWriter.write(serialized);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
	}

	public static void createConceptMapICD10ProcToSCT() {
		ConceptMap cm = new ConceptMap();
		cm.setUrl("https://itcr.uni-luebeck.de/fhir/ConceptMap/ICD10PCS_to_SNOMEDCT");
		cm.setName("ICD10 Procedure to SNOMED CT");
		cm.setTitle("ICD10 Procedure to SNOMED CT - OHSDI Athena");
		cm.setStatus(PublicationStatus.DRAFT);
		cm.setDescription(
				"ConceptMap to map ICD10 Procedure to SNOMED CT over the SNOMED Mapping created from OHSDI Athena");
		cm.setVersion("2021-03-17");
		cm.setSource(new UriType("http://hl7.org/fhir/sid/icd-10-pcs"));
		cm.setTarget(new UriType("http://snomed.info/sct"));

		ConceptMapGroupComponent gc = new ConceptMapGroupComponent();
		gc.setSource("http://hl7.org/fhir/sid/icd-10-pcs");
		gc.setTarget("http://snomed.info/sct");
		HashMap<String, ArrayList<InnerResult>> hashElements = new HashMap<>();
		try {
			ResultSet rs = query("queryICD10PCS2SNOMED.sql");
			while (rs.next()) {
				InnerResult iR = new InnerResult(rs.getString(2), rs.getString(1), rs.getString(5), rs.getString(4));
				if (hashElements.containsKey(iR.sCode)) {
					ArrayList<InnerResult> temp = hashElements.get(iR.sCode);
					temp.add(iR);
					hashElements.put(iR.sCode, temp);
				} else {
					ArrayList<InnerResult> temp = new ArrayList<>();
					temp.add(iR);
					hashElements.put(iR.sCode, temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<SourceElementComponent> elements = new ArrayList<SourceElementComponent>();
		for (String key : hashElements.keySet()) {
			ArrayList<InnerResult> inners = hashElements.get(key);
			SourceElementComponent sc = new SourceElementComponent();
			sc.setCode(inners.get(0).getsCode());
			sc.setDisplay(inners.get(0).getsDisplay());
			for (InnerResult t : inners) {
				TargetElementComponent tc = new TargetElementComponent();
				tc.setCode(t.gettCode());
				tc.setDisplay(t.gettDisplay());
				tc.setEquivalence(ConceptMapEquivalence.RELATEDTO);
				sc.addTarget(tc);
			}
			elements.add(sc);
		}

		gc.setElement(elements);
		ArrayList<ConceptMapGroupComponent> groups = new ArrayList<>();
		groups.add(gc);
		cm.setGroup(groups);
		FhirContext ctx = FhirContext.forR4();
		IParser parser = ctx.newJsonParser();
		String serialized = parser.encodeResourceToString(cm);

		try {
			FileWriter myWriter = new FileWriter("ICD10PCS2SCT.json");
			myWriter.write(serialized);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
	}

	public static ResultSet query(String filename) throws IOException {
		URL temp = Thread.currentThread().getContextClassLoader().getResource(filename);
		File myObj = new File(temp.getPath());
		Scanner myReader = new Scanner(myObj);
		StringBuilder sb = new StringBuilder();
		while (myReader.hasNextLine()) {
			sb.append(myReader.nextLine() + "\n");
		}
		myReader.close();
		try {
			Connection con = getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sb.toString());
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getConnection() throws SQLException {
		Connection con = DriverManager.getConnection(url, user, password);
		return con;
	}
}
