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
import thewarrior.actions.RunDependOnEnemyVulnerableAction;
import thewarrior.actions.RunDependOnEnemyWeakenedAction;
import thewarrior.powers.DazedPower;
import thewarrior.powers.DistractedPower;

public class SpecialAxe extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:SpecialAxe";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 2;

	public SpecialAxe() {
		super(ID, NAME, COST, DESCRIPTION, CARD_RARITY, CARD_TARGET, WeaponType.AXE);
		changePreviewCards(new Axe1(), new Axe2(), new Axe3());
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
		}
	}

	class Axe1 extends AbstractWarriorSubcard {
		private static final int SPEED = 25;
		private static final int DAMAGE = 7;
		private static final int UPGRADE_PLUS_DAMAGE = 2;

		public Axe1() {
			super(ID, AttackType.STRIKE, COST, EXTENDED_DESCRIPTION[2], CARD_RARITY, CARD_TARGET);

			this.baseDamage = DAMAGE;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeDamage(UPGRADE_PLUS_DAMAGE);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AttackType.STRIKE, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboActionManager.add(new RunDependOnEnemyVulnerableAction(m, () -> {
				AbstractDungeon.actionManager.addToTop(new DamageAction(m, new DamageInfo(p, this.damage + 6, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
				AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, p, new DazedPower(m, 13), 13));
			}, () -> {
				AbstractDungeon.actionManager.addToTop(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
				AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, p, new DazedPower(m, 7), 7));
			}));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Axe1();
		}

	}

	class Axe2 extends AbstractWarriorSubcard {
		private static final int SPEED = 30;
		private static final int DAMAGE = 9;
		private static final int UPGRADE_PLUS_DAMAGE = 3;

		public Axe2() {
			super(ID, AttackType.CHOP, COST, EXTENDED_DESCRIPTION[4], CARD_RARITY, CARD_TARGET);

			this.baseDamage = DAMAGE;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeDamage(UPGRADE_PLUS_DAMAGE);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AttackType.CHOP, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboActionManager.add(new RunDependOnEnemyVulnerableAction(m, () -> {
				AbstractDungeon.actionManager.addToTop(new DamageAction(m, new DamageInfo(p, this.damage + 6, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
				AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, p, new DazedPower(m, 18), 18));
			}, () -> {
				AbstractDungeon.actionManager.addToTop(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
				AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, p, new DazedPower(m, 11), 11));
			}));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Axe2();
		}

	}

	class Axe3 extends AbstractWarriorSubcard {
		private static final int SPEED = 30;
		private static final int MAGIC = 20;
		private static final int UPGRADE_MAGIC = 8;

		public Axe3() {
			super(ID, AttackType.DISARM, COST, EXTENDED_DESCRIPTION[6], CARD_RARITY, CARD_TARGET);

			this.magicNumber = this.baseMagicNumber = MAGIC;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeMagicNumber(UPGRADE_MAGIC);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AttackType.DISARM, m, p.hand));
			ComboAction.speed += SPEED;

			ComboAction.comboActionManager.add(new RunDependOnEnemyWeakenedAction(m, () -> {
				AbstractDungeon.actionManager
						.addToTop(new ApplyPowerAction(m, p, new DistractedPower(m, magicNumber + 10), magicNumber + 10));
			}, () -> {
				AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, p, new DistractedPower(m, magicNumber), magicNumber));
			}));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Axe3();
		}

	}

}
