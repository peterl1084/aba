package ch.abacus.application.view.customers;

import java.util.function.Predicate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.data.DataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ch.abacus.application.common.repository.customer.CustomerDTO;
import ch.abacus.application.common.repository.customer.FilterDefinition;
import ch.abacus.application.ui.data.DataEditor;
import ch.abacus.application.ui.data.DataTable;
import ch.abacus.application.view.MenuDefinition;

@SpringView(name = "customers")
@MenuDefinition(translationKey = "Customers", icon = FontAwesome.USERS, order = 1)
public class CustomerViewBean extends VerticalLayout implements View {
	private static final long serialVersionUID = 7351024221204217108L;

	@Autowired
	private DataTable<CustomerDTO> customerTable;

	@Autowired
	private DataEditor<CustomerDTO> customerEditor;

	private ComboBox<CustomerDTO> customerSelector;

	@Autowired
	private DataProvider<CustomerDTO, FilterDefinition> customerDataProvider;

	@PostConstruct
	protected void initialize() {
		setSizeFull();

		setMargin(true);
		addComponents(customerTable, customerEditor);
		setExpandRatio(customerTable, 1);
		customerTable.asSingleSelect().addValueChangeListener(e -> onItemSelected(e.getValue()));

		Button changeTheme = new Button("Change theme", e -> onChangeTheme());

		customerSelector = new ComboBox<>();
		customerSelector.setItemCaptionGenerator(c -> c.getFirstName() + " " + c.getLastName());
		customerSelector.setDataProvider(customerDataProvider.convertFilter(filterText -> {
			FilterDefinition filterDef = new FilterDefinition();
			filterDef.setFilterParam("firstName", filterText);
			return filterDef;
		}));

		addComponent(customerSelector);
	}

	private void onChangeTheme() {
		if (UI.getCurrent().getTheme().equals("abacus")) {
			UI.getCurrent().setTheme("abacus2");
		} else {
			UI.getCurrent().setTheme("abacus");
		}
	}

	private void onItemSelected(CustomerDTO value) {
		customerEditor.setDTOToEdit(value);
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}
}
