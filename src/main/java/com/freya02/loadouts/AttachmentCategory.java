package com.freya02.loadouts;

import java.util.ArrayList;
import java.util.List;

public class AttachmentCategory {
	private final String name;
	private final List<Attachment> attachments = new ArrayList<>();

	public AttachmentCategory(String name) {
		this.name = name;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	@Override
	public String toString() {
		return name + " : " + attachments.size() + " attachments";
	}

	public String getName() {
		return name;
	}
}
