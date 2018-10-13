package thewarrior.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

// I don't understand why there isn't a action that uses your energy in the game...
// All X cost cards uses custom actions instead of a use energy action
public class UseEnergyAction extends AbstractGameAction {

	private int amount;

	public UseEnergyAction(int amount) {
		this.amount = amount;
	}

	@Override
	public void update() {
		AbstractDungeon.player.energy.use(amount);
		this.isDone = true;
	}

}
