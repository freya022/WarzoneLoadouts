package com.freya02.loadouts.ui;

import com.freya02.ui.window.LazyWindow;
import javafx.application.Platform;
import javafx.stage.WindowEvent;

public class AwaitableWindow<T> extends LazyWindow {
	private final Object key = new Object();

	public T waitFor() {
		return waitFor(false);
	}

	@SuppressWarnings("unchecked")
	public T waitFor(boolean exitOnClose) {
		getWindow().getStage().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
			if (exitOnClose) {
				System.exit(0);
			} else {
				Platform.exitNestedEventLoop(key, null);
			}
		});

		return (T) Platform.enterNestedEventLoop(key);
	}

	protected void returnValue(T t) {

		getWindow().getStage().close();
		Platform.exitNestedEventLoop(key, t);
	}
}
