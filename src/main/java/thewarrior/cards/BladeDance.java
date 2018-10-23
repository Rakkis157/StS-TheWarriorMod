package thewarrior.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import thewarrior.powers.DistractedPower;

public class BladeDance extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:BladeDance";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.SKILL;
	public static final CardRarity CARD_RARITY = CardRarity.BASIC;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 1;
	private static final int UPGRADE_COST = 0;

	public BladeDance() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(UPGRADE_COST);
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new DistractedPower(m, 1), 1));
	}

}
