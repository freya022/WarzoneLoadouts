package com.freya02.loadouts;

import com.freya02.gson.GsonUtils;
import com.freya02.ui.UILib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Profiles {
	private final List<Profile> profiles = new ArrayList<>();

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void save() {
		try {
			GsonUtils.saveGson(WarzoneLoadouts.PROFILES_PATH, this);
		} catch (IOException e) {
			UILib.displayError(e);
		}
	}
}
