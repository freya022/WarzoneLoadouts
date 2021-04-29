package com.freya02.loadouts.ui;

import com.freya02.loadouts.Profile;
import com.freya02.loadouts.Profiles;
import com.freya02.loadouts.WarzoneLoadouts;
import com.freya02.ui.window.WindowBuilder;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class ProfilesController extends AwaitableWindow<Profile> {
	@FXML private VBox profilesBox;

	@Nullable
	public static Profile selectProfile() {
		try {
			final ProfilesController controller = new ProfilesController();
			new WindowBuilder("Profiles.fxml", "Profils Warzone").resizable().create(controller);

			return controller.waitFor(true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void onInitialized() {
		final Profiles warzoneProfiles = WarzoneLoadouts.getProfiles();
		List<Profile> profiles = warzoneProfiles.getProfiles();
		for (int i = profiles.size() - 1; i >= 0; i--) {
			Profile profile = profiles.get(i);
			createProfileBox(profile);
		}

		final JFXButton newButton = new JFXButton("Créer un nouveau profil");
		newButton.setOnMouseClicked(e -> {
			final Profile profile = NewProfileController.createProfile();
			if (profile != null) {
				warzoneProfiles.getProfiles().add(profile);
				warzoneProfiles.save();

				createProfileBox(profile);
			}
		});

		newButton.setMaxWidth(Double.MAX_VALUE);
		profilesBox.getChildren().add(newButton);
	}

	private void createProfileBox(Profile profile) {
		final Pane pane = new Pane();
		HBox.setHgrow(pane, Priority.ALWAYS);

		final ImageView imageView = new ImageView(profile.getImage());
		imageView.setFitWidth(64);
		imageView.setFitHeight(64);

		final HBox box = new HBox(imageView, pane, new Label(profile.getName()));
		box.getStyleClass().add("profileBox");
		box.setAlignment(Pos.CENTER_LEFT);

		box.setOnMouseClicked(e -> returnValue(profile));

		profilesBox.getChildren().add(0, box);
	}
}
