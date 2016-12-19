package ch.abacus.application.view.customers;

import java.time.LocalDate;

import ch.abacus.application.common.data.Money;

public class UserDTO {

	private String zipCode;
	private String city;

	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private Money salary;

	private long purchases;

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

	public long getPurchases() {
		return purchases;
	}

	public void setPurchases(long purchases) {
		this.purchases = purchases;
	}

	public Money getSalary() {
		return salary;
	}

	public void setSalary(Money salary) {
		this.salary = salary;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
