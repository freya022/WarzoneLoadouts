package com.freya02.loadouts;

import java.util.ArrayList;
import java.util.List;

public class Weapon {
	private final Game game;

	private final boolean primary;
	private final boolean locked;

	private final String category;

	private final String name;
	private int id;

	private final List<AttachmentCategory> attachmentCategories = new ArrayList<>();

	public Weapon(Game game, boolean primary, String category, String name, boolean locked) {
		this.game = game;
		this.primary = primary;
		this.category = category;
		this.name = name;
		this.locked = locked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isLocked() {
		return locked;
	}

	public List<AttachmentCategory> getAttachmentCategories() {
		return attachmentCategories;
	}

	@Override
	public String toString() {
		return String.format("%s: %s : %s : %s : %d attachment categories, %d total attachments", game, primary, category, name, attachmentCategories.size(), attachmentCategories.stream().mapToInt(v -> v.getAttachments().size()).sum());
	}

	public String getName() {
		return name;
	}

	public boolean isPrimary() {
		return primary;
	}

	public String getCategory() {
		return category;
	}

	public Game getGame() {
		return game;
	}
}
