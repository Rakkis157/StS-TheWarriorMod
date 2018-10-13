package thewarrior.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import basemod.abstracts.CustomCard;
import thewarrior.powers.DrawReductionPower;

public class Fatigue extends CustomCard {
	public static final String ID = "TheWarrior:Fatigue";
	public static final String IMG = "images/cards/Fatigue.png";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.STATUS;
	public static final CardColor CARD_COLOR = CardColor.COLORLESS;
	public static final CardRarity CARD_RARITY = CardRarity.COMMON;
	public static final CardTarget CARD_TARGET = CardTarget.NONE;
	private static final int COST = -2;

	public Fatigue() {
		super(ID, NAME, IMG, COST, DESCRIPTION, CARD_TYPE, CARD_COLOR, CARD_RARITY, CARD_TARGET);
	}

	@Override
	public void upgrade() {}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) { // every status card in the do like this
		if (p.hasRelic("Medical Kit")) {
			useMedicalKit(p);
		} else {
			AbstractDungeon.actionManager.addToBottom(new UseCardAction(this));
		}
	}

	@Override
	public void triggerWhenDrawn() { // every status card in the game has this thing
		if ((AbstractDungeon.player.hasPower("Evolve")) && (!AbstractDungeon.player.hasPower("No Draw"))) {
			AbstractDungeon.player.getPower("Evolve").flash();
			AbstractDungeon.actionManager
					.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.getPower("Evolve").amount));
		}
	}

	@Override
	public void triggerOnEndOfTurnForPlayingCard() {
		AbstractPlayer p = AbstractDungeon.player;
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DrawReductionPower(1), 1));
		this.retain = true;
	}

}
