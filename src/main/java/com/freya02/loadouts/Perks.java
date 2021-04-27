package com.freya02.loadouts;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

public class Perks {
	private final List<Perk> firstPerks = new ArrayList<>();
	private final List<Perk> secondPerks = new ArrayList<>();
	private final List<Perk> thirdPerks = new ArrayList<>();

	public Perks() {
		for (CSVRecord record : Utils.loadCsv("Perks.csv")) {
			switch (record.get("tier")) {
				case "1" -> firstPerks.add(new Perk(1, record.get("name"), Boolean.parseBoolean(record.get("isOverkill"))));
				case "2" -> secondPerks.add(new Perk(2, record.get("name"), Boolean.parseBoolean(record.get("isOverkill"))));
				case "3" -> thirdPerks.add(new Perk(3, record.get("name"), Boolean.parseBoolean(record.get("isOverkill"))));
			}
		}
	}

	public List<Perk> getFirstPerks() {
		return firstPerks;
	}

	public List<Perk> getSecondPerks() {
		return secondPerks;
	}

	public List<Perk> getThirdPerks() {
		return thirdPerks;
	}
}
