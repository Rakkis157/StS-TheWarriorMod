package thewarrior.actions;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import basemod.abstracts.CustomCard;
import thewarrior.TheWarriorMod;
import thewarrior.cards.AbstractWarriorAttackCard;
import thewarrior.cards.SpecialHammer;
import thewarrior.cards.AbstractWarriorAttackCard.AttackType;
import thewarrior.cards.AbstractWarriorCard;
import thewarrior.powers.AbstractWarriorPower;
import thewarrior.powers.ComboPower;
import thewarrior.powers.DazedPower;
import thewarrior.powers.DoubleComboPower;

public class ComboAction extends AbstractGameAction {
	public static List<AbstractGameAction> comboActionManager = new ArrayList<>();
	public static AttackType lastAttackType = null;
	public static AttackType attackType = null;
	public static int cardPlayed = 0;
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
			boolean canCombo = false;

			// checking attack type of this card
			for (int i = 0; i < AttackType.ATTACK_NUM; i++) {
				AttackType thisAttackType = AttackType.getAttacktypeById(i);
				if (AbstractWarriorAttackCard.listAttackType.get(thisAttackType.toId()).contains(thiscard.cardID)) {
					attackTypeNum++;
					// check if this can attack type can combo
					if (AttackType.getCombotype.get(attacktype.toId()).contains(thisAttackType)) {
						AbstractCard tmpCard = AbstractWarriorAttackCard.getSubcard(thiscard.cardID, attackTypeNum);
						tmpCard.modifyCostForTurn(-1); // reduce sub card cost
						subCards.add(tmpCard);
						canCombo = true;
					} else {
						subCards.add(null);
					}
				}
			}

			if (canCombo && attackTypeNum == 3) { // there's no way attackTypeNum != 3
				this.add(thiscard, subCards, () -> { // add card to combo choices
					AbstractDungeon.actionManager.addToBottom(new ChooseAction(thiscard, subCards, target));
					lastPlayedCard = thiscard;
				});
			}
		}

		AbstractCard finishCombo = new FinishComboCard();
		choices.addToTop(finishCombo); // add finish combo card
		actions.add(() -> {
			AbstractDungeon.actionManager.addToBottom(new UseCardAction(finishCombo, target));
		});
	}

	private void add(AbstractCard baseCard, ArrayList<AbstractCard> subCards, Runnable action) {
		AbstractWarriorAttackCard choice = (AbstractWarriorAttackCard) baseCard.makeStatEquivalentCopy();
		choice.modifyCostForTurn(-1); // reduce card cost
		TheWarriorMod.logger.info("energy:" + EnergyPanel.getCurrentEnergy() + " " + choice.name + " cost: " + choice.costForTurn);
		if (!choice.canUse(AbstractDungeon.player, (AbstractMonster) target))
			return; // return if choice cannot use

		// change preview cards
		try {
			choice.changePreviewCards(subCards.get(0), subCards.get(1), subCards.get(2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// size?
		int size = 0;
		for (AbstractCard c : subCards)
			if (c != null)
				size++;

		String description = size > 1 ? new String("Choose: ") : new String(); // change description
		int i = 0;
		for (AbstractCard c : subCards) {
			if (c == null)
				continue;
			if (i > 0) {
				description = description.substring(0, description.length() - 1);
				description += ", ";
				if (i == size - 1)
					description += "or ";
			}
			description += c.name + ".";
			i++;
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

class FinishComboCard extends CustomCard {
	FinishComboCard() {
		super(AbstractWarriorCard.tmpCardId, (ComboAction.cardPlayed > 1 ? "Finish Combo" : "Cancel"), "images/cards/Cancel.png", 0,
				(ComboAction.cardPlayed > 1 ? "Finish this combo." : "Cancel this combo."), CardType.SKILL, CardColor.COLORLESS,
				CardRarity.BASIC, CardTarget.NONE);
	}

	@Override
	public void upgrade() {}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		// COOOMMMMBBOOOOOOO
		for (AbstractGameAction action : ComboAction.comboActionManager) {
			AbstractDungeon.actionManager.addToBottom(action);
			// double combo action
			if (action.actionType == ActionType.DAMAGE) {
				DoubleComboPower.doubleComboAction.add(new DamageAction(action.target,
						new DamageInfo(p, MathUtils.floor(action.amount * .75F), DamageType.NORMAL), AttackEffect.FIRE));
			}
		}
		ComboAction.comboActionManager.clear();
		// you will get a bonus only if you combo 2 or more cards
		if (ComboAction.cardPlayed > 1) {
			// unnamed starting relic things
			if (p.hasRelic("TheWarrior:UnnamedStartingRelic"))
				AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
			// give dazed
			int dazeAmount = (int) Math.sqrt(ComboAction.cardPlayed * 5000.0F / ComboAction.speed);
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new DazedPower(m, dazeAmount), dazeAmount));
			// special hammer things
			if (ComboAction.lastPlayedCard instanceof SpecialHammer) {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, 2), 2));

				AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(ComboAction.lastPlayedCard, p.drawPile));
				AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(ComboAction.lastPlayedCard, p.hand));
				AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(ComboAction.lastPlayedCard, p.discardPile));
			}
			// call AbstractWarriorPower.onFinishCombo()
			for (AbstractPower power : p.powers) {
				if (power instanceof AbstractWarriorPower) {
					((AbstractWarriorPower) power).onFinishCombo(m);
				}
			}
		}
		// call AbstractWarriorPower.onComboEnd()
		for (AbstractPower power : p.powers) {
			if (power instanceof AbstractWarriorPower) {
				((AbstractWarriorPower) power).onComboEnd();
			}
		}
		// reset double combo power things
		DoubleComboPower.doubleComboAction.clear();
		// reset everything
		ComboAction.cardPlayed = 0;
		ComboAction.speed = 0;
		ComboAction.lastPlayedCard = null;
		ComboAction.attackType = null;
		TheWarriorMod.logger.info("Changed last combo played to null");
	}

	@Override
	public AbstractCard makeCopy() {
		return new FinishComboCard();
	}
}
