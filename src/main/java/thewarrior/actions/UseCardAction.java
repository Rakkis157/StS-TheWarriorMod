package thewarrior.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UseCardAction extends AbstractGameAction {
	private AbstractCard card;

	/**
	 * UseCardAction directly calls card.use() method.
	 */
	public UseCardAction(AbstractCard card, AbstractMonster m) {
		setValues(m, AbstractDungeon.player);
		this.card = card;
	}

	@Override
	public void update() {
		card.use(AbstractDungeon.player, (AbstractMonster) target);
		this.isDone = true;
	}

}
