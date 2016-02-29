package au.edu.unsw.soacourse.pdv;

public class DriverPDV {
	private String licenseNumber;
	private String fullName;
	private String postcode;
	
	
	
	public DriverPDV() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DriverPDV(String licenseNumber, String fullName, String postcode) {
		super();
		this.licenseNumber = licenseNumber;
		this.fullName = fullName;
		this.postcode = postcode;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	
	
}
