package thewarrior.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RunDependOnEnemyBleedingAction extends AbstractGameAction {
	private Runnable actionIfBleeding;
	private Runnable actionIfNotBleeding;

	public RunDependOnEnemyBleedingAction(AbstractMonster target, Runnable actionIfBleeding, Runnable actionIfNotBleeding) {
		setValues(target, AbstractDungeon.player);
		this.actionIfBleeding = actionIfBleeding;
		this.actionIfNotBleeding = actionIfNotBleeding;
	}

	@Override
	public void update() {
		if (target.hasPower("TheWarriorMod:Bleeding"))
			actionIfBleeding.run();
		else
			actionIfNotBleeding.run();
		this.isDone = true;
	}

}
