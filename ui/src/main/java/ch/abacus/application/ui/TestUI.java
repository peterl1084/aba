package ch.abacus.application.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI(path = "test")
@Widgetset("ch.abacus.application.client.AbacusWidgetset")
@Theme("abacus2")
public class TestUI extends UI {

	@Override
	protected void init(VaadinRequest request) {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		
		layout.addComponent(new Label("Test UI"));
		setContent(layout);
	}

}
