package com.freya02.loadouts.ui;

import com.freya02.loadouts.Profile;
import com.freya02.loadouts.WarzoneLoadouts;
import com.freya02.loadouts.Weapon;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.stream.Collectors;

public class AddWeaponController extends JFXDialog {
	private final WarzoneLoadoutsController parent;
	private final Profile profile;

	@FXML private ChoiceBox<Weapon> weaponChoiceBox;

	public AddWeaponController(WarzoneLoadoutsController parent, Profile profile) {
		this.parent = parent;
		this.profile = profile;
	}

	public static Weapon askWeapon(WarzoneLoadoutsController parent, Profile profile) throws IOException {
		final FXMLLoader loader = new FXMLLoader(WarzoneLoadoutsController.class.getResource("AddWeapon.fxml"));
		final AddWeaponController controller = new AddWeaponController(parent, profile);
		loader.setController(controller);
		final Region root = loader.load();

		controller.setContent(root);

		return (Weapon) Platform.enterNestedEventLoop(controller);
	}

	@FXML private void initialize() {
		setDialogContainer((StackPane) parent.getWindow().getRoot());
		setOverlayClose(false);
		setTransitionType(DialogTransition.TOP);

		//Weapon is locked and unlocked weapons doesn't contain that weapon
		weaponChoiceBox.getItems().addAll(WarzoneLoadouts.getWeapons().stream().filter(w -> w.isLocked() && !profile.getUnlockedWeapons().contains(w.getId())).collect(Collectors.toList()));
		weaponChoiceBox.setValue(weaponChoiceBox.getItems().get(0));
		weaponChoiceBox.setConverter(new StringConverter<>() {
			@Override
			public String toString(Weapon object) {
				return object.getCategory() + " : " + object.getName();
			}

			@Override
			public Weapon fromString(String string) {
				throw new UnsupportedOperationException();
			}
		});

		show();
	}

	@FXML private void onBackClicked(MouseEvent event) {
		Platform.exitNestedEventLoop(this, null);

		close();
	}

	@FXML private void onOkClicked(MouseEvent event) {
		Platform.exitNestedEventLoop(this, weaponChoiceBox.getValue());

		close();
	}
}
