package thewarrior.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ScrapShieldDiscardAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	public static int numDiscarded;
	private static final float DURATION = Settings.ACTION_DUR_XFAST;
	private int blockAmt = 0;

	public ScrapShieldDiscardAction(AbstractCreature target, AbstractCreature source, int amount, int blockAmt) {
		this.p = ((AbstractPlayer) target);
		setValues(target, source, amount);
		this.actionType = AbstractGameAction.ActionType.DISCARD;
		this.duration = DURATION;
		this.blockAmt = blockAmt;
	}

	public void update() {
		int cardCount = 0;

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

					/* Count card discarded. */
					cardCount++;
					/* Count card discarded. */

					c.triggerOnManualDiscard();
					GameActionManager.incrementDiscard(false);
				}

				/* gain !B! block */
				AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, blockAmt * cardCount));
				/* gain !B! block */

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

				/* Count card discarded. */
				cardCount++;
				/* Count card discarded. */

				c.triggerOnManualDiscard();
				GameActionManager.incrementDiscard(false);
			}

			/* gain !B! block */
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, blockAmt * cardCount));
			/* gain !B! block */

			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
		}
		tickDuration();
	}
}
