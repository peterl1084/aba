package ch.abacus.application.ui.data;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;

import com.vaadin.spring.annotation.ViewScope;

@Configuration
public class DataConfiguration {

    @Bean
    @ViewScope
    public <DTO> DataTable<DTO> provideDataTable(DependencyDescriptor injectionPoint) {
        return new DataTable<DTO>(resolveGenericType(injectionPoint));
    }

    @Bean
    @ViewScope
    public <DTO> DataEditor<DTO> provideDataEditor(DependencyDescriptor injectionPoint) {
        return new DataEditor<DTO>(resolveGenericType(injectionPoint));
    }

    @SuppressWarnings("unchecked")
    private <DTO> Class<DTO> resolveGenericType(DependencyDescriptor injectionPoint) {
        ResolvableType resolvableType = injectionPoint.getResolvableType();
        if (resolvableType.getGenerics().length == 1) {
            return (Class<DTO>) resolvableType.getGeneric(0).resolve();
        }

        throw new RuntimeException("Failed to resolve generic type for " + injectionPoint.getDependencyName());
    }
}
