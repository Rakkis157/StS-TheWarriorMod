package thewarrior.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import thewarrior.actions.ComboAction;
import thewarrior.powers.ComboPower;
import thewarrior.powers.DistractedPower;

public class WeakeningClaw extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:WeakeningClaw";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.COMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 1;

	public WeakeningClaw() {
		super(ID, NAME, COST, DESCRIPTION, CARD_RARITY, CARD_TARGET, WeaponType.CLAW);
		changePreviewCards(new Claw1(), new Claw2(), new Claw3());
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
		}
	}

	class Claw1 extends AbstractWarriorSubcard {
		private static final int MGC = 25;
		private static final int PLUS_MGC = -5;

		public Claw1() {
			super(ID, AttackType.GRASP, COST, EXTENDED_DESCRIPTION[2], CARD_RARITY, CARD_TARGET);

			this.magicNumber = this.baseMagicNumber = MGC;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				this.upgradeName();
				this.upgradeMagicNumber(PLUS_MGC);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager
					.addToBottom(new ComboAction(AbstractWarriorAttackCard.AttackType.GRASP, m, p.hand));
			ComboAction.speed += magicNumber;
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Claw1();
		}
	}

	class Claw2 extends AbstractWarriorSubcard {
		private static final int SPEED = 15;
		private static final int DMG = 4;
		private static final int PLUS_DMG = 2;

		public Claw2() {
			super(ID, AttackType.SCRATCH, COST, EXTENDED_DESCRIPTION[4], CARD_RARITY, CARD_TARGET);

			this.baseDamage = DMG;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				this.upgradeName();
				this.upgradeDamage(PLUS_DMG);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager
					.addToBottom(new ComboAction(AbstractWarriorAttackCard.AttackType.SCRATCH, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboCardManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ComboPower(33), 33));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Claw2();
		}
	}

	class Claw3 extends AbstractWarriorSubcard {
		private static final int SPEED = 20;
		private static final int MGC = 13;
		private static final int PLUS_MGC = 6;

		public Claw3() {
			super(ID, AttackType.FEINT, COST, EXTENDED_DESCRIPTION[6], CARD_RARITY, CARD_TARGET);

			this.magicNumber = this.baseMagicNumber = MGC;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeMagicNumber(PLUS_MGC);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager
					.addToBottom(new ComboAction(AbstractWarriorAttackCard.AttackType.FEINT, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboCardManager
					.add(new ApplyPowerAction(m, p, new DistractedPower(m, magicNumber), magicNumber));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Claw3();
		}
	}

}
