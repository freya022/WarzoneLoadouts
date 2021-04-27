package com.freya02.loadouts;

public class Lethal {
	private final String name;

	public Lethal(String name) {
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
