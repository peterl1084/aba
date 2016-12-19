package ch.abacus.application.view.optimization;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import ch.abacus.application.view.MenuDefinition;

@SpringView(name = "contacts_slow")
// @MenuDefinition(translationKey = "Slow Contacts", icon = FontAwesome.USER,
// order = 4)
public class ContactView extends VerticalLayout implements View {
    private static final long serialVersionUID = 4003000255650171493L;

    public ContactView() {
        for (int i = 0; i < 100; i++) {
            ContactPanel contactPanel = new ContactPanel();
            addComponent(contactPanel);
        }
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }

    private class ContactPanel extends CustomComponent {

        public ContactPanel() {
            setWidth(100, Unit.PERCENTAGE);
            Panel personPanel = new Panel();
            personPanel.setCaption("John Doe");

            addComponent(personPanel);

            HorizontalLayout hl = new HorizontalLayout();
            hl.setMargin(true);
            personPanel.setContent(hl);
            hl.setWidth("100%");

            VerticalLayout contactDetails = new VerticalLayout();
            Label phone = new Label();
            phone.setValue("Phone: 123 4567");
            contactDetails.addComponent(phone);
            Label fax = new Label();
            fax.setValue("Fax: 123 4568");
            contactDetails.addComponent(fax);
            Label email = new Label();
            email.setValue("Email: example@example.com");
            contactDetails.addComponent(email);
            hl.addComponent(contactDetails);

            VerticalLayout addressLayout = new VerticalLayout();
            Label address = new Label();
            address.setValue("Random street 1");
            addressLayout.addComponent(address);
            Label city = new Label();
            city.setValue("City of Lorem Ipsum");
            addressLayout.addComponent(city);
            Label zip = new Label();
            zip.setValue("012345");
            addressLayout.addComponent(zip);
            hl.addComponent(addressLayout);

            setCompositionRoot(personPanel);
        }
    }
}
