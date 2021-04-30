package com.freya02.loadouts;

import com.freya02.loadouts.ui.ProfilesController;
import com.freya02.loadouts.ui.WarzoneLoadoutsController;
import com.freya02.ui.UILib;

public class Main {
	public static void main(String[] args) {
		WarzoneLoadouts.loadAll();

		UILib.runAndWait(() -> {
			final Profile profile = ProfilesController.selectProfile();

			WarzoneLoadoutsController.withProfile(profile);
		});
	}
}
