package au.edu.unsw.soacourse.crv;

public class DriverCRV {
	private String licenseNumber;
	private Boolean isClean;
	private String description;
	
	public DriverCRV() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DriverCRV(String licenseNumber, Boolean isClean, String description) {
		super();
		this.licenseNumber = licenseNumber;
		this.isClean = isClean;
		this.description = description;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public Boolean getIsClean() {
		return isClean;
	}

	public void setIsClean(Boolean isClean) {
		this.isClean = isClean;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
