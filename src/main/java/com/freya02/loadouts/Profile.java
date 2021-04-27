package com.freya02.loadouts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Profile {
	private final String name;
	private final Set<Integer> unlockedWeapons = new HashSet<>();
	private final Map<Integer, Integer> levelsMap = new HashMap<>();

	public Profile(String name) {
		this.name = name;
	}

	public void unlockWeapon(Weapon weapon) {
		unlockedWeapons.add(weapon.getId());
	}

	public void lockWeapon(Weapon weapon) {
		unlockedWeapons.remove(weapon.getId());
	}

	public String getName() {
		return name;
	}

	public Set<Integer> getUnlockedWeapons() {
		return unlockedWeapons;
	}

	public Map<Integer, Integer> getLevelsMap() {
		return levelsMap;
	}
}
