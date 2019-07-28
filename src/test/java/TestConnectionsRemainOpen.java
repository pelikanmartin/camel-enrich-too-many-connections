import java.util.HashMap;
import java.util.Map;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestConnectionsRemainOpen extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/route-http.xml");
    }

    /**
     * Test no pooling route, 100 connections will be opened.
     */
    @Test
    public void testMultipleEndpointsWithoutConnPooling() throws Exception {
        for (int i = 0; i < 100; i++) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("attachmentUrl",
                    "https4://localhost:4447/server/file/1/" + i + "?httpClient.socketTimeout=5000"
                            + "&httpClient.connectTimeout=5000" + "&httpClient.connectionRequestTimeout=5000"
                            + "&sessionSupport=false");
            template.sendBodyAndHeaders("direct:in-nopooling", null, headers); 
        }
        Thread.sleep(60000);
    }
    
    /**
     * Test no pooling route, 1 connections will be opened.
     */
    @Test
    public void testSingleEndpointWithoutConnPooling() throws Exception {
        for (int i = 0; i < 100; i++) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("attachmentUrl",
                    "https4://localhost:4447/server/file/1/1" + "?httpClient.socketTimeout=5000"
                            + "&httpClient.connectTimeout=5000" + "&httpClient.connectionRequestTimeout=5000"
                            + "&sessionSupport=false");
            template.sendBodyAndHeaders("direct:in-nopooling", null, headers); 
        }
        Thread.sleep(60000);
    }

    /**
     * Test pooling route - 1 connection will be open
     */
    @Test
    public void testMultipleEndpointsWithConnPooling() throws Exception {
        for (int i = 0; i < 100; i++) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("CamelHttpPath", "/server/file/1/" + i);
            template.sendBodyAndHeaders("direct:in-pooling", null, headers);

        }
        Thread.sleep(60000);
    }

}
