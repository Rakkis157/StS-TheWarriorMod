package thewarrior.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import thewarrior.powers.StrongerPower;

public class Stronger extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:Stronger";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.POWER;
	public static final CardRarity CARD_RARITY = CardRarity.RARE;
	public static final CardTarget CARD_TARGET = CardTarget.SELF;

	private static final int COST = 1;

	public Stronger() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (!upgraded)
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrongerPower(1), 1));
		else
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrongerPower(2), 2));
	}
}
