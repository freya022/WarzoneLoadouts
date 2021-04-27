package com.freya02.loadouts;

public class ChoosedAttachment {
	private final String categoryName, attachmentName;

	public ChoosedAttachment(String categoryName, String attachmentName) {
		this.categoryName = categoryName;
		this.attachmentName = attachmentName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	@Override
	public String toString() {
		return "%s : %s".formatted(categoryName, attachmentName);
	}
}