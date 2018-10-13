package thewarrior.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class ThrowAwayDiscardAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	public static int numDiscarded;
	private static final float DURATION = Settings.ACTION_DUR_XFAST;

	public ThrowAwayDiscardAction(AbstractCreature target, AbstractCreature source, int amount) {
		this.p = ((AbstractPlayer) target);
		setValues(target, source, amount);
		this.actionType = AbstractGameAction.ActionType.DISCARD;
		this.duration = DURATION;
	}

	public void update() {
		int cardCount = 0;
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

					/* Count card discarded. */
					cardCount++;
					if (c.type == CardType.STATUS)
						statusCardCount++;
					/* Count card discarded. */

					c.triggerOnManualDiscard();
					GameActionManager.incrementDiscard(false);
				}

				/* Add a Dazed to your draw pile for each. */
				AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), cardCount, true, true));
				/* If you discarded at least 3, gain 1 energy and draw 1 card. */
				if (cardCount >= 3) {
					AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
					AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
				}
				/* If you discarded at least 3 status cards, gain 1 Dexterity. */
				if (statusCardCount >= 3) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
				}

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
				if (c.type == CardType.STATUS)
					statusCardCount++;
				/* Count card discarded. */

				c.triggerOnManualDiscard();
				GameActionManager.incrementDiscard(false);
			}

			/* Add a Dazed to your draw pile for each. */
			AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), cardCount, true, true));
			/* If you discarded at least 3, gain 1 energy and draw 1 card. */
			if (cardCount >= 3) {
				AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
				AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
			}
			/* If you discarded at least 3 status cards, gain 1 Dexterity. */
			if (statusCardCount >= 3) {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
			}

			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
		}
		tickDuration();
	}
}
