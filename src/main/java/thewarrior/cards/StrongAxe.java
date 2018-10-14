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
import com.megacrit.cardcrawl.powers.VulnerablePower;

import thewarrior.actions.ComboAction;
import thewarrior.powers.BleedingPower;
import thewarrior.powers.DazedPower;
import thewarrior.powers.DistractedPower;

public class StrongAxe extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:StrongAxe";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 3;

	public StrongAxe() {
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
		private static final int SPEED = 50;
		private static final int DAMAGE = 14;
		private static final int UPGRADE_PLUS_DAMAGE = 4;

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
			ComboAction.comboActionManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new BleedingPower(p, m, 3), 3));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new DazedPower(m, 16), 16));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Axe1();
		}

	}

	class Axe2 extends AbstractWarriorSubcard {
		private static final int SPEED = 60;
		private static final int DAMAGE = 16;
		private static final int UPGRADE_PLUS_DAMAGE = 4;

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
			ComboAction.comboActionManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new VulnerablePower(m, 2, false), 2));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new DazedPower(m, 18), 18));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Axe2();
		}

	}

	class Axe3 extends AbstractWarriorSubcard {
		private static final int SPEED = 60;
		private static final int MAGIC = 33;
		private static final int UPGRADE_MAGIC = 12;

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
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new DistractedPower(m, magicNumber), magicNumber));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Axe3();
		}

	}

}
