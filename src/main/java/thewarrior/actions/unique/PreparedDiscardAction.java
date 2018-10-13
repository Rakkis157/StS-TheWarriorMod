package thewarrior.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import thewarrior.powers.BleedingPower;

public class PreparedDiscardAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	public static int numDiscarded;
	private static final float DURATION = Settings.ACTION_DUR_XFAST;
	private int poisonAmt = 0;

	public PreparedDiscardAction(AbstractCreature target, AbstractCreature source, int amount, int poisonAmt) {
		this.p = ((AbstractPlayer) target);
		setValues(target, source, amount);
		this.actionType = AbstractGameAction.ActionType.DISCARD;
		this.duration = DURATION;
		this.poisonAmt = poisonAmt;
	}

	public void update() {
		int statusCardCount = 0;

		int i;
		if (this.duration == DURATION) {
			if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
				this.isDone = true;
				return;
			}
			if (this.p.hand.size() <= this.amount) {
				this.amount = this.p.hand.size();
				int tmp = this.p.hand.size();
				for (i = 0; i < tmp; i++) {
					AbstractCard c = this.p.hand.getTopCard();
					this.p.hand.moveToDiscardPile(c);

					/* Count status card discarded. */
					if (c.type == CardType.STATUS)
						statusCardCount++;
					/* Count status card discarded. */

					c.triggerOnManualDiscard();
					GameActionManager.incrementDiscard(false);
				}

				/* Give all enemy 2 Bleeding and !M! Poison for each status card discarded. */
				for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
					AbstractDungeon.actionManager.addToBottom(
							new ApplyPowerAction(monster, p, new BleedingPower(monster, p, 2 * statusCardCount), 2 * statusCardCount));
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, p,
							new PoisonPower(monster, p, poisonAmt * statusCardCount), poisonAmt * statusCardCount));
				}
				/* Give all enemy 2 Bleeding and !M! Poison for each status card discarded. */

				AbstractDungeon.player.hand.applyPowers();
				tickDuration();
				return;
			}
			if (this.amount < 0) {
				AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);
				AbstractDungeon.player.hand.applyPowers();
				tickDuration();
				return;
			}
			numDiscarded = this.amount;
			if (this.p.hand.size() > this.amount) {
				AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false);
			}
			AbstractDungeon.player.hand.applyPowers();
			tickDuration();
			return;
		}
		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				this.p.hand.moveToDiscardPile(c);

				/* Count status card discarded. */
				if (c.type == CardType.STATUS)
					statusCardCount++;
				/* Count status card discarded. */

				c.triggerOnManualDiscard();
				GameActionManager.incrementDiscard(false);
			}

			/* Give all enemy 2 Bleeding and !M! Poison for each status card discarded. */
			for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
				AbstractDungeon.actionManager.addToBottom(
						new ApplyPowerAction(monster, p, new BleedingPower(monster, p, 2 * statusCardCount), 2 * statusCardCount));
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, p,
						new PoisonPower(monster, p, poisonAmt * statusCardCount), poisonAmt * statusCardCount));
			}
			/* Give all enemy 2 Bleeding and !M! Poison for each status card discarded. */

			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
		}
		tickDuration();
	}
}
