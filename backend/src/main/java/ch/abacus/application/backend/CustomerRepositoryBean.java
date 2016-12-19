package ch.abacus.application.backend;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.abacus.application.common.data.Money;
import ch.abacus.application.common.data.SortDefinition;
import ch.abacus.application.common.repository.customer.CustomerDTO;
import ch.abacus.application.common.repository.customer.CustomerRepository;
import ch.abacus.application.common.repository.customer.FilterDefinition;

@Service
public class CustomerRepositoryBean implements CustomerRepository {
	private Set<CustomerEntity> customers;

	@Autowired
	private DataGenerator dataGenerator;

	private BeanInfo beanInfo;

	private List<PropertyDescriptor> propertyDescriptors;

	public CustomerRepositoryBean() {
		customers = new HashSet<CustomerEntity>();
		try {
			beanInfo = Introspector.getBeanInfo(CustomerEntity.class);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
		propertyDescriptors = Arrays.asList(beanInfo.getPropertyDescriptors());
	}

	@PostConstruct
	protected void initialize() {
		customers.addAll(dataGenerator.generateTestData().stream().collect(Collectors.toList()));
	}

	@Override
	public List<CustomerDTO> getCustomers(SortDefinition sortDefinition, Optional<FilterDefinition> filter,
			int startIndex, int count) {
		return getSubList(sortDefinition, filter, startIndex, count);
	}

	@Override
	public List<CustomerDTO> getCustomers() {
		List<CustomerDTO> customerList = new LinkedList<>();
		int index = 0;
		for (int i = 0; i < 10; i++) {
			customerList.addAll(getSubList(null, Optional.empty(), index * 10, 100));
		}

		return customerList;
	}

	@Override
	public void storeCustomers(Collection<CustomerDTO> toStore) {
		Set<CustomerEntity> newDate = toStore.stream().map(dto -> new CustomerEntity(dto)).collect(Collectors.toSet());
		newDate.forEach(newItem -> {
			customers.remove(newItem);
			customers.add(newItem);
		});
	}

	@Override
	public int size(FilterDefinition filter) {
		int size = (int) getCustomerStream(Optional.ofNullable(filter)).count();
		System.out.println("Reporting " + size + " items for " + filter);
		return size;
	}

	protected Stream<CustomerEntity> getCustomerStream(Optional<FilterDefinition> filter) {
		Stream<CustomerEntity> stream = customers.stream();

		if (filter.isPresent()) {
			stream = stream.filter(c -> passesFilter(c, filter));
		}

		return stream;
	}

	private boolean passesFilter(CustomerEntity c, Optional<FilterDefinition> filter) {
		if (!filter.isPresent() || filter.isPresent() && filter.get().isEmpty()) {
			return true;
		}

		FilterDefinition filterDefinition = filter.get();

		for (String propertyName : filterDefinition.getFilteredProperties()) {
			if (filterDefinition.getFilterParam(propertyName).isPresent()) {
				PropertyDescriptor descriptor = findPropertyDefinition(propertyName);
				Method readMethod = descriptor.getReadMethod();

				if (readMethod != null) {
					Object propertyValue = null;
					try {
						propertyValue = readMethod.invoke(c);
					} catch (Exception e) {
						throw new RuntimeException("Error getting value from " + readMethod.getName());
					}
					String filterValue = filterDefinition.getFilterParam(propertyName).get().toString();
					if (propertyValue instanceof String) {
						if (!propertyValue.toString().toLowerCase().contains(filterValue.toLowerCase())) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private PropertyDescriptor findPropertyDefinition(String propertyName) {
		Optional<PropertyDescriptor> descriptorOptional = propertyDescriptors.stream()
				.filter(descriptor -> descriptor.getName().equals(propertyName)).findFirst();
		return descriptorOptional.orElseThrow(() -> new RuntimeException("Could not find property with name "
				+ propertyName + " from " + CustomerEntity.class.getCanonicalName()));
	}

	private List<CustomerDTO> getSubList(SortDefinition sortDefinition, Optional<FilterDefinition> filter, int i,
			int size) {
		List<CustomerDTO> customerDtos = new LinkedList<>();

		filter.ifPresent(System.out::println);
		List<CustomerEntity> customerEntities = null;
		Stream<CustomerEntity> stream = getCustomerStream(filter);
		if (sortDefinition != null) {
			stream = stream.sorted((a, b) -> CustomerRepositoryBean.sort(a, b, sortDefinition));
		}

		customerEntities = stream.collect(Collectors.toList());

		int toIndex = i + size;
		if (toIndex >= customerEntities.size()) {
			toIndex = customerEntities.size();
		}

		List<CustomerEntity> subList = customerEntities.subList(i, toIndex);

		for (CustomerEntity customer : subList) {
			customerDtos.add(buildCustomerDTO(customer));
		}

		return customerDtos;
	}

	private CustomerDTO buildCustomerDTO(CustomerEntity customer) {
		CustomerDTO dto = new CustomerDTO(customer.getIndex());
		dto.setFirstName(customer.getFirstName());
		dto.setLastName(customer.getLastName());
		dto.setPurchaseTotal(Money.of(customer.getCurrencyCode(), customer.getPurchasesTotal()));
		dto.setBirthdate(customer.getBirthDate());
		return dto;
	}

	private static int sort(CustomerEntity a, CustomerEntity b, SortDefinition sortDefinition) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(CustomerEntity.class);
			List<PropertyDescriptor> properties = Arrays.asList(beanInfo.getPropertyDescriptors());
			Optional<PropertyDescriptor> property = properties.stream()
					.filter(prop -> prop.getName().equals(sortDefinition.getSortProperty())).findFirst();

			if (property.isPresent()) {
				Method method = property.get().getReadMethod();
				if (Comparable.class.isAssignableFrom(method.getReturnType())) {
					Comparable comparableA = (Comparable) method.invoke(a);
					Comparable comparableB = (Comparable) method.invoke(b);

					if (sortDefinition.isAscending()) {
						return comparableA.compareTo(comparableB);
					} else {
						return comparableB.compareTo(comparableA);
					}

				}
			}

			return 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static class CustomerEntity {
		private int index;
		private String firstName;
		private String lastName;
		private LocalDate birthDate;
		private BigDecimal purchasesTotal;
		private String currencyCode;
		private static int customerIndex; // for testing purposes

		public CustomerEntity() {
			this.index = customerIndex++;
		}

		public CustomerEntity(CustomerDTO customerDTO) {
			this.index = customerDTO.getIndex();
			this.firstName = customerDTO.getFirstName();
			this.lastName = customerDTO.getLastName();
			this.birthDate = customerDTO.getBirthDate();
			this.purchasesTotal = customerDTO.getPurchaseTotal().getAmount();
			this.currencyCode = customerDTO.getPurchaseTotal().getCurrencyCode();
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public LocalDate getBirthDate() {
			return birthDate;
		}

		public void setBirthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
		}

		public BigDecimal getPurchasesTotal() {
			return purchasesTotal;
		}

		public void setPurchasesTotal(BigDecimal purchasesTotal) {
			this.purchasesTotal = purchasesTotal;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}

			if (other instanceof CustomerEntity) {
				return this.getIndex() == ((CustomerEntity) other).getIndex();
			}

			return false;
		}

		@Override
		public int hashCode() {
			return Integer.valueOf(index).hashCode();
		}

		public static int compareByIndex(CustomerEntity a, CustomerEntity b) {
			return a.getIndex() - b.getIndex();
		}

		public static int compareByFirstName(CustomerEntity a, CustomerEntity b) {
			return a.getFirstName().compareTo(b.getFirstName());
		}

		public static int compareByLastName(CustomerEntity a, CustomerEntity b) {
			return a.getLastName().compareTo(b.getLastName());
		}

		public static int compareByBirthDate(CustomerEntity a, CustomerEntity b) {
			return a.getBirthDate().compareTo(b.getBirthDate());
		}
	}
}
