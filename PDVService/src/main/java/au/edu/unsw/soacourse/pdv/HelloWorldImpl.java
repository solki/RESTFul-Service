
package au.edu.unsw.soacourse.pdv;

import javax.jws.WebService;

@WebService(endpointInterface = "au.edu.unsw.soacourse.pdv.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    public String sayHi(String text) {
        return "Hello " + text;
    }
}

