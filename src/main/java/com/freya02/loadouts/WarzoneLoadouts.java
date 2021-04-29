package com.freya02.loadouts;

import com.freya02.gson.GsonUtils;
import com.freya02.io.NativeUtils;
import com.freya02.ui.UILib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class WarzoneLoadouts {
	public static final Path APP_FOLDER = Path.of(System.getenv("appdata"), "WarzoneLoadouts");

	public static final Path WEAPONS_PATH = APP_FOLDER.resolve("Weapons.json");
	public static final Path PROFILES_PATH = APP_FOLDER.resolve("Profiles.json");

	private static final Profiles profiles;

	private static final Weapons weapons;
	private static final Perks perks = new Perks();
	private static final Tacticals tacticals = new Tacticals();
	private static final Lethals lethals = new Lethals();

	static {
		try {
			Files.createDirectories(APP_FOLDER);

			final byte[] bytes;
			try (InputStream stream = WarzoneLoadouts.class.getResourceAsStream("Weapons.json")) {
				bytes = stream.readAllBytes();
			}

			if (Files.notExists(WEAPONS_PATH) || !Arrays.equals(NativeUtils.getFileHashBytes(WEAPONS_PATH.toString()), NativeUtils.getBytesHashBytes(bytes))) {
				Files.write(WEAPONS_PATH, bytes, CREATE, TRUNCATE_EXISTING);
			}
			weapons = GsonUtils.loadGson(WEAPONS_PATH, Weapons.class);
			profiles = GsonUtils.loadGson(PROFILES_PATH, Profiles.class);
		} catch (IOException e) {
			UILib.displayError(e);
			System.exit(-2);
			throw new AssertionError();
		}
	}

	public static Profiles getProfiles() {
		return profiles;
	}

	public static List<Weapon> getWeapons() {
		return weapons.getWeapons();
	}

	public static Weapon getWeaponById(int weaponId) {
		for (Weapon weapon : getWeapons()) {
			if (weapon.getId() == weaponId) {
				return weapon;
			}
		}

		return null;
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
