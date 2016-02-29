package au.edu.unsw.soacourse.pdv;

import javax.jws.WebService;

@WebService
public interface HelloWorld {
    String sayHi(String text);
}

