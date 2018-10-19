package thewarrior.powers;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractWarriorPower extends AbstractPower {

	/**
	 * This method is called when a combo ends and played more than 1 card.
	 */
	public void onFinishCombo(AbstractMonster target) {};

	/**
	 * This method is called when a combo ends.
	 */
	public void onComboEnd() {};

}
