package thewarrior.actions;

import java.util.ArrayList;
import java.util.List;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import basemod.abstracts.CustomCard;
import thewarrior.TheWarriorMod;
import thewarrior.cards.AbstractWarriorAttackCard;
import thewarrior.cards.SpecialHammer;
import thewarrior.cards.AbstractWarriorAttackCard.AttackType;
import thewarrior.cards.AbstractWarriorCard;
import thewarrior.powers.ComboPower;
import thewarrior.powers.DazedPower;
import thewarrior.powers.DoubleComboPower;

public class ComboAction extends AbstractGameAction {
	public static List<AbstractGameAction> comboActionManager = new ArrayList<>();
	public static AttackType lastAttackType = null;
	public static AttackType attackType = null;
	public static int cardPlayed = 1;
	public static int speed = 0;
	public static AbstractCard lastPlayedCard = null;

	CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
	ArrayList<Runnable> actions = new ArrayList<>();
	String message = "Choose a card to combo:";

	public ComboAction(AttackType attacktype, AbstractMonster target, CardGroup hand) {
		TheWarriorMod.logger.info("Creating new combo action for " + attacktype.toString());
		ArrayList<AbstractCard> handcard = hand.group;

		this.setValues(target, AbstractDungeon.player, 1);
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_FASTER;

		// counter
		cardPlayed++;
		lastAttackType = attacktype;
		// combo power things
		AbstractDungeon.actionManager
				.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ComboPower(25), 25));
		// stronger power things
		if (AbstractDungeon.player.hasPower("TheWarrior:Stronger")) {
			int amount = AbstractDungeon.player.getPower("TheWarrior:Stronger").amount;
			AbstractDungeon.actionManager
					.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ComboPower(amount), amount));
		}

		for (AbstractCard thiscard : handcard) {
			ArrayList<AbstractCard> subCards = new ArrayList<>();
			int attackTypeNum = 0;

			// checking attack type of this card
			for (AttackType thisAttackType : AttackType.values()) {
				if (AbstractWarriorAttackCard.listAttackType.get(AttackType.getId(thisAttackType)).contains(thiscard.cardID)) {
					attackTypeNum++;
					// check if this can attack type can combo
					if (AttackType.getCombotype.get(AttackType.getId(attacktype)).contains(thisAttackType)) {
						AbstractCard tmpCard = AbstractWarriorAttackCard.getSubcard(thiscard.cardID, attackTypeNum);
						tmpCard.modifyCostForTurn(-1); // reduce sub card cost
						subCards.add(tmpCard);
					}
				}
			}

			if (!subCards.isEmpty()) {
				this.add(thiscard, subCards, () -> { // add card to combo choices
					AbstractDungeon.actionManager.addToBottom(new ChooseAction(thiscard, subCards, target));
					lastPlayedCard = thiscard;
				});
			}
		}

		AbstractCard cancel = new CancelCard();
		choices.addToTop(cancel); // add cancel card
		actions.add(() -> {
			cancel.use(AbstractDungeon.player, target);
		});
	}

	private void add(AbstractCard baseCard, ArrayList<AbstractCard> subCards, Runnable action) {
		AbstractWarriorAttackCard choice = (AbstractWarriorAttackCard) baseCard.makeStatEquivalentCopy();
		choice.modifyCostForTurn(-1); // reduce card cost
		TheWarriorMod.logger.info("energy:" + EnergyPanel.getCurrentEnergy() + " " + choice.name + " cost: " + choice.costForTurn);
		if (!choice.canUse(AbstractDungeon.player, (AbstractMonster) target))
			return; // return if choice cannot use

		AbstractCard c1 = null, c2 = null, c3 = null; // change preview cards
		int size = subCards.size();
		if (size > 0) {
			c1 = subCards.get(0);
			if (size > 1) {
				c2 = subCards.get(1);
				if (size > 2) {
					c3 = subCards.get(2);
				}
			}
		}
		choice.changePreviewCards(c1, c2, c3);

		String description = size > 1 ? new String("Choose: ") : new String(); // change description
		for (int i = 0; i < size; i++) {
			if (i > 0) {
				description = description.substring(0, description.length() - 1);
				description += ", ";
				if (i == size - 1)
					description += "or ";
			}
			description += subCards.get(i).name + ".";
		}
		choice.rawDescription = description;
		choice.initializeDescription();

		if (target != null) { // calculate card damage
			choice.calculateCardDamage((AbstractMonster) target);
		} else {
			choice.applyPowers();
		}
		// add card to choices
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
			AbstractDungeon.gridSelectScreen.open(choices, 1, message, false, false, false, false);
			this.tickDuration();
			return;
		}
		if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			AbstractCard pick = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
			AbstractDungeon.gridSelectScreen.selectedCards.clear();
			int i = choices.group.indexOf(pick);
			TheWarriorMod.logger.info("Combo action: picked option " + (i + 1));
			actions.get(i).run();
		}
		this.tickDuration();
	}
}

