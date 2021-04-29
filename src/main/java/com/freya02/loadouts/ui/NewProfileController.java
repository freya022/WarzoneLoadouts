package com.freya02.loadouts.ui;

import com.freya02.loadouts.Profile;
import com.freya02.ui.UILib;
import com.freya02.ui.window.WindowBuilder;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class NewProfileController extends AwaitableWindow<Profile> {
	@FXML private TextField nameField, pictureField;
	@FXML private JFXButton createButton;

	private final ObjectProperty<File> profileFileProperty = new SimpleObjectProperty<>();

	@Nullable
	public static Profile createProfile() {
		try {
			final NewProfileController controller = new NewProfileController();
			new WindowBuilder("NewProfile.fxml", "Nouveau profil Warzone").create(controller);

			return controller.waitFor();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void onInitialized() {
		createButton.disableProperty().bind(profileFileProperty.isNull().or(nameField.textProperty().isEmpty()));
		pictureField.textProperty().bind(profileFileProperty.asString());
	}

	@FXML private void onCreateClicked(MouseEvent event) {
		try {
			returnValue(new Profile(nameField.getText(), profileFileProperty.get()));
		} catch (IOException e) {
			UILib.displayError(e);
		}
	}

	@FXML private void onSelectClicked(MouseEvent event) {
		final FileChooser chooser = new FileChooser();
		chooser.setTitle("Sélectionner une photo de profil");
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.bmp"));
		final File file = chooser.showOpenDialog(this.getWindow().getStage());
		if (file != null) {
			profileFileProperty.set(file);
		}
	}
}
