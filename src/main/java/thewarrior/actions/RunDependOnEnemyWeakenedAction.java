package thewarrior.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RunDependOnEnemyWeakenedAction extends AbstractGameAction {
	private Runnable actionIfWeakened;
	private Runnable actionIfNotWeakened;

	public RunDependOnEnemyWeakenedAction(AbstractMonster target, Runnable actionIfWeakened, Runnable actionIfNotWeakened) {
		setValues(target, AbstractDungeon.player);
		this.actionIfWeakened = actionIfWeakened;
		this.actionIfNotWeakened = actionIfNotWeakened;
	}

	@Override
	public void update() {
		if (target.hasPower("Weakened"))
			actionIfWeakened.run();
		else
			actionIfNotWeakened.run();
		this.isDone = true;
	}

}
