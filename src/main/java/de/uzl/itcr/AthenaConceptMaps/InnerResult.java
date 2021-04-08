package main.java.de.uzl.itcr.AthenaConceptMaps;

class InnerResult {

	String sCode;
	String sDisplay;
	String tCode;
	String tDisplay;

	public InnerResult() {
	};

	public InnerResult(String sCode, String sDisplay, String tCode, String tDisplay) {
		this.sCode = sCode;
		this.sDisplay = sDisplay;
		this.tCode = tCode;
		this.tDisplay = tDisplay;
	}

	public String getsCode() {
		return sCode;
	}

	public void setsCode(String sCode) {
		this.sCode = sCode;
	}

	public String getsDisplay() {
		return sDisplay;
	}

	public void setsDisplay(String sDisplay) {
		this.sDisplay = sDisplay;
	}

	public String gettCode() {
		return tCode;
	}

	public void settCode(String tCode) {
		this.tCode = tCode;
	}

	public String gettDisplay() {
		return tDisplay;
	}

	public void settDisplay(String tDisplay) {
		this.tDisplay = tDisplay;
	}

}