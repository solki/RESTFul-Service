package au.edu.unsw.soacourse.autocheck;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.0.4
 * 2015-10-21T11:35:20.986+11:00
 * Generated source version: 3.0.4
 * 
 */
@WebServiceClient(name = "AutoCheckService", 
                  wsdlLocation = "http://localhost:6060/ode/processes/AutoCheckService?wsdl",
                  targetNamespace = "http://autocheck.soacourse.unsw.edu.au") 
public class AutoCheckService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://autocheck.soacourse.unsw.edu.au", "AutoCheckService");
    public final static QName AutoCheck = new QName("http://autocheck.soacourse.unsw.edu.au", "AutoCheck");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:6060/ode/processes/AutoCheckService?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(AutoCheckService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:6060/ode/processes/AutoCheckService?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public AutoCheckService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public AutoCheckService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AutoCheckService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public AutoCheckService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public AutoCheckService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public AutoCheckService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns AutoCheckServicePortType
     */
    @WebEndpoint(name = "AutoCheck")
    public AutoCheckServicePortType getAutoCheck() {
        return super.getPort(AutoCheck, AutoCheckServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AutoCheckServicePortType
     */
    @WebEndpoint(name = "AutoCheck")
    public AutoCheckServicePortType getAutoCheck(WebServiceFeature... features) {
        return super.getPort(AutoCheck, AutoCheckServicePortType.class, features);
    }

}