package com.freya02.loadouts.ui;

import com.freya02.loadouts.*;
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
import java.util.List;

public class WarzoneLoadoutsController extends LazyWindow {
	private final Profile profile;

	@FXML private Label profileLabel, primaryLabel, secondaryLabel, perk1Label, perk2Label, perk3Label, lethalLabel, tacticalLabel;
	@FXML private ImageView profileView;
	@FXML private VBox unlockedBox;
	@FXML private GridPane primaryGrid, secondaryGrid;

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
		getWindow().getStage().setMinWidth(1000);
		getWindow().getStage().setMinHeight(500);

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
			final HBox box = new HBox();
			box.getStyleClass().add("unlockedWeapon");

			final Label label = new Label(weapon.getCategory() + " - " + weapon.getName());
			final Pane pane = new Pane();
			HBox.setHgrow(pane, Priority.ALWAYS);

			final StackPane stackPane = new StackPane(FXUtils.getSvg(stream));
			stackPane.setOnMouseClicked(e -> {
				profile.lockWeapon(weapon);
				WarzoneLoadouts.getProfiles().save();

				unlockedBox.getChildren().remove(box);
			});

			box.getChildren().addAll(label, pane, stackPane);

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
		primaryGrid.getChildren().clear();
		secondaryGrid.getChildren().clear();

		final RandomLoadout randomLoadout = RandomLoadout.fromProfile(profile);

		final ChoosedWeapon primary = randomLoadout.getPrimary();
		final ChoosedWeapon secondary = randomLoadout.getSecondary();

		primaryLabel.setText(primary.getWeapon().getCategory() + " - " + primary.getWeapon().getName());
		secondaryLabel.setText(secondary.getWeapon().getCategory() + " - " + secondary.getWeapon().getName());

		showAttachments(primary, primaryGrid);
		showAttachments(secondary, secondaryGrid);

		perk1Label.setText(randomLoadout.getFirstPerk().getName());
		perk2Label.setText(randomLoadout.getSecondPerk().getName());
		perk3Label.setText(randomLoadout.getThirdPerk().getName());

		lethalLabel.setText(randomLoadout.getLethal().getName());
		tacticalLabel.setText(randomLoadout.getTactical().getName());
	}

	private void showAttachments(ChoosedWeapon secondary, GridPane secondaryGrid) {
		List<ChoosedAttachment> choosedAttachments = secondary.getChoosedAttachments();
		for (int i = 0, choosedAttachmentsSize = choosedAttachments.size(); i < choosedAttachmentsSize; i++) {
			ChoosedAttachment attachment = choosedAttachments.get(i);

			secondaryGrid.addRow(i, new Label(attachment.getCategoryName()), new Label(attachment.getAttachmentName()), new Label("Lvl " + attachment.getAttachmentLevel()));
		}
	}
}
