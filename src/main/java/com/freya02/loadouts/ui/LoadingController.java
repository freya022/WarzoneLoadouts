package com.freya02.loadouts.ui;

import com.freya02.loadouts.WarzoneLoadouts;
import com.freya02.ui.window.CloseHandler;
import com.freya02.ui.window.LazyWindow;
import com.freya02.ui.window.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class LoadingController extends LazyWindow {
	private final WarzoneLoadouts warzoneLoadouts;

	@FXML private Label label;

	public LoadingController(WarzoneLoadouts warzoneLoadouts) throws IOException {
		this.warzoneLoadouts = warzoneLoadouts;
		new WindowBuilder("LoadingController.fxml", "Chargement...").onClose(CloseHandler.SYSTEM_EXIT).createBorderless(this);
	}

	@Override
	protected void onInitialized() {
		label.textProperty().bind(warzoneLoadouts.stateProperty());
	}
}
