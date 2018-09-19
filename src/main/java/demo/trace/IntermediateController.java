package demo.trace;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.annotation.Timed;

@ConditionalOnProperty("intermediate")
@EnableDiscoveryClient
@RestController
public class IntermediateController {
	
	@Autowired RestTemplate restTemplate;
	
	@Autowired private DiscoveryClient discoveryClient;
	
	private static Random r = new Random();
	
	@Bean RestTemplate restTemplate() {
	    return new RestTemplate();
	}


	@RequestMapping("/api")
	@Timed(value = "api.frontend", percentiles = { 0.5,0.75, 0.99 } )
    public String api() throws InterruptedException {
		System.out.println("Intermediate Called!");
		int i = r.nextInt(500);
		System.out.println("Pausing for " + i + " ms!");
		TimeUnit.MILLISECONDS.sleep(i);
		ServiceInstance instance = this.discoveryClient.getInstances("backend").get(0);
		System.out.println("Instance:" + instance.getUri() );
		String obj = restTemplate.getForObject(instance.getUri()+"/api", String.class);
		System.out.println("Obj:" + obj );
		return obj;
    }
	
}
