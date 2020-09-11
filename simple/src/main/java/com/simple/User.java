package com.simple;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class User extends AbstractFactoryBean<Object> {

	private String name;
	private String address;

	public User() {
	}

	public String getName() {
		return name;
	}
	@Required
	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", address='" + address + '\'' +
				'}';
	}

	@Override
	public Class<?> getObjectType() {
		return User.class;
	}

	@Override
	protected Object createInstance() throws Exception {
		return this;
	}
}
