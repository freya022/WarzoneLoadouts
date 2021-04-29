package com.freya02.loadouts.ui;

import com.freya02.loadouts.Profile;
import com.freya02.loadouts.WarzoneLoadouts;
import com.freya02.loadouts.Weapon;
import com.freya02.ui.FXUtils;
import com.freya02.ui.UILib;
import com.freya02.ui.window.CloseHandler;
import com.freya02.ui.window.LazyWindow;
import com.freya02.ui.window.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.io.InputStream;

public class WarzoneLoadoutsController extends LazyWindow {
	private final Profile profile;

	@FXML private Label profileLabel;
	@FXML private ImageView profileView;
	@FXML private VBox unlockedBox;

	public WarzoneLoadoutsController(Profile profile) {
		this.profile = profile;
	}

	public static void withProfile(Profile profile) {
		try {
			new WindowBuilder("WarzoneLoadouts.fxml", "Warzone Loadouts")
					.onClose(CloseHandler.SYSTEM_EXIT)
					.resizable()
					.create(new WarzoneLoadoutsController(profile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void onInitialized() {
		profileLabel.setText(profile.getName());
		profileView.setImage(profile.getImage());

		for (Integer unlockedWeaponId : profile.getUnlockedWeapons()) {
			Weapon weapon = WarzoneLoadouts.getWeaponById(unlockedWeaponId);

			if (weapon == null) {
				System.out.println("Cannot resolve weapon with ID " + unlockedWeaponId);
				return;
			}

			showUnlockedWeapon(weapon);
		}
	}

	private void showUnlockedWeapon(Weapon weapon) {
		try (InputStream stream = WarzoneLoadoutsController.class.getResourceAsStream("delete.svg")) {
			final Label label = new Label(weapon.getCategory() + " - " + weapon.getName());
			final Pane pane = new Pane();
			HBox.setHgrow(pane, Priority.ALWAYS);

			final StackPane stackPane = new StackPane(FXUtils.getSvg(stream));
			stackPane.setOnMouseClicked(e -> {
				profile.lockWeapon(weapon);
				WarzoneLoadouts.getProfiles().save();
			});

			final HBox box = new HBox(label, pane, stackPane);
			box.getStyleClass().add("unlockedWeapon");

			unlockedBox.getChildren().add(box);
		} catch (IOException e) {
			UILib.displayError(e);
		}
	}

	@FXML private void onAddWeaponClicked(MouseEvent event) {
		try {
			final Weapon weapon = AddWeaponController.askWeapon(this, profile);
			if (weapon != null) {
				profile.unlockWeapon(weapon);
				WarzoneLoadouts.getProfiles().save();

				showUnlockedWeapon(weapon);
			}
		} catch (IOException e) {
			UILib.displayError(e);
		}
	}

	@FXML private void onNewLoadoutClicked(MouseEvent event) {

	}
}
