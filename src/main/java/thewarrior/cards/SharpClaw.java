package thewarrior.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import thewarrior.actions.ComboAction;
import thewarrior.powers.BleedingPower;
import thewarrior.powers.ComboPower;
import thewarrior.powers.DistractedPower;
import thewarrior.powers.GraspedPower;

public class SharpClaw extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:SharpClaw";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 1;

	public SharpClaw() {
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
		private static final int SPEED = 20;
		private static final int DMG = 2;
		private static final int PLUS_DMG = 1;
		private static final int GRASPED_AMOUNT = 2;
		private static final int UPGRADE_PLUS_GRASPED_AMOUNT = 1;

		public Claw1() {
			super(ID, AttackType.GRASP, COST, EXTENDED_DESCRIPTION[2], CARD_RARITY, CARD_TARGET);

			this.magicNumber = this.baseMagicNumber = GRASPED_AMOUNT;
			this.baseDamage = DMG;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				this.upgradeName();
				this.upgradeMagicNumber(UPGRADE_PLUS_GRASPED_AMOUNT);
				this.upgradeDamage(PLUS_DMG);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AbstractWarriorAttackCard.AttackType.GRASP, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboActionManager
					.add(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new GraspedPower(m, magicNumber), magicNumber));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new BleedingPower(m, p, 2), 2));
			ComboAction.comboActionManager.add(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Claw1();
		}
	}

	class Claw2 extends AbstractWarriorSubcard {
		private static final int SPEED = 20;
		private static final int SCRATCH_DAMAGE = 5;
		private static final int UPGRADE_PLUS_SCRATCH_DAMAGE = 2;

		public Claw2() {
			super(ID, AttackType.SCRATCH, COST, EXTENDED_DESCRIPTION[4], CARD_RARITY, CARD_TARGET);

			this.baseDamage = SCRATCH_DAMAGE;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				this.upgradeName();
				this.upgradeDamage(UPGRADE_PLUS_SCRATCH_DAMAGE);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AbstractWarriorAttackCard.AttackType.SCRATCH, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboActionManager.add(new DamageAction(p, new DamageInfo(p, 2), AttackEffect.SLASH_HORIZONTAL)); // take damage
			ComboAction.comboActionManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL)); // deal damage
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new BleedingPower(m, p, 1), 1)); // give bleeding
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ComboPower(20), 20)); // combo deal more
		}

		@Override
		public AbstractCard makeCopy() {
			return new Claw2();
		}
	}

	class Claw3 extends AbstractWarriorSubcard {
		private static final int SPEED = 30;
		private static final int DISTRACTED_NUMBER = 10;
		private static final int UPGRADE_PLUS_DISTRACTED_NUMBER = 5;

		public Claw3() {
			super(ID, AttackType.FEINT, COST, EXTENDED_DESCRIPTION[6], CARD_RARITY, CARD_TARGET);

			this.magicNumber = this.baseMagicNumber = DISTRACTED_NUMBER;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeMagicNumber(UPGRADE_PLUS_DISTRACTED_NUMBER);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AbstractWarriorAttackCard.AttackType.FEINT, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new DistractedPower(m, magicNumber), magicNumber));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Claw3();
		}
	}

}
