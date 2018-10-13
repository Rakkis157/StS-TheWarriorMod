package thewarrior.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import thewarrior.TheWarriorMod;
import thewarrior.cards.AbstractWarriorAttackCard.AttackType;

public class PlayComboCardAction extends AbstractGameAction {

	private AbstractCard card;
	private AttackType attackType;

	public PlayComboCardAction(AbstractCreature target, AbstractCard card, AttackType aType) {
		this.duration = Settings.ACTION_DUR_FAST;
		this.actionType = com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType.WAIT;
		this.source = AbstractDungeon.player;
		this.target = target;
		this.card = card;
		card.modifyCostForTurn(-1);
		this.attackType = aType;
	}

	@Override
	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			AbstractDungeon.player.limbo.group.add(card);
			card.current_y = (-200.0F * Settings.scale);
			card.target_x = (Settings.WIDTH / 2.0F + 200.0F * Settings.scale);
			card.target_y = (Settings.HEIGHT / 2.0F);
			card.targetAngle = 0.0F;
			card.lighten(false);
			card.drawScale = 0.12F;
			card.targetDrawScale = 0.75F;

			card.applyPowers();
			ComboAction.attackType = attackType;
			TheWarriorMod.logger.info("Changed combo type to " + attackType.toString());
			AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, this.target));
			AbstractDungeon.actionManager.addToBottom(new UnlimboAction(card));
			if (!Settings.FAST_MODE) {
				AbstractDungeon.actionManager.addToBottom(new WaitAction(Settings.ACTION_DUR_MED));
			} else {
				AbstractDungeon.actionManager.addToBottom(new WaitAction(Settings.ACTION_DUR_FASTER));
			}
			AbstractDungeon.player.hand.group.remove(card);

			this.isDone = true;
		}
	}

}
