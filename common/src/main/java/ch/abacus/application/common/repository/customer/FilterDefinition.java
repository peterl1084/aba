package ch.abacus.application.common.repository.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FilterDefinition {
	private Map<String, Object> filterParams;

	public FilterDefinition() {
		filterParams = new HashMap<>();
	}

	public void setFilterParam(String propertyName, Object value) {
		filterParams.put(propertyName, value);
	}

	public Optional<Object> getFilterParam(String propertyName) {
		return Optional.ofNullable(filterParams.get(propertyName));
	}

	public List<String> getFilteredProperties() {
		return new ArrayList<>(filterParams.keySet());
	}

	public void removeFilterParam(String propertyName) {
		filterParams.remove(propertyName);
	}

	public boolean isEmpty() {
		return filterParams.isEmpty();
	}

	@Override
	public String toString() {
		return getFilteredProperties().stream().map(propertyName -> propertyName + ":" + filterParams.get(propertyName))
				.collect(Collectors.joining(","));
	}
}
