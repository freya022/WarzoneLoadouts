package com.freya02.loadouts;

import com.freya02.gson.GsonUtils;
import com.freya02.ui.UILib;
import com.freya02.ui.window.LazyWindow;
import com.freya02.ui.window.WindowBuilder;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AttachmentMaker extends LazyWindow {
	private static final Path WEAPONS_PATH = Path.of("Weapons.json");
	private static Weapons weapons;

	@FXML private Label nameLabel, categoryLabel, gameLabel;
	@FXML private TextArea textArea;
	@FXML private Button firstOk, secondOk;
	@FXML private GridPane levelsBox;

	private final BooleanProperty locked = new SimpleBooleanProperty();

	private final List<AttachmentCategory> attachmentCategories = new ArrayList<>();
	private Weapon weapon;

	public static void main(String[] args) throws IOException {
		if (Files.notExists(WEAPONS_PATH)) AttachmentMakerJson.createFile(WEAPONS_PATH);

		weapons = GsonUtils.loadGson(WEAPONS_PATH, Weapons.class);
		Platform.startup(AttachmentMaker::newAttachmentMaker);
	}

	private static void newAttachmentMaker() {
		try {
			final WindowBuilder builder = new WindowBuilder("AttachmentMaker.fxml", "Attachements").resizable();
			builder.create(new AttachmentMaker());
		} catch (Exception e) {
			UILib.displayError(e);
			System.exit(-1);
		}
	}

	@Override
	protected void onInitialized() {
		textArea.disableProperty().bind(locked);
		firstOk.disableProperty().bind(locked);
		secondOk.disableProperty().bind(locked.not());
		levelsBox.disableProperty().bind(locked.not());

		newWeapon();

		textArea.textProperty().addListener((x, old, text) -> {
			if (text.length() - old.length() > 1) {
				text = text.replaceAll("([^\\n]\\n)(Optic|Barrel|Muzzle|Magazine|Body|Underbarrel|Handle|Stock|Laser|Perks)\\n", "$1\n$2\n");
				textArea.setText(text);
			}

			levelsBox.getChildren().clear();
			String[] split = text.split("\n\n");
			for (int categoryNumber = 0; categoryNumber < split.length; categoryNumber++) {
				String category = split[categoryNumber];
				
				final String[] lines = category.split("\n");
				final String categoryName = lines[0];
				for (int attachmentNumber = 1; attachmentNumber < lines.length; attachmentNumber++) {
					String attachmentName = lines[attachmentNumber];

					makeWidgets(categoryName, categoryNumber, attachmentNumber, attachmentName);
				}
			}
		});
	}

	private void newWeapon() {
		for (Weapon weapon : weapons.getWeapons()) {
			if (weapon.getAttachmentCategories().isEmpty()) {
				nameLabel.setText(weapon.getName());
				gameLabel.setText(weapon.getGame().toString());
				categoryLabel.setText(weapon.getCategory());

				this.weapon = weapon;

				break;
			}
		}
	}

	@FXML private void onFirstClicked(MouseEvent event) {
		locked.set(true);

		final String text = textArea.getText();

		levelsBox.getChildren().clear();
		String[] split = text.split("\n\n");
		for (int categoryNumber = 0; categoryNumber < split.length; categoryNumber++) {
			String category = split[categoryNumber];

			final String[] lines = category.split("\n");
			final String categoryName = lines[0];
			final AttachmentCategory attachmentCategory = new AttachmentCategory(categoryName);
			attachmentCategories.add(attachmentCategory);
			for (int attachmentNumber = 1; attachmentNumber < lines.length; attachmentNumber++) {
				String attachmentName = lines[attachmentNumber];

				final Attachment attachment = new Attachment(attachmentName);
				attachmentCategory.getAttachments().add(attachment);

				final Spinner<Integer> spinner = makeWidgets(categoryName, categoryNumber, attachmentNumber, attachmentName);

				spinner.setEditable(true);
				spinner.valueProperty().addListener((x, y, newV) -> attachment.setRequiredLevel(newV));
			}
		}
	}

	@NotNull
	private Spinner<Integer> makeWidgets(String categoryName, int categoryNumber, int attachmentNumber, String attachmentName) {
		final Spinner<Integer> spinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, 80, -1, 1));
		final Label label = new Label(categoryName + " : " + attachmentName);
		spinner.setMaxWidth(70);

		levelsBox.add(label, categoryNumber * 2, attachmentNumber);
		levelsBox.add(spinner, categoryNumber * 2 + 1, attachmentNumber);

		return spinner;
	}

	@FXML private void onSecondClicked(MouseEvent event) {
		weapon.getAttachmentCategories().addAll(attachmentCategories);
		attachmentCategories.clear();
		levelsBox.getChildren().clear();
		textArea.clear();

		try {
			GsonUtils.saveGson(WEAPONS_PATH, weapons);
			Files.copy(WEAPONS_PATH, WEAPONS_PATH.resolveSibling("Weapons" + System.currentTimeMillis() + ".json"));
		} catch (Exception e) {
			UILib.displayError(e);
		}

		locked.set(false);

		newWeapon();
	}
}
