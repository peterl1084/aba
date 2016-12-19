package ch.abacus.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.i18n.MessageProvider;
import org.vaadin.spring.i18n.ResourceBundleMessageProvider;

@Configuration
public class LiteralsConfiguration {

	@Bean
	public MessageProvider literalsMessageProvider() {
		return new ResourceBundleMessageProvider("ch.abacus.application.literals", "UTF-8");
	}
}
