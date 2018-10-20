package thewarrior.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class SpecialShieldRetainCardsAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RetainCardsAction");
	public static final String[] TEXT = uiStrings.TEXT;

	public SpecialShieldRetainCardsAction(int amount) {
		setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
	}

	public void update() {
		if (this.duration == 0.5F) {
			AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, true, false, false, true);
			AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
			tickDuration();
			return;
		}
		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				if (!c.isEthereal) {
					c.retain = true;
					// lose 3 blocks each
					AbstractDungeon.actionManager.addToBottom(new LoseBlockAction(AbstractDungeon.player, AbstractDungeon.player, 3));
				}
				AbstractDungeon.player.hand.addToTop(c);
			}
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
		}
		tickDuration();
	}
}
