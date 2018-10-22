package thewarrior.characters;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.abstracts.CustomPlayer;
import thewarrior.cards.Axe;
import thewarrior.cards.BladeDance;
import thewarrior.cards.Claw;
import thewarrior.cards.Dagger;
import thewarrior.cards.Hammer;
import thewarrior.cards.PrepareToCombo;
import thewarrior.cards.Shield;
import thewarrior.cards.Sword;
import thewarrior.relics.Gladiatoria;

public class TheWarrior extends CustomPlayer {
	public static final String CHARACTER_NAME = "The Warrior";
	public static final String FLAVOR_TEXT = "Sorry, the mod creator don't know what to say here.";

	public static final int ENERGY_PER_TURN = 3;
	public static final int STARTING_HP = 150;
	public static final int MAX_HP = 150;
	public static final int STARTING_GOLD = 99;
	public static final int HAND_SIZE = 5;
	public static final int ORB_SLOTS = 0;

	public static final String[] ORB_TEXTURES = new String[] { "images/ui/energypanel/layer1.png", "images/ui/energypanel/layer2.png",
			"images/ui/energypanel/layer3.png", "images/ui/energypanel/layer4.png", "images/ui/energypanel/layer5.png",
			"images/ui/energypanel/layer6.png", "images/ui/energypanel/layer1d.png", "images/ui/energypanel/layer2d.png",
			"images/ui/energypanel/layer3d.png", "images/ui/energypanel/layer4d.png", "images/ui/energypanel/layer5d.png" };
	public static final String MY_CHARACTER_SHOULDER_2 = "images/char/shoulder2.png"; // campfire pose
	public static final String MY_CHARACTER_SHOULDER_1 = "images/char/shoulder1.png"; // another campfire pose
	public static final String MY_CHARACTER_CORPSE = "images/char/corpse.png"; // dead corpse
	public static final String MY_CHARACTER_SKELETON_ATLAS = "images/char/skeleton.atlas"; // spine animation atlas
	public static final String MY_CHARACTER_SKELETON_JSON = "images/char/skeleton.json"; // spine animation json

	public TheWarrior(String playerName) {
		super(playerName, TheWarriorEnum.THE_WARRIOR, ORB_TEXTURES, "images/ui/cardui/energyGrayVFX.png", (String) null, (String) null);

		initializeClass(null, MY_CHARACTER_SHOULDER_2, MY_CHARACTER_SHOULDER_1, MY_CHARACTER_CORPSE, getLoadout(), 20.0F, -10.0F, 220.0F,
				290.0F, new EnergyManager(ENERGY_PER_TURN));

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

		retVal.add(Gladiatoria.ID);
		UnlockTracker.markRelicAsSeen(Gladiatoria.ID);

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
	public CardColor getCardColor() {
		return TheWarriorEnum.WARRIOR_GREY;
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
		return Color.BLACK;
	}

	@Override
	public int getAscensionMaxHPLoss() {
		return 10;
	}

	@Override
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontRed;
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
	public String getLocalizedCharacterName() {
		return CHARACTER_NAME;
	}

	@Override
	public AbstractPlayer newInstance() {
		return new TheWarrior(this.name);
	}

	@Override
	public Color getCardRenderColor() {
		return Color.GRAY;
	}

	@Override
	public String getSpireHeartText() {
		return "NL You prepared all your weapons for the final combo...";
	}

	@Override
	public Color getSlashAttackColor() {
		return Color.BLACK;
	}

	@Override
	public AttackEffect[] getSpireHeartSlashEffect() {
		return new AttackEffect[] { AttackEffect.SLASH_HORIZONTAL, AttackEffect.SLASH_HORIZONTAL, AttackEffect.SLASH_HORIZONTAL,
				AttackEffect.SLASH_HEAVY, AttackEffect.SLASH_HEAVY, AttackEffect.BLUNT_LIGHT, AttackEffect.SMASH };
	}

	@Override
	public String getVampireText() {
		return "Navigating an unlit street, you come across several hooded figures in the midst of some dark ritual. "
				+ "As you approach, they turn to you in eerie unison. The tallest among them bares fanged teeth and extends a long, "
				+ "pale hand towards you. NL ~\"Join~ ~us~ ~brother,~ ~and~ ~feel~ ~the~ ~warmth~ ~of~ ~the~ ~Spire.\"~";
	}
}
