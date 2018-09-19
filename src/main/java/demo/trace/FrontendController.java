package demo.trace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

@ConditionalOnProperty("frontend")
@EnableDiscoveryClient
@RestController
@Timed("frontend")
public class FrontendController {
	
	private static void dumpVars(Map<String, ?> m) {
	  List<String> keys = new ArrayList<String>(m.keySet());
	  Collections.sort(keys);
	  for (String k : keys) {
	    System.out.println(k + " : " + m.get(k));
	  }
	}

	
	private String obj = null;
	private String uri = null; 
	
	@Autowired 
	RestTemplate restTemplate;
	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	@Bean 
//	@LoadBalanced
	RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	@RequestMapping("/")
	public String hello() {
		return "Hello!"; 
	}
	
	// try the method 9 times with 2 seconds delay.
//	@Retryable(maxAttempts=9/*,value=org.springframework.web.client.HttpClientErrorException.class*/,backoff=@Backoff(delay = 2000))
	private void makeRestCall(String uri) throws Exception {
	    System.out.println("try!:"+uri);
	    String obj = restTemplate.getForObject(uri, String.class);
	    System.out.println("try! returned:"+obj);
	    this.obj = obj;
	}
	
	@RequestMapping("/api")
	@Timed(value = "api.frontend", percentiles = { 0.5,0.75, 0.99 } )
    public String api() throws Exception {
		System.out.println("Frontend Called!");
		ServiceInstance instance = this.discoveryClient.getInstances("intermediate").get(0);
		List<ServiceInstance> instances = this.discoveryClient.getInstances("intermediate");
		System.out.println("Instances:");

//		for (ServiceInstance i in instances)
//			System.out.println(i.getUri());
			
		System.out.println("Instance:"+instance.getUri());
	    String obj = restTemplate.getForObject(instance.getUri()+"/api", String.class);
	    System.out.println("obj:"+obj);
	    
	    System.out.println("===== ENV VARIABLES =====");
	    dumpVars(System.getenv());
	    
	    return obj;
    }
	
	public String fallbackAPI() {
		return "No Time Available!";
	}
	
}
