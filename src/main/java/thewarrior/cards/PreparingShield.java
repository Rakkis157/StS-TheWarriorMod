package thewarrior.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import thewarrior.actions.UseEnergyAction;
import thewarrior.powers.EnergizedPower;

public class PreparingShield extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:PreparingShield";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.SKILL;
	public static final CardRarity CARD_RARITY = CardRarity.RARE;
	public static final CardTarget CARD_TARGET = CardTarget.SELF;

	private static final int COST = -1;
	private static final int BLK = 4;
	private static final int PLUS_BLK = 2;

	public PreparingShield() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);

		baseBlock = BLK;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
			upgradeBlock(PLUS_BLK);
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (energyOnUse < EnergyPanel.totalCount) // calculate energy
			energyOnUse = EnergyPanel.totalCount;
		int effect = EnergyPanel.totalCount;
		if (energyOnUse != -1)
			effect = energyOnUse;
		if (p.hasRelic("Chemical X")) {
			effect += 2;
			p.getRelic("Chemical X").flash();
		}

		if (effect > 0) { // play card
			if (!freeToPlayOnce)
				AbstractDungeon.actionManager.addToBottom(new UseEnergyAction(EnergyPanel.totalCount));
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, effect * block));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, effect / 2), effect / 2));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EnergizedPower(effect / 2), effect / 2));
		}

		if (!p.hasPower("Entangled")) // cannot attack this turn
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EntanglePower(p), 1));
	}

}
