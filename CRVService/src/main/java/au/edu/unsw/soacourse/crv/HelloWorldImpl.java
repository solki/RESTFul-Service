
package au.edu.unsw.soacourse.crv;

import javax.jws.WebService;

@WebService(endpointInterface = "au.edu.unsw.soacourse.crv.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    public String sayHi(String text) {
        return "Hello " + text;
    }
}

