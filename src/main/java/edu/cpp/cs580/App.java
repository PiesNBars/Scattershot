package edu.cpp.cs580;

import edu.cpp.cs580.customer.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.cpp.cs580.data.provider.FSUserManager;
import edu.cpp.cs580.data.provider.UserManager;

@SpringBootApplication
public class App {

	@Autowired
	private CustomerRepository repo;
	
    /**
     * This is a good example of how Spring instantiates
     * objects. The instances generated from this method
     * will be used in this project, where the Autowired
     * annotation is applied.
     */
    @Bean
    public UserManager userManager() {
        UserManager userManager = new FSUserManager();
        return userManager;
    }
    
    /**
     * This is the running main method for the web application.
     * Please note that Spring requires that there is one and
     * ONLY one main method in your whole program. You can create
     * other main methods for testing or debugging purposes, but
     * you cannot put extra main method when building your project.
     */
    public static void main(String[] args) throws Exception {
        // Run Spring Boot
        SpringApplication.run(App.class, args);
    }
}
