package ch.abacus.application.view.optimization;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ch.abacus.application.view.MenuDefinition;

@SpringView(name = "contacts_optimized")
// @MenuDefinition(translationKey = "Optimized Contacts", icon =
// FontAwesome.USER, order = 4)
public class ContactViewOptimized extends VerticalLayout implements View {
    private static final long serialVersionUID = 4003000255650171493L;

    public ContactViewOptimized() {
        for (int i = 0; i < 100; i++) {
            CssLayout personPanel = new CssLayout();
            personPanel.addStyleName(ValoTheme.LAYOUT_CARD);
            personPanel.addStyleName("person-panel");
            personPanel.setWidth(100, Unit.PERCENTAGE);
            personPanel.setCaption("John Doe");
            addComponent(personPanel);

            Label name = new Label();
            name.setWidth(50, Unit.PERCENTAGE);
            name.setContentMode(ContentMode.HTML);
            name.setValue("Phone: 123 4567</br>Fax: 123 4568</br>Email: example@example.com");

            Label address = new Label();
            address.setWidth(50, Unit.PERCENTAGE);
            address.setContentMode(ContentMode.HTML);
            address.setValue("Random street 1</br>City of Lorem Ipsum</br>012345");

            personPanel.addComponent(name);
            personPanel.addComponent(address);
        }
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }
}
