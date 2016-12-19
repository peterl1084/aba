package ch.abacus.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.vaadin.spring.security.annotation.EnableVaadinManagedSecurity;

@EnableVaadinManagedSecurity
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AbacusApplication {

	public static void main(String[] args) {
		System.setProperty("phantom.exec", "/Applications/phantomjs-2.1.1-macosx/bin/phantomjs");
		SpringApplication.run(AbacusApplication.class, args);
	}
}
