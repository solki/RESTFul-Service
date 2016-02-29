package au.edu.unsw.soacourse.pdv;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.0.4
 * 2015-10-21T11:33:46.545+11:00
 * Generated source version: 3.0.4
 * 
 */
@WebService(targetNamespace = "http://pdv.soacourse.unsw.edu.au", name = "PDVService")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PDVService {

    @WebResult(name = "PDVResponse", targetNamespace = "http://pdv.soacourse.unsw.edu.au", partName = "parameters")
    @WebMethod(operationName = "PDVCheck", action = "http://pdv.soacourse.unsw.edu.au/PDVCheck")
    public PDVResponse pdvCheck(
        @WebParam(partName = "parameters", name = "PDVRequest", targetNamespace = "http://pdv.soacourse.unsw.edu.au")
        PDVRequest parameters
    );
}