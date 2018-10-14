package thewarrior.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import thewarrior.powers.BleedingPower;

public class IfPoisonGiveBleedingAction extends AbstractGameAction {
	private int amount;

	public IfPoisonGiveBleedingAction(AbstractMonster target, int amount) {
		setValues(target, AbstractDungeon.player, amount);
		actionType = ActionType.DEBUFF;
		attackEffect = AttackEffect.FIRE;

		this.amount = amount;
	}

	@Override
	public void update() {
		if (target.hasPower("Poison"))
			AbstractDungeon.actionManager
					.addToBottom(new ApplyPowerAction(target, source, new BleedingPower(target, source, amount), amount));
		this.isDone = true;
	}

}
