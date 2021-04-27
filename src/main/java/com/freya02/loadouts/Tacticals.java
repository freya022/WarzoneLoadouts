package com.freya02.loadouts;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

public class Tacticals {
	private final List<Tactical> tacticals = new ArrayList<>();

	public Tacticals() {
		for (CSVRecord record : Utils.loadCsv("Tactical.csv")) {
			tacticals.add(new Tactical(record.get("name")));
		}
	}

	public List<Tactical> getTacticals() {
		return tacticals;
	}
}
