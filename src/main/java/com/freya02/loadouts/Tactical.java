package com.freya02.loadouts;

public class Tactical {
	private final String name;

	public Tactical(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
