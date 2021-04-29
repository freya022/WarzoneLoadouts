package com.freya02.loadouts;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
		return fromProfile(profile, Game.values());
	}

	public static RandomLoadout fromProfile(Profile profile, Game... games) {
		final List<Weapon> weapons = WarzoneLoadouts.getWeapons();

		Weapon primary = getRandomItem(weapons.stream().filter(Weapon::isPrimary).filter(w -> canTakeWeapon(profile, w, games)).collect(Collectors.toList()));
		Weapon secondary = getRandomItem(weapons.stream().filter(w -> w != primary && canTakeWeapon(profile, w, games)).collect(Collectors.toList()));

		final Perks perks = WarzoneLoadouts.getPerks();
		final Perk firstPerk = getRandomItem(perks.getFirstPerks());
		final Perk secondPerk = secondary.isPrimary() ? perks.getSecondPerks().stream().filter(Perk::isOverkill).findFirst().orElseThrow() : getRandomItem(perks.getSecondPerks().stream().filter(perk -> !perk.isOverkill()).collect(Collectors.toList()));
		final Perk thirdPerk = getRandomItem(WarzoneLoadouts.getPerks().getThirdPerks());

		final Tactical tactical = getRandomItem(WarzoneLoadouts.getTacticals());
		final Lethal lethal = getRandomItem(WarzoneLoadouts.getLethals());

		return new RandomLoadout(new ChoosedWeapon(primary, profile), new ChoosedWeapon(secondary, profile), firstPerk, secondPerk, thirdPerk, tactical, lethal);
	}

	private static boolean canTakeWeapon(Profile profile, Weapon weapon, Game[] games) {
		return Arrays.asList(games).contains(weapon.getGame()) && (!weapon.isLocked() || profile.getUnlockedWeapons().contains(weapon.getId()));
	}

	private static <T> T getRandomItem(List<T> list) {
		return list.get(ThreadLocalRandom.current().nextInt(list.size()));
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
