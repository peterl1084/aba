package ch.abacus.application.ui.data;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
public class GenericBeanResolver {

    @Autowired
    private ApplicationContext context;

    private static GenericBeanResolver instance;

    @PostConstruct
    protected void initialize() {
        instance = this;
    }

    private ApplicationContext getContext() {
        return context;
    }

    public static <T> Optional<T> resolveBean(Class<T> beanType, Class<?>... genericTypes) {
        ResolvableType type = ResolvableType.forClassWithGenerics(beanType, genericTypes);
        List<String> converterBeans = Arrays.asList(instance.getContext().getBeanNamesForType(type));
        if (converterBeans.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(instance.getContext().getBean(converterBeans.iterator().next(), beanType));
        } catch (NoSuchBeanDefinitionException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
