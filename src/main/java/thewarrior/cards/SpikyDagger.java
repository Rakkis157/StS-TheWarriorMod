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

import thewarrior.actions.ComboAction;
import thewarrior.powers.BleedingPower;
import thewarrior.powers.ComboPower;
import thewarrior.powers.DistractedPower;

public class SpikyDagger extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:SpikyDagger";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.COMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 1;

	public SpikyDagger() {
		super(ID, NAME, COST, DESCRIPTION, CARD_RARITY, CARD_TARGET, WeaponType.DAGGER);
		changePreviewCards(new Dagger1(), new Dagger2(), new Dagger3());
	}

	@Override
	public AbstractCard makeCopy() {
		return new SpikyDagger();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
		}
	}

	class Dagger1 extends AbstractWarriorSubcard {
		private static final int SPEED = 20;
		private static final int DISTRACTED_NUMBER = 12;
		private static final int UPGRADE_PLUS_DISTRACTED_NUMBER = 6;

		public Dagger1() {
			super(ID, AttackType.FEINT, COST, EXTENDED_DESCRIPTION[2], CARD_RARITY, CARD_TARGET);

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
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new BleedingPower(m, p, 2), 2));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Dagger1();
		}
	}

	class Dagger2 extends AbstractWarriorSubcard {
		private static final int SPEED = 20;
		private static final int CUT_DAMAGE = 3;
		private static final int UPGRADE_PLUS_CUT_DAMAGE = 2;

		public Dagger2() {
			super(ID, AttackType.CUT, COST, EXTENDED_DESCRIPTION[4], CARD_RARITY, CARD_TARGET);

			this.baseDamage = CUT_DAMAGE;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeMagicNumber(UPGRADE_PLUS_CUT_DAMAGE);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AbstractWarriorAttackCard.AttackType.CUT, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboActionManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new BleedingPower(m, p, 2), 2));
		}

		@Override
		public float calculateModifiedCardDamage(AbstractPlayer player, float tmp) {
			if (ComboAction.lastAttackType == AttackType.CUT)
				tmp *= 1.2F;
			return tmp;
		}

		@Override
		public AbstractCard makeCopy() {
			return new Dagger2();
		}
	}

	class Dagger3 extends AbstractWarriorSubcard {
		private static final int SPEED = 20;
		private static final int THRUST_DAMAGE = 4;
		private static final int UPGRADE_PLUS_THRUST_DAMAGE = 2;

		public Dagger3() {
			super(ID, AttackType.THRUST, COST, EXTENDED_DESCRIPTION[6], CARD_RARITY, CARD_TARGET);

			this.baseDamage = THRUST_DAMAGE;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeMagicNumber(UPGRADE_PLUS_THRUST_DAMAGE);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AbstractWarriorAttackCard.AttackType.THRUST, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboActionManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new BleedingPower(m, p, 2), 2));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ComboPower(20), 20));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Dagger3();
		}
	}

}
