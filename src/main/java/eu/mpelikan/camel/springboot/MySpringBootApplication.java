package eu.mpelikan.camel.springboot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class MySpringBootApplication {

    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:META-INF/spring/route-http4.xml");
    }
}
