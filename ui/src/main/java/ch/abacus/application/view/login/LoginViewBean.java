package ch.abacus.application.view.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.security.VaadinSecurity;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = "login")
public class LoginViewBean extends VerticalLayout implements View {
	private static final long serialVersionUID = -8230556303683711279L;

	private TextField username;
	private PasswordField password;

	@Autowired
	private VaadinSecurity security;

	public LoginViewBean() {
		setSizeFull();

		VerticalLayout content = new VerticalLayout();
		content.setWidth(300, Unit.PIXELS);
		content.setMargin(true);
		content.setSpacing(true);
		content.addStyleName(ValoTheme.LAYOUT_CARD);
		content.setCaption("Vaadin 8 App - Login");

		username = new TextField("Username");
		username.setWidth(100, Unit.PERCENTAGE);
		username.setIcon(VaadinIcons.USER);
		username.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		password = new PasswordField("Password");
		password.setWidth(100, Unit.PERCENTAGE);
		password.setIcon(VaadinIcons.LOCK);
		password.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		Button login = new Button("Login", e -> onLoginClicked());
		login.addStyleName(ValoTheme.BUTTON_PRIMARY);
		login.setWidth(100, Unit.PERCENTAGE);
		// login.setDisableOnClick(true);

		content.addComponents(username, password, login);

		addComponent(content);
		setComponentAlignment(content, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		username.clear();
		password.clear();
	}

	private void onLoginClicked() {
		try {
			Thread.sleep(10000);
			security.login(username.getValue(), password.getValue());
		} catch (AuthenticationException e) {
			Notification.show("Invalid credentials", Type.TRAY_NOTIFICATION);
		} catch (Exception e) {
			Notification.show("System error", Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
