package com.freya02.loadouts;

import com.freya02.gson.GsonUtils;
import com.freya02.ui.UILib;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class WarzoneLoadouts {
	private static final Weapons weapons;
	private static final Perks perks = new Perks();
	private static final Tacticals tacticals = new Tacticals();
	private static final Lethals lethals = new Lethals();

	static {
		try {
			weapons = GsonUtils.loadGson(Path.of("Weapons.json"), Weapons.class);
		} catch (IOException e) {
			UILib.displayError(e);
			System.exit(-2);
			throw new AssertionError();
		}
	}

	public static List<Weapon> getWeapons() {
		return weapons.getWeapons();
	}

	public static Perks getPerks() {
		return perks;
	}

	public static List<Tactical> getTacticals() {
		return tacticals.getTacticals();
	}

	public static List<Lethal> getLethals() {
		return lethals.getLethals();
	}
}
