package ch.abacus.application.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.security.VaadinSecurity;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import ch.abacus.application.ui.data.EditorSaveEvent;

@SpringUI
@Widgetset("ch.abacus.application.client.AbacusWidgetset")
@Theme("abacus")
@Push(PushMode.AUTOMATIC)
public class AbacusUI extends UI {
	private static final long serialVersionUID = -5202569828633859753L;

	@Autowired
	private DevDayViewDisplay viewDisplay;

	@Autowired
	private VaadinSecurity security;

	@Autowired
	private Navigator navigator;

	@Autowired
	private EventBus.UIEventBus eventBus;

	@Override
	protected void init(VaadinRequest request) {
		eventBus.subscribe(this);
		setContent(viewDisplay);

		if (!security.isAuthenticated()) {
			navigator.navigateTo("login");
		} else {
			navigator.navigateTo("customers");
		}
	}

	@EventBusListenerMethod
	protected void saveEvent(EditorSaveEvent e) {
		Notification.show("Saved!");
	}

	@EventListener
	protected void onLoginSucceeded(AuthenticationSuccessEvent event) {
		navigator.navigateTo("customers");
	}
}
