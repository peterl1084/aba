package ch.abacus.application.common.data;

public class SortDefinition {
	private final String sortProperty;
	private final boolean isAscending;

	public SortDefinition(String sortProperty, boolean isAscending) {
		this.sortProperty = sortProperty;
		this.isAscending = isAscending;
	}

	public String getSortProperty() {
		return sortProperty;
	}

	public boolean isAscending() {
		return isAscending;
	}
}
