package thewarrior.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.helpers.BaseModCardTags;
import thewarrior.powers.ComboFormPower;

public class ComboForm extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:ComboForm";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.POWER;
	public static final CardRarity CARD_RARITY = CardRarity.RARE;
	public static final CardTarget CARD_TARGET = CardTarget.SELF;

	private static final int COST = 3;
	private static final int UPGRADE_COST = 2;

	public ComboForm() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);

		tags.add(BaseModCardTags.FORM);
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
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ComboFormPower(1), 1));
	}

}
