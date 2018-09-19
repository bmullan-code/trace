package demo.trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.cloud.sleuth.SpanAccessor;

@EnableAutoConfiguration(exclude = { 
	    SecurityAutoConfiguration.class 
})

@SpringBootApplication
public class TraceApplication {
	
//	@Autowired private SpanAccessor spanAccessor;

	
	public static void main(String[] args) {
		SpringApplication.run(TraceApplication.class, args);
	}
}

