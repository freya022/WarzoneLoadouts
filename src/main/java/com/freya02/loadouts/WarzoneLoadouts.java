package com.freya02.loadouts;

import com.freya02.gson.GsonUtils;
import com.freya02.io.IOOperation;
import com.freya02.loadouts.ui.LoadingController;
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

public class WarzoneLoadouts extends IOOperation {
	public static final Path APP_FOLDER = Path.of(System.getenv("appdata"), "WarzoneLoadouts");

	public static final Path WEAPONS_PATH = APP_FOLDER.resolve("Weapons.json");
	public static final Path PROFILES_PATH = APP_FOLDER.resolve("Profiles.json");

	private static WarzoneLoadouts instance;

	private final Profiles profiles;

	private final Weapons weapons;
	private final Perks perks = new Perks();
	private final Tacticals tacticals = new Tacticals();
	private final Lethals lethals = new Lethals();

	private WarzoneLoadouts() throws Exception {
		final LoadingController controller = UILib.runAndWait(() -> new LoadingController(WarzoneLoadouts.this));

		setState("Chargement...");
		Files.createDirectories(APP_FOLDER);

		if (Files.notExists(WEAPONS_PATH)) {
			downloadWeapons();
		}

		weapons = loadWeapons(1);

		setState("Chargement des profils...");
		profiles = GsonUtils.loadGson(PROFILES_PATH, Profiles.class);

		UILib.runAndWait(() -> controller.getWindow().close());
	}

	public static WarzoneLoadouts getInstance() {
		if (instance == null) {
			loadAll();
		}

		return instance;
	}

	public static void loadAll() {
		try {
			instance = new WarzoneLoadouts();
		} catch (Exception e) {
			UILib.displayError(e);
			System.exit(-7);
		}
	}

	private Weapons loadWeapons(int n) throws Exception {
		setState("Chargement des armes...");
		try (final BufferedReader br = Files.newBufferedReader(WEAPONS_PATH)) {
			return new Gson().fromJson(br, Weapons.class);
		} catch (Exception e) {
			if (n == 2) {
				throw e;
			}

			downloadWeapons();
			return loadWeapons(n + 1);
		}
	}

	private void downloadWeapons() throws IOException {
		setState("Téléchargement des armes...");
		try (InputStream stream = new URL("https://raw.githubusercontent.com/freya022/WarzoneLoadouts/master/data/Weapons.json").openStream()) {
			Files.write(WEAPONS_PATH, stream.readAllBytes(), CREATE, TRUNCATE_EXISTING);
		}
	}

	public static Profiles getProfiles() {
		return getInstance().profiles;
	}

	public static List<Weapon> getWeapons() {
		return getInstance().weapons.getWeapons();
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
		return getInstance().perks;
	}

	public static List<Tactical> getTacticals() {
		return getInstance().tacticals.getTacticals();
	}

	public static List<Lethal> getLethals() {
		return getInstance().lethals.getLethals();
	}
}
