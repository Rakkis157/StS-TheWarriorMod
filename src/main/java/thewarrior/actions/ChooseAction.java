package thewarrior.actions;

import java.util.ArrayList;
import java.util.List;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import thewarrior.TheWarriorMod;
import thewarrior.cards.AbstractWarriorAttackCard.AttackType;

public class ChooseAction extends AbstractGameAction {
	private CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
	private ArrayList<Runnable> actions = new ArrayList<>();
	private String message = "Choose:";

	public ChooseAction(List<AbstractCard> subCards, AbstractMonster target) {
		this.setValues(target, AbstractDungeon.player, 1);
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_FASTER;

		for (AbstractCard card : subCards) {
			this.add(card, () -> {
				ComboAction.lastAttackType = AttackType.valueOf(card.name.toUpperCase());
				card.use(AbstractDungeon.player, target);
			});
		}
	}

	// choose action when combo
	public ChooseAction(AbstractCard basecard, ArrayList<AbstractCard> subCards, AbstractMonster target) {
		this.setValues(target, AbstractDungeon.player, 1);
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_FASTER;

		for (AbstractCard card : subCards) {
			this.add(card, () -> {
				AbstractDungeon.actionManager
						.addToBottom(new PlayComboCardAction(target, basecard, AttackType.valueOf(card.name.toUpperCase())));
			});
		}
	}

	private void add(AbstractCard card, Runnable action) {
		AbstractCard choice = card.makeStatEquivalentCopy();

		if (target != null) {
			choice.calculateCardDamage((AbstractMonster) target);
		} else {
			choice.applyPowers();
		}
		choices.addToTop(choice);
		actions.add(action);
	}

	@Override
	public void update() {
		if (choices.isEmpty()) {
			this.tickDuration();
			this.isDone = true;
			return;
		}
		if (this.duration == Settings.ACTION_DUR_FASTER) {
			if (choices.size() > 1) {
				AbstractDungeon.gridSelectScreen.open(this.choices, 1, message, false, false, false, false);
				this.tickDuration();
				return;
			} else {
				actions.get(0).run();
				this.tickDuration();
				this.isDone = true;
				return;
			}
		}
		if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			AbstractCard pick = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
			AbstractDungeon.gridSelectScreen.selectedCards.clear();
			int i = choices.group.indexOf(pick);
			TheWarriorMod.logger.info("Choose action: picked option " + i);
			actions.get(i).run();
		}
		this.tickDuration();
	}
}
