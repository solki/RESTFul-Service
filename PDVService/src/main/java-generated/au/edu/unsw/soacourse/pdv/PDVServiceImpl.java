package au.edu.unsw.soacourse.pdv;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

import au.edu.unsw.soacourse.pdv.*;


@WebService(endpointInterface = "au.edu.unsw.soacourse.pdv.PDVService")
public class PDVServiceImpl implements PDVService {

	private ObjectFactory of;
	private PDVXMLHandler handler;
	
	@Override
	@WebMethod
	public PDVResponse pdvCheck(PDVRequest parameters) {
		
		this.of = new ObjectFactory();
		this.handler = new PDVXMLHandler();
		
		DriverPDV driver = new DriverPDV();
		
		System.out.println("-----------------------------------");
		System.out.println(parameters.getLicenseNumber().trim());
		System.out.println(parameters.getFullName().trim());
		System.out.println(parameters.getPostcode().trim());
		System.out.println("-----------------------------------");
		driver.setLicenseNumber(parameters.getLicenseNumber().trim());
		driver.setFullName(parameters.getFullName().trim());
		driver.setPostcode(parameters.getPostcode().trim());
		
		ArrayList<DriverPDV> driverlist = handler.translateToDriverPDV();
		
		PDVResponse response = of.createPDVResponse();
		response.setResponseCode(PDVResponseCode.NO_MATCH);
		response.setResponseMessage("Applicant's information has no match in Database.");
		for (int i = 0; i < driverlist.size(); i++) {
			DriverPDV curdriver = driverlist.get(i);
			if (curdriver.getLicenseNumber().equals(driver.getLicenseNumber())) {
				response.setResponseCode(PDVResponseCode.PARTIAL_MATCH);
				response.setResponseMessage("Applicant's personal information does not fully match.");
				
				if (curdriver.getFullName().equals(driver.getFullName()) &&
						curdriver.getPostcode().equals(driver.getPostcode())) {
					response.setResponseCode(PDVResponseCode.ALL_MATCH);
					response.setResponseMessage("Applicant's information has all matched.");
				} 
				
			}
		}
		
		return response;
	}

}
