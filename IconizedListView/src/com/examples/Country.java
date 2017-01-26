package com.examples;

public class Country {
	public String name;
	public String abbreviation;
	public String region;
	public String resourceId;

	public Country() {
		// TODO Auto-generated constructor stub
	}

	public Country(String name, String abbreviation, String region,
			String resourceFilePath) {
		this.name = name;
		this.abbreviation = abbreviation;
		this.region = region;
		this.resourceId = resourceFilePath;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
