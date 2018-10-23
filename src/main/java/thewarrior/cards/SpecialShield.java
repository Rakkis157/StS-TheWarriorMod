package thewarrior.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import thewarrior.actions.UseEnergyAction;
import thewarrior.actions.unique.SpecialShieldRetainCardsAction;

public class SpecialShield extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:SpecialShield";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.SKILL;
	public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;
	public static final CardTarget CARD_TARGET = CardTarget.SELF;

	private static final int COST = -1;
	private static final int BLK = 6;
	private static final int PLUS_BLK = 2;

	public SpecialShield() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);

		this.baseBlock = BLK;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
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
			if (!AbstractDungeon.player.hand.isEmpty() && (!AbstractDungeon.player.hasRelic("Runic Pyramid"))
					&& (!AbstractDungeon.player.hasPower("Equilibrium")))
				AbstractDungeon.actionManager.addToBottom(new SpecialShieldRetainCardsAction(effect));
		}

		if (!p.hasPower("Entangled")) // cannot attack this turn
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EntanglePower(p), 1));
	}

}
