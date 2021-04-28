package com.freya02.loadouts;

import com.freya02.gson.GsonUtils;
import com.google.gson.GsonBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttachmentMakerJson {
	private static final Weapons weapons = new Weapons();
	private static final Pattern SEARCH_RESULT_PATTERN = Pattern.compile("<div class=\"items-row clearfix\">\\X+?<a href=\"([^\"]+?)\">\\X+?</div>");
	private static final Pattern ATTACHMENTS_BLOCK_PATTERN = Pattern.compile("<div class=\"cod-cw-attachments\">\\X+?<dl class=\"fields-container\">(\\X+?)</dl></div>");
	private static final Pattern ATTACHMENT_BLOCK_PATTERN = Pattern.compile("<dd.*>\\X+?</dd>");
	private static final Pattern ATTACHMENT_CATEGORY_NAME_PATTERN = Pattern.compile("<thead><tr><th.*?>(\\X+?)</th><th.*?>\\X*?</th></tr></thead>");
	private static final Pattern ATTACHMENT_PATTERN = Pattern.compile("<tr><td>(\\X+?)</td><td>(\\X*?)</td></tr>");

	public static void main(String[] args) throws IOException {
		createFile(Path.of("BlankWeapons.json"));

//		final List<AttachmentCategory> cwAttachments = getCWAttachments("ak-47");
	}

	private static List<AttachmentCategory> getCWAttachments(String weaponName) {
		final List<AttachmentCategory> attachmentCategories = new ArrayList<>();
		final String url = searchCWWeapon(weaponName);

		final String html = Utils.unescapeHtml3(Utils.downloadHtml(url));
		final Matcher attachmentsBlockMatcher = ATTACHMENTS_BLOCK_PATTERN.matcher(html);
		if (attachmentsBlockMatcher.find()) {
			final String attachmentsBlockHtml = attachmentsBlockMatcher.group(1);

			final Matcher attachmentBlockMatcher = ATTACHMENT_BLOCK_PATTERN.matcher(attachmentsBlockHtml);
			while (attachmentBlockMatcher.find()) {
				final String attachmentBlockHtml = attachmentBlockMatcher.group();

				final Matcher categoryNameMatcher = ATTACHMENT_CATEGORY_NAME_PATTERN.matcher(attachmentBlockHtml);
				if (!categoryNameMatcher.find()) throw new IllegalArgumentException();

				final String categoryName = categoryNameMatcher.group(1);
				final AttachmentCategory attachmentCategory = new AttachmentCategory(categoryName);

//				System.out.println(categoryName);
				final Matcher matcher = ATTACHMENT_PATTERN.matcher(attachmentBlockHtml);
				while (matcher.find()) {
					final String attachmentName = Utils.removeHtmlTags(matcher.group(1)).trim();
					final String attachmentLevel = Utils.removeHtmlTags(matcher.group(2)).trim();
//					System.out.println("\t- " + attachmentName + " at lvl " + attachmentLevel);
					if (attachmentLevel.isEmpty()) {
						System.err.println(weaponName + " is incomplete at " + url);
						return List.of();
					}
					attachmentCategory.getAttachments().add(new Attachment(attachmentName, Integer.parseInt(attachmentLevel)));
				}

//				System.out.println();
				attachmentCategories.add(attachmentCategory);
			}
		}

		return attachmentCategories;
	}

	private static String searchCWWeapon(String weaponName) {
		final String html = Utils.downloadHtml("https://www.gamesatlas.com/index.php?searchword=" + URLEncoder.encode(weaponName, StandardCharsets.UTF_8) + "&searchphrase=all&Itemid=102&option=com_search");

		final Matcher matcher = SEARCH_RESULT_PATTERN.matcher(html);
		while (matcher.find()) {
			final String searchResultSuburl = matcher.group(1);
			if (searchResultSuburl.startsWith("/cod-black-ops-cold-war/weapons/")) {
				return "https://www.gamesatlas.com" + searchResultSuburl;
			}
		}

		throw new NoSuchElementException();
	}

	public static void createFile(Path path) throws IOException {
		readWeapons("Primaries.csv", true);
		readWeapons("Secondaries.csv", false);

		GsonUtils.saveGson(path, weapons, GsonBuilder::disableHtmlEscaping);
	}

	private static void readWeapons(String inputFile, boolean isPrimary) throws IOException {
		final Weapon[] internalList;
		final ExecutorService es = Executors.newCachedThreadPool();
		try (CSVParser csvParser = new CSVParser(new InputStreamReader(AttachmentMakerJson.class.getResourceAsStream(inputFile)), CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader())) {
			final List<CSVRecord> records = csvParser.getRecords();
			internalList = new Weapon[records.size()];

			for (CSVRecord record : records) {
				es.submit(() -> {
					final Game game = record.get("game").equals("CW") ? Game.COLD_WAR : Game.MODERN_WARFARE;
					final Weapon e = new Weapon(game,
							isPrimary,
							record.get("category"),
							record.get("name"),
							false);

					if (game == Game.COLD_WAR) {
						final List<AttachmentCategory> cwAttachments = getCWAttachments(record.isSet("wikiName") ? record.get("wikiName") : record.get("name"));
						e.getAttachmentCategories().addAll(cwAttachments);
					}

					synchronized (internalList) {
						internalList[(int) record.getRecordNumber() - 1] = e;
					}
				});
			}
		}

		try {
			es.shutdown();
			es.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Collections.addAll(weapons.getWeapons(), internalList);
	}
}
