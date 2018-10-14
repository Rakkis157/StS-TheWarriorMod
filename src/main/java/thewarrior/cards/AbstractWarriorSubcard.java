package thewarrior.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thewarrior.TheWarriorMod;
import thewarrior.cards.AbstractWarriorAttackCard.AttackType;

public abstract class AbstractWarriorSubcard extends AbstractWarriorCard {

	public AbstractWarriorSubcard(String mainCardId, AttackType attackType, int cost, String rawDescription, CardRarity rarity,
			CardTarget target) {
		super("TheWarrior:tmp", attackType.toString(),
				"images/cards/subcards/" + mainCardId.substring(new String(TheWarriorMod.MOD_ID + ':').length()) + attackType + ".png",
				cost, rawDescription, CardType.ATTACK, rarity, target);
	}

	@Override
	public abstract AbstractCard makeCopy();

	@Override
	protected void upgradeName() {
		this.timesUpgraded += 1;
		this.upgraded = true;
	}
}
