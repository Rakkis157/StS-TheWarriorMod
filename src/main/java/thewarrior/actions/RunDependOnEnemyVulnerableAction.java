package thewarrior.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RunDependOnEnemyVulnerableAction extends AbstractGameAction {
	private Runnable actionIfVulnerable;
	private Runnable actionIfNotVulnerable;

	public RunDependOnEnemyVulnerableAction(AbstractMonster target, Runnable actionIfVulnerable, Runnable actionIfNotVulnerable) {
		setValues(target, AbstractDungeon.player);
		this.actionIfVulnerable = actionIfVulnerable;
		this.actionIfNotVulnerable = actionIfNotVulnerable;
	}

	@Override
	public void update() {
		if (target.hasPower("Vulnerable"))
			actionIfVulnerable.run();
		else
			actionIfNotVulnerable.run();
		this.isDone = true;
	}

}
