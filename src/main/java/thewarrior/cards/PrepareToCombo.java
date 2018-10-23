package thewarrior.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import thewarrior.actions.ComboAction;

public class PrepareToCombo extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:PrepareToCombo";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.SKILL;
	public static final CardRarity CARD_RARITY = CardRarity.BASIC;
	public static final CardTarget CARD_TARGET = CardTarget.NONE;
	private static final int COST = 0;

	public PrepareToCombo() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);
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
		if (!upgraded)
			ComboAction.cardPlayed += 1;
		else
			ComboAction.cardPlayed += 2;
	}

}
