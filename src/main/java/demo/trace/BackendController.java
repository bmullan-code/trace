package demo.trace;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;

@ConditionalOnProperty("backend")
@RestController
//@Timed("backend")
public class BackendController {
	
//	@Autowired
//    private DiscoveryClient discoveryClient;
	
	private static Random r = new Random();

	@RequestMapping("/api")
//	@Timed(value = "api.frontend", percentiles = { 0.5, 0.75, 0.99 } )
    public String api() throws InterruptedException {
		
		int i = r.nextInt(500);
		System.out.println("Pausing for " + i + " ms!");
		TimeUnit.MILLISECONDS.sleep(i);
		return new Date().toString();
    }
	
}
