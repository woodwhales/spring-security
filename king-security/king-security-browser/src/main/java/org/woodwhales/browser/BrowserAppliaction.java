package org.woodwhales.browser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.woodwhales")
public class BrowserAppliaction {

	public static void main(String[] args) {
        SpringApplication.run(BrowserAppliaction.class, args);
    }
}
