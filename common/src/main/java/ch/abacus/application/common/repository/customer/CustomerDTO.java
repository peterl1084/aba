package ch.abacus.application.common.repository.customer;

import java.time.LocalDate;

import ch.abacus.application.common.data.ColumnDefinition;
import ch.abacus.application.common.data.FieldDefinition;
import ch.abacus.application.common.data.Money;

public class CustomerDTO {
	private int index;

	private String firstName;
	private String lastName;
	private Money purchaseTotal;
	private LocalDate birthDate;
	private int yearOfBirth;

	public CustomerDTO(int index) {
		this.index = index;
	}

	@ColumnDefinition(translationKey = "devday.customer.index", order = 1)
	public int getIndex() {
		return index;
	}

	@ColumnDefinition(translationKey = "devday.customer.firstName", order = 2, sortProperty = "firstName", filterable = true)
	@FieldDefinition(translationKey = "devday.customer.firstName", order = 1)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstname) {
		this.firstName = firstname;
	}

	@ColumnDefinition(translationKey = "devday.customer.lastName", order = 3, sortProperty = "lastName", filterable = true)
	@FieldDefinition(translationKey = "devday.customer.lastName", order = 2)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastname) {
		this.lastName = lastname;
	}

	@ColumnDefinition(translationKey = "devday.customer.purchaseTotal", order = 4, sortProperty = "purchasesTotal")
	@FieldDefinition(translationKey = "devday.customer.purchases", order = 3)
	public Money getPurchaseTotal() {
		return purchaseTotal;
	}

	public void setPurchaseTotal(Money purchaseTotal) {
		this.purchaseTotal = purchaseTotal;
	}

	public void setBirthdate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other instanceof CustomerDTO) {
			return this.getIndex() == ((CustomerDTO) other).getIndex();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(index).hashCode();
	}

	public int getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(int yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}
}
