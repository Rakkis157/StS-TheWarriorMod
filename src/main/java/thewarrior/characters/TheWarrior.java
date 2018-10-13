package thewarrior.characters;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import thewarrior.TheWarriorMod;
import thewarrior.cards.Axe;
import thewarrior.cards.BladeDance;
import thewarrior.cards.Claw;
import thewarrior.cards.Dagger;
import thewarrior.cards.Hammer;
import thewarrior.cards.PrepareToCombo;
import thewarrior.cards.Shield;
import thewarrior.cards.Sword;
import thewarrior.ui.panels.energyorb.EnergyOrbGray;

public class TheWarrior extends AbstractPlayer {
	public static final String CHARACTER_NAME = "The Warrior";
	public static final String FLAVOR_TEXT = "Sorry, the mod creator don't know what to say here.";

	public static final int ENERGY_PER_TURN = 3;
	public static final int STARTING_HP = 150;
	public static final int MAX_HP = 150;
	public static final int STARTING_GOLD = 99;
	public static final int HAND_SIZE = 5;
	public static final int ORB_SLOTS = 0;

	public static final String MY_CHARACTER_SHOULDER_2 = "images/char/shoulder2.png"; // campfire pose
	public static final String MY_CHARACTER_SHOULDER_1 = "images/char/shoulder1.png"; // another campfire pose
	public static final String MY_CHARACTER_CORPSE = "images/char/corpse.png"; // dead corpse
	public static final String MY_CHARACTER_SKELETON_ATLAS = "images/char/skeleton.atlas"; // spine animation atlas
	public static final String MY_CHARACTER_SKELETON_JSON = "images/char/skeleton.json"; // spine animation json

	private static final String WARRIOR_FILE = SaveAndContinue.getPlayerSavePath(TheWarriorEnum.THE_WARRIOR);
	private EnergyOrbInterface energyOrb = new EnergyOrbGray();
	private Prefs prefs;
	private CharStat charStat;

	public TheWarrior(String playerName) {
		super(playerName, TheWarriorEnum.THE_WARRIOR);
		this.charStat = new CharStat(this);

		this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
		this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values

		initializeClass(null, MY_CHARACTER_SHOULDER_2, MY_CHARACTER_SHOULDER_1, MY_CHARACTER_CORPSE, getLoadout(), 20.0F, -10.0F, 220.0F,
				290.0F, new EnergyManager(ENERGY_PER_TURN)); // required call to load textures and setup energy/loadout

		loadAnimation(MY_CHARACTER_SKELETON_ATLAS, MY_CHARACTER_SKELETON_JSON, 1.0F);

		AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
		e.setTime(e.getEndTime() * MathUtils.random());

		if ((ModHelper.enabledMods.size() > 0)
				&& ((ModHelper.isModEnabled("Diverse")) || (ModHelper.isModEnabled("Chimera")) || (ModHelper.isModEnabled("Blue Cards")))) {
			this.masterMaxOrbs = 1;
		}
	}

	@Override
	public ArrayList<String> getStartingDeck() {
		ArrayList<String> retVal = new ArrayList<>();

		retVal.add(Claw.ID);
		retVal.add(Dagger.ID);
		retVal.add(Sword.ID);
		retVal.add(Axe.ID);
		retVal.add(Hammer.ID);

		retVal.add(Shield.ID);
		retVal.add(PrepareToCombo.ID);
		retVal.add(BladeDance.ID);

		return retVal;
	}

	@Override
	public ArrayList<String> getStartingRelics() {
		ArrayList<String> retVal = new ArrayList<>();

		retVal.add("TheWarrior:UnnamedStartingRelic");
		UnlockTracker.markRelicAsSeen("TheWarrior:UnnamedStartingRelic");

		return retVal;
	}

	@Override
	public CharSelectInfo getLoadout() {
		return new CharSelectInfo(CHARACTER_NAME, FLAVOR_TEXT, STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, HAND_SIZE, this,
				getStartingRelics(), getStartingDeck(), false);
	}

	@Override
	public String getTitle(PlayerClass paramPlayerClass) {
		return CHARACTER_NAME;
	}

	@Override
	public Color getCardColor() {
		return Color.PINK; // TODO where does this color appear?
	}

	@Override
	public String getAchievementKey() {
		return ""; // TODO can I add a custom achievement?
	}

