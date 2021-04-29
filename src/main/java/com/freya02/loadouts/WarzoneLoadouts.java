package com.freya02.loadouts;

import com.freya02.gson.GsonUtils;
import com.freya02.ui.UILib;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class WarzoneLoadouts {
	public static final Path APP_FOLDER = Path.of(System.getenv("appdata"), "WarzoneLoadouts");

	public static final Path WEAPONS_PATH = APP_FOLDER.resolve("Weapons.json");
	public static final Path PROFILES_PATH = APP_FOLDER.resolve("Profiles.json");

	private static final Profiles profiles;

	private static Weapons weapons;
	private static final Perks perks = new Perks();
	private static final Tacticals tacticals = new Tacticals();
	private static final Lethals lethals = new Lethals();

	static {
		try {
			Files.createDirectories(APP_FOLDER);

			if (Files.notExists(WEAPONS_PATH)) {
				downloadWeapons();
			}

			loadWeapons(1);

			profiles = GsonUtils.loadGson(PROFILES_PATH, Profiles.class);
		} catch (IOException e) {
			UILib.displayError(e);
			System.exit(-2);
			throw new AssertionError();
		}
	}

	private static void loadWeapons(int n) throws IOException {
		try (final BufferedReader br = Files.newBufferedReader(WEAPONS_PATH)) {
			weapons = new Gson().fromJson(br, Weapons.class);
		} catch (Exception e) {
			if (n == 2) {
				UILib.displayError(e);
				System.exit(-9);
				throw new AssertionError();
			}

			downloadWeapons();
			loadWeapons(n + 1);
		}
	}

	private static void downloadWeapons() throws IOException {
		try (InputStream stream = new URL("https://raw.githubusercontent.com/freya022/WarzoneLoadouts/master/data/Weapons.json").openStream()) {
			Files.write(WEAPONS_PATH, stream.readAllBytes(), CREATE, TRUNCATE_EXISTING);
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
