package com.freya02.loadouts;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Game {
	COLD_WAR,
	MODERN_WARFARE;

	@Override
	public String toString() {
		return Arrays.stream(name().split("_")).map(s -> Character.toUpperCase(s.charAt(0)) + s.toLowerCase().substring(1)).collect(Collectors.joining(" "));
	}
}
