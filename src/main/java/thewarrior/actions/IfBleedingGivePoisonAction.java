package thewarrior.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class IfBleedingGivePoisonAction extends AbstractGameAction {
	private int amount;

	public IfBleedingGivePoisonAction(AbstractMonster target, int amount) {
		setValues(target, AbstractDungeon.player, amount);
		actionType = ActionType.DEBUFF;
		attackEffect = AttackEffect.POISON;

		this.amount = amount;
	}

	@Override
	public void update() {
		if (target.hasPower("TheWarrior:Bleeding"))
			AbstractDungeon.actionManager
					.addToBottom(new ApplyPowerAction(target, source, new PoisonPower(target, source, amount), amount));
		this.isDone = true;
	}

}
