package com.freya02.loadouts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ChoosedWeapon {
	private final Weapon weapon;
	private final List<ChoosedAttachment> choosedAttachments = new ArrayList<>();

	public ChoosedWeapon(Weapon weapon, Profile profile) {
		this.weapon = weapon;

		//Choose 5 categories
		final List<AttachmentCategory> attachmentCategories = weapon.getAttachmentCategories().stream().filter(a -> getUnlockedAttachmentCount(weapon, profile, a) > 0).collect(Collectors.toList());
		final List<Integer> attachmentNumbers = Utils.uniqueRandoms(Math.min(5, attachmentCategories.size()), 0, attachmentCategories.size());

		for (Integer attachmentNumber : attachmentNumbers) {
			final AttachmentCategory attachmentCategory = attachmentCategories.get(attachmentNumber);

			final Attachment attachment = getRandomAttachment(weapon, profile, attachmentCategory);
			choosedAttachments.add(new ChoosedAttachment(attachmentCategory.getName(), attachment.getName(), attachment));
		}
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public List<ChoosedAttachment> getChoosedAttachments() {
		return choosedAttachments;
	}

	private int getUnlockedAttachmentCount(Weapon weapon, Profile profile, AttachmentCategory attachmentCategory) {
		return (int) attachmentCategory.getAttachments().stream().filter(a -> a.isUnlockedFor(weapon, profile)).count();
	}

	private Attachment getRandomAttachment(Weapon weapon, Profile profile, AttachmentCategory attachmentCategory) {
		final List<Attachment> availableAttachments = attachmentCategory.getAttachments().stream().filter(a -> a.isUnlockedFor(weapon, profile)).toList();
		final int index = ThreadLocalRandom.current().nextInt(availableAttachments.size());
		return availableAttachments.get(index);
	}

	@Override
	public String toString() {
		return "%s : %s%s".formatted(weapon.getCategory(), weapon.getName(), choosedAttachments.isEmpty() ? "" : " : " + choosedAttachments.stream().map(ChoosedAttachment::toString).collect(Collectors.joining(", ")));
	}
}
