package thewarrior.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import thewarrior.TheWarriorMod;
import thewarrior.actions.ComboAction;
import thewarrior.cards.AbstractWarriorAttackCard.AttackType;

public abstract class AbstractWarriorSubcard extends AbstractWarriorCard {

	public AbstractWarriorSubcard(String mainCardId, AttackType attackType, int cost, String rawDescription, CardRarity rarity,
			CardTarget target) {
		super(AbstractWarriorCard.tmpCardId, attackType.toString(),
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

	@Override
	public float calculateModifiedCardDamage(AbstractPlayer player, float tmp) {
		return tmp + fibonacci(ComboAction.cardPlayed);
	}

	private int fibonacci(int n) {
		int prev2 = 0, prev1 = 1, now = 1;
		for (int i = 2; i <= n; i++) {
			now = prev2 + prev1;
			prev2 = prev1;
			prev1 = now;
		}
		return now;
	}
}
