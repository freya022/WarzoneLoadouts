package com.freya02.loadouts;

import com.freya02.ui.ImageUtil;
import com.freya02.ui.UILib;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Profile {
	private final String name;
	private final Set<Integer> unlockedWeapons = new HashSet<>();
	private final Map<Integer, Integer> levelsMap = new HashMap<>();

	public Profile(String name, File pictureFile) throws IOException {
		this.name = name;
		ImageUtil.saveImage(ImageUtil.loadImage(pictureFile.toPath()), getPicturePath());
	}

	@NotNull
	private Path getPicturePath() {
		return WarzoneLoadouts.APP_FOLDER.resolve(name + ".png");
	}

	public Image getImage() {
		try {
			return new Image(getPicturePath().toUri().toURL().toString(), false);
		} catch (MalformedURLException e) {
			UILib.displayError(e);
			return new WritableImage(64, 64);
		}
	}

	public void unlockWeapon(Weapon weapon) {
		unlockedWeapons.add(weapon.getId());
	}

	public void lockWeapon(Weapon weapon) {
		unlockedWeapons.remove(weapon.getId());
	}

	public String getName() {
		return name;
	}

	public Set<Integer> getUnlockedWeapons() {
		return unlockedWeapons;
	}

	public Map<Integer, Integer> getLevelsMap() {
		return levelsMap;
	}
}
