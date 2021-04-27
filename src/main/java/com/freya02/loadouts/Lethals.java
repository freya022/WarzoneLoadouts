package com.freya02.loadouts;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

public class Lethals {
	private final List<Lethal> lethals = new ArrayList<>();

	public Lethals() {
		for (CSVRecord record : Utils.loadCsv("Lethal.csv")) {
			lethals.add(new Lethal(record.get("name")));
		}
	}

	public List<Lethal> getLethals() {
		return lethals;
	}
}
