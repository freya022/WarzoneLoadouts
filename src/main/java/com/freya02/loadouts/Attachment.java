package com.freya02.loadouts;

public class Attachment {
	private final String name;
	private int requiredLevel = -1;

	public Attachment(String name) {
		this.name = name;
	}

	public Attachment(String attachmentName, int attachmentLevel) {
		this.name = attachmentName;
		this.requiredLevel = attachmentLevel;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}

	public void setRequiredLevel(int requiredLevel) {
		this.requiredLevel = requiredLevel;
	}

	public boolean isUnlockedFor(Weapon weapon, Profile profile) {
		return requiredLevel == -1 || profile.getLevelsMap().get(weapon.getId()) == null || profile.getLevelsMap().get(weapon.getId()) >= requiredLevel;
	}

	public String getName() {
		return name;
	}
}
