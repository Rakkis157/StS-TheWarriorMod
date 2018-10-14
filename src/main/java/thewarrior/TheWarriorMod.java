package thewarrior;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
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
import thewarrior.cards.*;
import thewarrior.cards.AbstractWarriorAttackCard.AttackType;
import thewarrior.cards.AbstractWarriorAttackCard.WeaponType;
import thewarrior.characters.*;
import thewarrior.relics.UnnamedStartingRelic;

@SpireInitializer
public class TheWarriorMod
		implements EditCharactersSubscriber, EditCardsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber {

	public static final Logger logger = LogManager.getLogger(TheWarriorMod.class.getName());
	public static final String MOD_ID = "TheWarrior";

	private static final String MOD_NAME = "The Warrior";
	private static final String AUTHOR = "yhrcyt";
	private static final String DESCRIPTION = "The mod creator is too lazy to write any discription.";

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
		Texture badgeTexture = ImageMaster.loadImage("images/badge.png");
		BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, null);
	}

	@Override
	public void receiveEditCharacters() {
		logger.info("adding the warrior...");

		BaseMod.addCharacter(new TheWarrior(CardCrawlGame.playerName), TheWarriorEnum.WARRIOR_GREY, "images/char/button.png",
				"images/char/portrait.png", TheWarriorEnum.THE_WARRIOR);
	}

	@Override
	public void receiveEditCards() {
		logger.info("adding warrior cards...");
		for (int i = 0; i < WeaponType.WEAPON_NUM; i++)
			AbstractWarriorAttackCard.listWeaponType.add(new ArrayList<>());
		for (int i = 0; i < AttackType.ATTACK_NUM; i++)
			AbstractWarriorAttackCard.listAttackType.add(new ArrayList<>());

		// STATUS
		BaseMod.addCard(new Fatigue());

		// BASIC
		BaseMod.addCard(new Claw());
		BaseMod.addCard(new Dagger());
		BaseMod.addCard(new Sword());
		BaseMod.addCard(new Axe());
		BaseMod.addCard(new Hammer());

		BaseMod.addCard(new Shield());
		BaseMod.addCard(new PrepareToCombo());
		BaseMod.addCard(new BladeDance());

		// COMMON
		BaseMod.addCard(new WeakeningClaw());
		BaseMod.addCard(new WeakeningDagger());
		BaseMod.addCard(new WoundingSword());
		BaseMod.addCard(new WoundingAxe());
		BaseMod.addCard(new WoundingHammer());
		BaseMod.addCard(new PoisonousClaw());
		BaseMod.addCard(new PoisonousDagger());
		BaseMod.addCard(new PoisonousSword());
		BaseMod.addCard(new PoisonousAxe());
		BaseMod.addCard(new PoisonousHammer());
		BaseMod.addCard(new SpikyClaw());
		BaseMod.addCard(new SpikyDagger());
		BaseMod.addCard(new SpikySword());
		BaseMod.addCard(new SpikyAxe());
		BaseMod.addCard(new SpikyHammer());

		BaseMod.addCard(new Taunt());
		BaseMod.addCard(new Envenom());
		BaseMod.addCard(new BleedingPoison());
		BaseMod.addCard(new PoisonousShield());
		BaseMod.addCard(new SpikyShield());

		BaseMod.addCard(new WoundInfection());
		BaseMod.addCard(new BloodExcitement());

		// UNCOMMON
		BaseMod.addCard(new SharpClaw());
		BaseMod.addCard(new SharpDagger());
		BaseMod.addCard(new HeavySword());
		BaseMod.addCard(new HeavyAxe());
		BaseMod.addCard(new HeavyHammer());
		BaseMod.addCard(new ShortClaw());
		BaseMod.addCard(new ShortDagger());
		BaseMod.addCard(new ShortSword());
		BaseMod.addCard(new LightAxe());
		BaseMod.addCard(new LightHammer());
		BaseMod.addCard(new StrongClaw());
		BaseMod.addCard(new StrongDagger());
		BaseMod.addCard(new StrongSword());
		BaseMod.addCard(new StrongAxe());
		BaseMod.addCard(new StrongHammer());
		BaseMod.addCard(new SpecialClaw());
		BaseMod.addCard(new SpecialDagger());
		BaseMod.addCard(new SpecialSword());
		BaseMod.addCard(new SpecialAxe());
		BaseMod.addCard(new SpecialHammer());

		BaseMod.addCard(new WarCry());
		BaseMod.addCard(new WoundPain());
		BaseMod.addCard(new Prepared());
		BaseMod.addCard(new Bloody());
		BaseMod.addCard(new SpecialShield());
		BaseMod.addCard(new ScrapShield());
		BaseMod.addCard(new DesperateShield());

		BaseMod.addCard(new BloodyBlade());
		BaseMod.addCard(new BloodyCloud());
		BaseMod.addCard(new BloodyNature());

		// RARE
		BaseMod.addCard(new HawkClaw());
		BaseMod.addCard(new PushDagger());
		BaseMod.addCard(new WaveBladeSword());
		BaseMod.addCard(new HalberdAxe());
		BaseMod.addCard(new UltralightHammer());

		BaseMod.addCard(new PreparingShield());
		BaseMod.addCard(new BloodyFumes());
		BaseMod.addCard(new Charge());
		BaseMod.addCard(new ThrowAway());
		BaseMod.addCard(new DoubleCombo());

		BaseMod.addCard(new Stronger());
		BaseMod.addCard(new Faster());
		BaseMod.addCard(new ComboForm());
		BaseMod.addCard(new UltimateArmor());
		BaseMod.addCard(new Anger());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void receiveEditKeywords() {
		logger.info("adding warrior keywords...");
		Gson gson = new Gson();

		String keywordStrings = Gdx.files.internal("localization/TheWarrior-Keywords.json")
				.readString(String.valueOf(StandardCharsets.UTF_8));
		Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

		keywords = (Map) gson.fromJson(keywordStrings, typeToken);

		keywords.forEach((k, v) -> {
			logger.info("TheWarriorMod: Adding Keyword - " + v.NAMES[0]);
			BaseMod.addKeyword(v.NAMES, v.DESCRIPTION);
		});
	}

	@Override
	public void receiveEditStrings() {
		String cardStrings = Gdx.files.internal("localization/TheWarrior-CardStrings.json")
				.readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(CardStrings.class, cardStrings);

		String relicStrings = Gdx.files.internal("localization/TheWarrior-RelicStrings.json")
				.readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);

		String powerStrings = Gdx.files.internal("localization/TheWarrior-PowerStrings.json")
				.readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
	}

	@Override
	public void receiveEditRelics() {
		logger.info("adding warrior relics...");
		BaseMod.addRelicToCustomPool(new UnnamedStartingRelic(), TheWarriorEnum.WARRIOR_GREY);
	}

}
