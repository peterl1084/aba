package ch.abacus.application.ui.menu;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.vaadin.spring.security.VaadinSecurity;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;

import ch.abacus.application.view.MenuDefinition;

@UIScope
@SpringComponent
public class TopMenuBean extends HorizontalLayout implements TopMenu {
    private static final long serialVersionUID = 2345810969051915298L;

    private MenuBar menuBar;

    @Autowired
    @Lazy
    private Navigator navigator;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private VaadinSecurity security;

    public TopMenuBean() {
        setWidth(100, Unit.PERCENTAGE);

        menuBar = new MenuBar();
        menuBar.setWidth(100, Unit.PERCENTAGE);

        addComponent(menuBar);

        setVisible(false);
    }

    @PostConstruct
    protected void initialize() {
        if (security.isAuthenticated()) {
            findAndPopulateMenuItems();
            setVisible(true);
        }
    }

    @Override
    public void addMenuItem(String caption, Resource icon, String viewName) {
        menuBar.addItem(caption, icon, e -> navigator.navigateTo(viewName));
    }

    private void findAndPopulateMenuItems() {
        List<String> beanNames = Arrays.asList(context.getBeanNamesForAnnotation(MenuDefinition.class));

        Map<String, MenuDefinition> definitionsToNames = beanNames.stream().collect(Collectors
                .toMap(Function.identity(), beanName -> context.findAnnotationOnBean(beanName, MenuDefinition.class)));
        Map<String, SpringView> viewsToNames = beanNames.stream().collect(Collectors.toMap(Function.identity(),
                beanName -> context.findAnnotationOnBean(beanName, SpringView.class)));

        beanNames.forEach(beanName -> {
            MenuDefinition menuDefinition = definitionsToNames.get(beanName);
            SpringView viewDefinition = viewsToNames.get(beanName);

            addMenuItem(menuDefinition.translationKey(), menuDefinition.icon(), viewDefinition.name());
        });
    }

    @EventListener
    protected void onLoginEvent(AuthenticationSuccessEvent event) {
        findAndPopulateMenuItems();
        setVisible(true);
    }
}
