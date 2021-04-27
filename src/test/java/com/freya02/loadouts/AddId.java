package com.freya02.loadouts;

import com.freya02.gson.GsonUtils;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.nio.file.Path;

public class AddId {
	public static void main(String[] args) throws IOException {
		final Weapons weapons = GsonUtils.loadGson(Path.of("Weapons.json"), Weapons.class);
		for (CSVRecord strings : Utils.loadCsv("Primaries.csv")) {
			treatRecord(weapons, true, strings);
		}

		for (CSVRecord strings : Utils.loadCsv("Secondaries.csv")) {
			treatRecord(weapons, false, strings);
		}

		GsonUtils.saveGson(Path.of("Weapons2.json"), weapons);
	}

	private static void treatRecord(Weapons weapons, boolean primary, CSVRecord strings) {
		for (Weapon weapon : weapons.getWeapons()) {
			if (strings.get("name").equals(weapon.getName())) {
				weapon.setId((int) ((primary ? 0 : 1000) + strings.getRecordNumber()));

				break;
			}
		}
	}
}
