package ch.abacus.application.view.customers;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.data.AbstractDataProvider;
import com.vaadin.server.data.Query;
import com.vaadin.server.data.SortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringComponent;

import ch.abacus.application.common.data.SortDefinition;
import ch.abacus.application.common.repository.customer.CustomerDTO;
import ch.abacus.application.common.repository.customer.FilterDefinition;
import ch.abacus.application.common.repository.customer.CustomerRepository;
import ch.abacus.application.ui.data.DataPersister;

@SpringComponent
class CustomerDataProviderBean extends AbstractDataProvider<CustomerDTO, FilterDefinition>
		implements DataPersister<CustomerDTO> {
	private static final long serialVersionUID = -7622201409760555548L;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public boolean isInMemory() {
		return false;
	}

	@Override
	public int size(Query<CustomerDTO, FilterDefinition> query) {
		return customerRepository.size(query.getFilter().orElse(null));
	}

	@Override
	public Stream<CustomerDTO> fetch(Query<CustomerDTO, FilterDefinition> query) {
		System.out.println("Requesting [" + query.getOffset() + " to " + (query.getOffset() + query.getLimit())
				+ " with filter " + query.getFilter().orElse(null) + " with sort column "
				+ query.getSortOrders().stream().map(order -> order.getSorted() + " " + order.getDirection().name())
						.collect(Collectors.joining(",")));
		SortDefinition sortDef = null;

		if (query.getSortOrders() != null && !query.getSortOrders().isEmpty()) {
			SortOrder<String> sortOrder = query.getSortOrders().iterator().next();
			sortDef = new SortDefinition(sortOrder.getSorted(), SortDirection.ASCENDING == sortOrder.getDirection());

		}

		return customerRepository.getCustomers(sortDef, query.getFilter(), query.getOffset(), query.getLimit())
				.stream();
	}

	@Override
	public void store(Collection<CustomerDTO> dtos) {
		customerRepository.storeCustomers(dtos);
	}
}
