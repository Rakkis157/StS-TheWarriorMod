package thewarrior.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Charge extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:Charge";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.SKILL;
	public static final CardRarity CARD_RARITY = CardRarity.RARE;
	public static final CardTarget CARD_TARGET = CardTarget.NONE;
	private static final int COST = 0;

	public Charge() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);
		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (AbstractCard card : p.hand.group) {
			if (card.type == CardType.STATUS)
				AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(card, p.hand));
		}
		AbstractDungeon.actionManager.addToBottom(new DamageAction(p, new DamageInfo(p, 6, DamageType.NORMAL), AttackEffect.FIRE));
		if (!upgraded) {
			AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(2));
			AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 4));
		} else {
			AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(3));
			AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 6));
		}
		AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
	}

}
