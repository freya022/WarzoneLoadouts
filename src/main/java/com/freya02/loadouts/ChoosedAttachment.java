package com.freya02.loadouts;

public class ChoosedAttachment {
	private final String categoryName, attachmentName;
	private final Attachment attachment;

	public ChoosedAttachment(String categoryName, String attachmentName, Attachment attachment) {
		this.categoryName = categoryName;
		this.attachmentName = attachmentName;
		this.attachment = attachment;
	}

	public int getAttachmentLevel() {
		return attachment.getRequiredLevel();
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	@Override
	public String toString() {
		return "%s : %s, lvl %d".formatted(categoryName, attachmentName, attachment.getRequiredLevel());
	}
}