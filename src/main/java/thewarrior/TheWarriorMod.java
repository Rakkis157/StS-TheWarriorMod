package thewarrior;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import thewarrior.cards.attack.*;
import thewarrior.cards.attack.AbstractWarriorAttackCard.AttackType;
import thewarrior.cards.attack.AbstractWarriorAttackCard.WeaponType;
import thewarrior.characters.*;
import thewarrior.relics.Gladiatoria;

@SpireInitializer
public class TheWarriorMod implements EditCharactersSubscriber, EditCardsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber,
		EditRelicsSubscriber, PostInitializeSubscriber {

	public static final Logger logger = LogManager.getLogger(TheWarriorMod.class.getName());
	public static final String MOD_ID = "TheWarrior";
// private static final String MOD_NAME = "The Warrior";
// private static final String AUTHOR = "yhrcyt";
// private static final String DESCRIPTION = "";
	private static final Color WARRIOR_GREY_COLOR = CardHelper.getColor(200.0f, 200.0f, 200.0f);

	private static Map<String, Keyword> keywords;

	public TheWarriorMod() {
		BaseMod.subscribe(this);

		logger.info("adding warrior color");
		BaseMod.addColor(TheWarriorEnum.WARRIOR_GREY, WARRIOR_GREY_COLOR, WARRIOR_GREY_COLOR, WARRIOR_GREY_COLOR, WARRIOR_GREY_COLOR,
				WARRIOR_GREY_COLOR, WARRIOR_GREY_COLOR, WARRIOR_GREY_COLOR, "images/ui/cardui/bg_attack_grey_512.png",
				"images/ui/cardui/bg_skill_grey_512.png", "images/ui/cardui/bg_power_grey_512.png",
				"images/ui/cardui/card_grey_orb_512.png", "images/ui/cardui/bg_attack_grey.png", "images/ui/cardui/bg_skill_grey.png",
				"images/ui/cardui/bg_power_grey.png", "images/ui/cardui/card_grey_orb.png");
	}

	public static void initialize() {
		new TheWarriorMod();
	}

	public void receivePostInitialize() {
		// BaseMod.registerModBadge(ImageMaster.loadImage("images/TheWarriorModBadge.png"), MOD_NAME, AUTHOR, DESCRIPTION, null);
	}

	@Override
	public void receiveEditCharacters() {
		logger.info("adding the warrior...");

		BaseMod.addCharacter(new TheWarrior(CardCrawlGame.playerName), "images/char/button.png", "images/char/portrait.png",
				TheWarriorEnum.THE_WARRIOR);
	}

	@Override
	public void receiveEditCards() {
		logger.info("adding warrior cards...");
		for (int i = 0; i < WeaponType.WEAPON_NUM; i++)
			AbstractWarriorAttackCard.listWeaponType.add(new ArrayList<>());
		for (int i = 0; i < AttackType.ATTACK_NUM; i++)
			AbstractWarriorAttackCard.listAttackType.add(new ArrayList<>());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void receiveEditKeywords() {
		logger.info("adding warrior keywords...");
		Gson gson = new Gson();

		String keywordStrings =
				Gdx.files.internal("localization/TheWarrior-Keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
		Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

		keywords = (Map) gson.fromJson(keywordStrings, typeToken);

		keywords.forEach((k, v) -> {
			logger.info("TheWarriorMod: Adding Keyword - " + v.NAMES[0]);
			BaseMod.addKeyword(v.NAMES, v.DESCRIPTION);
		});
	}

	@Override
	public void receiveEditStrings() {
		String cardStrings =
				Gdx.files.internal("localization/TheWarrior-CardStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(CardStrings.class, cardStrings);

		String relicStrings =
				Gdx.files.internal("localization/TheWarrior-RelicStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);

		String powerStrings =
				Gdx.files.internal("localization/TheWarrior-PowerStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
	}

	@Override
	public void receiveEditRelics() {
		logger.info("adding warrior relics...");
		BaseMod.addRelicToCustomPool(new Gladiatoria(), TheWarriorEnum.WARRIOR_GREY);
	}

}
