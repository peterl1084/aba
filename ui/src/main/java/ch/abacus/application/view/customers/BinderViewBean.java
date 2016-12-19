package ch.abacus.application.view.customers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.BeanBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.ValidationStatus;
import com.vaadin.data.ValidationStatusHandler;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import ch.abacus.application.common.data.Money;
import ch.abacus.application.common.process.LongRunningProcess;
import ch.abacus.application.ui.component.MoneyField;
import ch.abacus.application.view.MenuDefinition;

@SpringView(name = "binder")
@MenuDefinition(translationKey = "Binder", icon = FontAwesome.WRENCH, order = 3)
public class BinderViewBean extends FormLayout implements View {
	private static final long serialVersionUID = 4996029286637113892L;

	private BeanBinder<UserDTO> binder;

	private UserDTO user;

	private TextField firstNameField;
	private TextField lastNameField;
	private TextField purchasesField;
	private DateField birthDateField;
	private MoneyField salary;

	private TextField zipCodeTextField;
	private TextField cityTextField;

	private Label actionResult;

	private Executor executor;

	@Autowired
	private LongRunningProcess processExecutor;

	private Map<String, String> codesByCity = new HashMap<>();

	ValidationStatusHandler zipCodeHandler = new ValidationStatusHandler() {

		@Override
		public void accept(ValidationStatus<?> status) {
			zipCodeTextField.setComponentError(status.isError() ? new UserError(status.getMessage().orElse("")) : null);
			if (!status.isError()) {
				String cityName = codesByCity.get(zipCodeTextField.getValue());
				if (cityName != null) {
					cityTextField.setValue(cityName);
				}
			}
		}
	};

	ValidationStatusHandler cityHandler = new ValidationStatusHandler() {

		@Override
		public void accept(ValidationStatus<?> status) {
			cityTextField.setComponentError(status.isError() ? new UserError(status.getMessage().orElse("")) : null);
			if (!status.isError()) {
				Optional<Entry<String, String>> findFirst = codesByCity.entrySet().stream()
						.filter(entry -> entry.getValue().equals(cityTextField.getValue())).findFirst();

				findFirst.ifPresent(entry -> zipCodeTextField.setValue(entry.getKey()));
			}
		}
	};

	public BinderViewBean() {
		setMargin(true);
		setSpacing(true);

		executor = Executors.newSingleThreadExecutor();

		codesByCity.put("1000", "St Gallen");
		codesByCity.put("2000", "Zürich");
		codesByCity.put("3000", "Turku");

		zipCodeTextField = new TextField("Zip");
		cityTextField = new TextField("City");

		binder = new BeanBinder<UserDTO>(UserDTO.class);

		addComponents(zipCodeTextField, cityTextField);

		binder.forField(zipCodeTextField).withValidator(zip -> codesByCity.containsKey(zip), "Unknown zip code")
				.withValidationStatusHandler(zipCodeHandler).bind(UserDTO::getZipCode, UserDTO::setZipCode);

		binder.forField(cityTextField).withValidator(city -> codesByCity.containsValue(city), "Invalid city")
				.withValidationStatusHandler(cityHandler).bind("city");

//		binder.setValidationStatusHandler(e -> binderValidationStatusChange(e));

		binder.setBean(getProduct());
	}

	private void binderValidationStatusChange(BinderValidationStatus<UserDTO> e) {
		e.getFieldValidationStatuses().forEach(status -> {
			Component field = (Component) status.getField();
			System.out
					.println(field.getCaption() + " error: " + status.isError() + ", ok: " + status.getStatus().name());
		});
	}

	private UserDTO getProduct() {
		UserDTO p = new UserDTO();
		p.setZipCode("1000");
		p.setFirstName("Peter");
		p.setLastName("Lehto");
		p.setBirthDate(LocalDate.of(1984, 10, 31));
		p.setSalary(Money.of("EUR", BigDecimal.valueOf(3000)));
		return p;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	private void onActionPerformed() {
		actionResult.setValue("Process running");
		// UI.getCurrent().setPollInterval(500);
		// pollingListener = UI.getCurrent().addPollListener(e -> onPoll());

		executor.execute(() -> {
			while (processExecutor.isProcessRunning()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}

			getUI().access(() -> {
				actionResult.setValue(processExecutor.getResult());
				// UI.getCurrent().setPollInterval(-1);
			});
		});
		processExecutor.performLongRunningProcess();
	}
}