class CancelCard extends CustomCard {
	CancelCard() {
		super(AbstractWarriorCard.tmpCardId, "Finish Combo", "images/cards/Cancel.png", 0, "Finish this combo.", CardType.SKILL,
				CardColor.COLORLESS, CardRarity.BASIC, CardTarget.NONE);
	}

	@Override
	public void upgrade() {}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (ComboAction.cardPlayed > 1) { // you will get a bonus only if you combo 2 or more cards
			// unnamed starting relic things
			if (AbstractDungeon.player.hasRelic("TheWarrior:UnnamedStartingRelic"))
				AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
			// give dazed
			int dazeAmount = (int) Math.sqrt(ComboAction.cardPlayed * 5000.0F / ComboAction.speed);
			AbstractDungeon.actionManager
					.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new DazedPower(m, dazeAmount), dazeAmount));
			// combo form power things
			if (AbstractDungeon.player.hasPower("TheWarrior:ComboForm")) {
				AbstractDungeon.player.getPower("TheWarrior:ComboForm").flash();
				int amount = AbstractDungeon.player.getPower("TheWarrior:ComboForm").amount;
				AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amount));
				AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, amount));
			}
			// special hammer things
			if (ComboAction.lastPlayedCard == new SpecialHammer()) {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
						new StrengthPower(AbstractDungeon.player, 2), 2));

				AbstractDungeon.actionManager
						.addToBottom(new ExhaustSpecificCardAction(ComboAction.lastPlayedCard, AbstractDungeon.player.drawPile));
				AbstractDungeon.actionManager
						.addToBottom(new ExhaustSpecificCardAction(ComboAction.lastPlayedCard, AbstractDungeon.player.hand));
				AbstractDungeon.actionManager
						.addToBottom(new ExhaustSpecificCardAction(ComboAction.lastPlayedCard, AbstractDungeon.player.discardPile));
			}
			// double combo power things
			if (!DoubleComboPower.doubleComboAction.isEmpty()) {
				AbstractDungeon.player.getPower("TheWarrior:DoubleCombo").flash();
				for (AbstractGameAction action : DoubleComboPower.doubleComboAction)
					AbstractDungeon.actionManager.addToBottom(action);
				int newDazeAmount = (int) Math.sqrt(ComboAction.cardPlayed * 5000.0F / (ComboAction.speed / 2));
				AbstractDungeon.actionManager
						.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new DazedPower(m, newDazeAmount), newDazeAmount));
			}
		}
		// COOOMMMMBBOOOOOOO
		for (AbstractGameAction action : ComboAction.comboActionManager)
			AbstractDungeon.actionManager.addToBottom(action);
		ComboAction.comboActionManager.clear();
		// remove combo power
		AbstractDungeon.actionManager
				.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, "TheWarrior:Combo"));
		// reset double combo power things
		AbstractDungeon.actionManager
				.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, "TheWarrior:DoubleCombo", 1));
		DoubleComboPower.doubleComboAction.clear();
		// reset everything
		ComboAction.cardPlayed = 1;
		ComboAction.speed = 0;
		ComboAction.lastPlayedCard = null;
		ComboAction.attackType = null;
		TheWarriorMod.logger.info("Changed last combo played to null");
	}

	@Override
	public AbstractCard makeCopy() {
		return new CancelCard();
	}
}
