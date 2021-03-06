package thewarrior.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import thewarrior.powers.EnvenomPower;

public class Envenom extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:Envenom";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.SKILL;
	public static final CardRarity CARD_RARITY = CardRarity.COMMON;
	public static final CardTarget CARD_TARGET = CardTarget.NONE;

	private static final int COST = 1;
	private static final int MGC = 2;
	private static final int PLUS_MGC = 1;

	public Envenom() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);

		magicNumber = baseMagicNumber = MGC;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(PLUS_MGC);
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EnvenomPower(magicNumber), magicNumber));
	}

}
