package com.freya02.loadouts;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadout {
	private final ChoosedWeapon primary, secondary;
	private final Perk firstPerk, secondPerk, thirdPerk;
	private final Tactical tactical;
	private final Lethal lethal;

	public RandomLoadout(ChoosedWeapon primary, ChoosedWeapon secondary, Perk firstPerk, Perk secondPerk, Perk thirdPerk, Tactical tactical, Lethal lethal) {
		this.primary = primary;
		this.secondary = secondary;
		this.firstPerk = firstPerk;
		this.secondPerk = secondPerk;
		this.thirdPerk = thirdPerk;
		this.tactical = tactical;
		this.lethal = lethal;
	}

	public static RandomLoadout fromProfile(Profile profile) {
		final List<Weapon> weapons = WarzoneLoadouts.getWeapons();
		final List<Integer> weaponNumbers = Utils.uniqueRandoms(2, 0, weapons.size(), i -> {
			final Weapon weapon = weapons.get(i);

			return !weapon.isLocked() || profile.getUnlockedWeapons().contains(weapon.getId());
		});

		Weapon primary = weapons.get(weaponNumbers.get(0));
		Weapon secondary = weapons.get(weaponNumbers.get(1));

		final Perks perks = WarzoneLoadouts.getPerks();
		final Perk firstPerk = getRandomItem(perks.getFirstPerks());
		final Perk secondPerk = secondary.isPrimary() ? perks.getSecondPerks().stream().filter(Perk::isOverkill).findFirst().orElseThrow() : getRandomItem(perks.getSecondPerks());
		final Perk thirdPerk = getRandomItem(WarzoneLoadouts.getPerks().getThirdPerks());

		final Tactical tactical = getRandomItem(WarzoneLoadouts.getTacticals());
		final Lethal lethal = getRandomItem(WarzoneLoadouts.getLethals());

		return new RandomLoadout(new ChoosedWeapon(primary, profile), new ChoosedWeapon(secondary, profile), firstPerk, secondPerk, thirdPerk, tactical, lethal);
	}

	private static <T> T getRandomItem(List<T> list) {
		return list.get(ThreadLocalRandom.current().nextInt(list.size()));
	}

	public static void main(String[] args) {
		final RandomLoadout loadout = fromProfile(new Profile("Lol"));

		System.out.println();
	}

	public ChoosedWeapon getPrimary() {
		return primary;
	}

	public ChoosedWeapon getSecondary() {
		return secondary;
	}

	public Perk getFirstPerk() {
		return firstPerk;
	}

	public Perk getSecondPerk() {
		return secondPerk;
	}

	public Perk getThirdPerk() {
		return thirdPerk;
	}

	public Tactical getTactical() {
		return tactical;
	}

	public Lethal getLethal() {
		return lethal;
	}
}
