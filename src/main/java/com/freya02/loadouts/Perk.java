package com.freya02.loadouts;

public class Perk {
	private final int tier;
	private final String name;
	private final boolean isOverkill;

	public Perk(int tier, String name, boolean isOverkill) {
		this.tier = tier;
		this.name = name;
		this.isOverkill = isOverkill;
	}

	public int getTier() {
		return tier;
	}

	public String getName() {
		return name;
	}

	public boolean isOverkill() {
		return isOverkill;
	}

	@Override
	public String toString() {
		return name;
	}
}
