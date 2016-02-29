package au.edu.unsw.soacourse.crv;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;




@WebService(endpointInterface = "au.edu.unsw.soacourse.crv.CRVService")
public class CRVServiceImpl implements CRVService {

	private ObjectFactory of;
	private CRVXMLHandler handler;
	
	@Override
	@WebMethod
	public CRVResponse crvCheck(CRVRequest parameters) {
		
		this.of = new ObjectFactory();
		this.handler = new CRVXMLHandler();
		
		String license = parameters.getLicenseNumber().trim();
		
		ArrayList<DriverCRV> driverlist = handler.translateToDriverCRV();
		CRVResponse response = of.createCRVResponse();
		response.setResponseCode(CRVResponseCode.UNKNOWN);
		response.setResponseMessage("Applicant's license has no match in criminal record, unknown.");
		for (int i = 0; i < driverlist.size(); i++) {
			DriverCRV curdriver = driverlist.get(i);
			if (curdriver.getLicenseNumber().equals(license)) {
				response.setResponseCode(CRVResponseCode.DIRTY);
				response.setResponseMessage("Applicant has criminal record. Description: " + curdriver.getDescription());
				
				if (curdriver.getIsClean()) {
					response.setResponseCode(CRVResponseCode.CLEAN);
					response.setResponseMessage("Applicant is clean and has no criminal record.");
				} 				
			}
		}
		
		return response;
	}

}