	@Override
	public ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> tmpPool) {
		AbstractCard card = null;
		for (Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
			card = (AbstractCard) c.getValue();
			if ((card.color == TheWarriorEnum.WARRIOR_GREY) && (card.rarity != AbstractCard.CardRarity.BASIC)
					&& ((!UnlockTracker.isCardLocked((String) c.getKey())) || (Settings.treatEverythingAsUnlocked()))) {
				tmpPool.add(card);
			}
		}
		if (ModHelper.isModEnabled("Red Cards")) {
			CardLibrary.addRedCards(tmpPool);
		}
		if (ModHelper.isModEnabled("Green Cards")) {
			CardLibrary.addGreenCards(tmpPool);
		}
		if (ModHelper.isModEnabled("Blue Cards")) {
			CardLibrary.addBlueCards(tmpPool);
		}
		if (ModHelper.isModEnabled("Colorless Cards")) {
			CardLibrary.addColorlessCards(tmpPool);
		}
		return tmpPool;
	}

	@Override
	public AbstractCard getStartCardForEvent() {
		return new PrepareToCombo();
	}

	@Override
	public Color getCardTrailColor() {
		return Color.YELLOW; // TODO where does this color appear?
	}

	@Override
	public String getLeaderboardCharacterName() {
		return ""; // TODO not sure if I can get modded character on leaderboard.
	}

	@Override
	public Texture getEnergyImage() {
		return ImageMaster.loadImage("images/cardui/energyGrayVFX.png");
	}

	@Override
	public int getAscensionMaxHPLoss() {
		return 10;
	}

	@Override
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontRed; // TODO idk what is this
	}

	@Override
	public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
		this.energyOrb.renderOrb(sb, enabled, current_x, current_y);
	}

	@Override
	public void updateOrb() {
		this.energyOrb.updateOrb();
	}

	@Override
	public String getSaveFilePath() {
		return WARRIOR_FILE;
	}

	@Override
	public Prefs getPrefs() {
		if (this.prefs == null) {
			TheWarriorMod.logger.error("prefs need to be initialized first!");
		}
		return this.prefs;
	}

	@Override
	public void loadPrefs() {
		if (this.prefs == null) {
			this.prefs = SaveHelper.getPrefs("THE_WARRIOR");
		}
	}

	@Override
	public CharStat getCharStat() {
		return this.charStat;
	}

	@Override
	public int getUnlockedCardCount() {
		// TODO BaseMod doesn't count the unlocked cards for me, and I didn't add any locked cards, so it's gotta be 75
		return 75;
	}

	@Override
	public int getSeenCardCount() {
		// TODO I have absolutely no way to get seen card count.
		return 0;
	}

	@Override
	public int getCardCount() {
		// TODO BaseMod.addcard method doesn't count the card numbers like CardLibrary did. So I have to write the number down myself.
		return 75;
	}

	@Override
	public boolean saveFileExists() {
		return SaveAndContinue.saveExistsAndNotCorrupted(this.chosenClass);
	}

	@Override
	public String getWinStreakKey() {
		// TODO not sure if this work or not
		return "win_streak_warrior";
	}

	@Override
	public String getLeaderboardWinStreakKey() {
		// TODO not sure if this will work
		return "WARRIOR_CONSECUTIVE_WINS";
	}

	@Override
	public void renderStatScreen(SpriteBatch sb, float screenX, float renderY) {
		StatsScreen.renderHeader(sb, CHARACTER_NAME, screenX, renderY); // TODO need test
		this.charStat.render(sb, screenX, renderY);
	}

	@Override
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.playA("ATTACK_IRON_3", MathUtils.random(-0.2F, 0.2F));
		CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
	}

	@Override
	public String getCustomModeCharacterButtonSoundKey() {
		return "ATTACK_IRON_3";
	}

	@Override
	public Texture getCustomModeCharacterButtonImage() {
		return ImageMaster.FILTER_IRONCLAD; // TODO add my own custom mode character button image
	}

	@Override
	public CharacterStrings getCharacterString() {
		CharacterStrings characterStrings = new CharacterStrings();
		characterStrings.NAMES = new String[] { CHARACTER_NAME };
		characterStrings.TEXT = new String[] { FLAVOR_TEXT };
		return characterStrings;
	}

	@Override
	public String getLocalizedCharacterName() {
		return CHARACTER_NAME;
	}

	@Override
	public void refreshCharStat() {
		this.charStat = new CharStat(this);
	}

	@Override
	public AbstractPlayer newInstance() {
		return new TheWarrior(this.name);
	}
}
