package au.edu.unsw.soacourse.crv;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.0.4
 * 2015-10-21T11:34:00.318+11:00
 * Generated source version: 3.0.4
 * 
 */
@WebService(targetNamespace = "http://crv.soacourse.unsw.edu.au", name = "CRVService")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface CRVService {

    @WebResult(name = "CRVResponse", targetNamespace = "http://crv.soacourse.unsw.edu.au", partName = "parameters")
    @WebMethod(operationName = "CRVCheck", action = "http://crv.soacourse.unsw.edu.au/CRVCheck")
    public CRVResponse crvCheck(
        @WebParam(partName = "parameters", name = "CRVRequest", targetNamespace = "http://crv.soacourse.unsw.edu.au")
        CRVRequest parameters
    );
}