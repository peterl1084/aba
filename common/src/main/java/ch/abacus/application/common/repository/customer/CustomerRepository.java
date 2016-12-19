package ch.abacus.application.common.repository.customer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import ch.abacus.application.common.data.SortDefinition;

/**
 * Customer repository simulates a customer repository that for our purposes
 * keeps all data in memory.
 * 
 * @author Peter / Vaadin
 */
public interface CustomerRepository {

	/**
	 * @param filterParam
	 * @param startIndex
	 * @param count
	 * @return List of customers consisting of a segment from startIndex to
	 *         startIndex + count. If startIndex + count > getNumberOfCustomers
	 *         the returned list will not be larger than getNumberOfCustomers -
	 *         startIndex
	 */
	List<CustomerDTO> getCustomers(SortDefinition sort, Optional<FilterDefinition> filterParam, int startIndex,
			int count);

	List<CustomerDTO> getCustomers();

	/**
	 * Stores given customer(s)
	 * 
	 * @param customers
	 */
	default void storeCustomers(CustomerDTO... customers) {
		if (customers == null) {
			return;
		}

		storeCustomers(Arrays.asList(customers));
	}

	/**
	 * Stores given collection of customers
	 * 
	 * @param customers
	 */
	void storeCustomers(Collection<CustomerDTO> customers);

	public int size(FilterDefinition filter);
}
