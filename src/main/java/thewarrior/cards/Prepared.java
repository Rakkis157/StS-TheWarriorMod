package thewarrior.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import thewarrior.actions.unique.PreparedDiscardAction;

public class Prepared extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:Prepared";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.SKILL;
	public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;
	public static final CardTarget CARD_TARGET = CardTarget.NONE;

	private static final int COST = 1;
	private static final int MGC = 2;
	private static final int PLUS_MGC = 1;

	public Prepared() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);

		magicNumber = baseMagicNumber = MGC;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(PLUS_MGC);
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (!upgraded) {
			AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
			AbstractDungeon.actionManager.addToBottom(new PreparedDiscardAction(p, p, 2, magicNumber));
		} else {
			AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 2));
			AbstractDungeon.actionManager.addToBottom(new PreparedDiscardAction(p, p, 3, magicNumber));
		}
	}

}
