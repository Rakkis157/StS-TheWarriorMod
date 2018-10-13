package thewarrior.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import thewarrior.actions.ComboAction;
import thewarrior.powers.ComboPower;
import thewarrior.powers.DazedPower;

public class SpecialSword extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:SpecialSword";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 2;

	public SpecialSword() {
		super(ID, NAME, COST, DESCRIPTION, CARD_RARITY, CARD_TARGET, WeaponType.SWORD);
		changePreviewCards(new Sword1(), new Sword2(), new Sword3());
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
		}
	}

	class Sword1 extends AbstractWarriorSubcard {
		private static final int SPEED = 20;
		private static final int THRUST_DAMAGE = 8;
		private static final int UPGRADE_PLUS_THRUST_DAMAGE = 3;

		public Sword1() {
			super(ID, AttackType.THRUST, COST, EXTENDED_DESCRIPTION[2], CARD_RARITY, CARD_TARGET);

			this.baseDamage = THRUST_DAMAGE;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeDamage(UPGRADE_PLUS_THRUST_DAMAGE);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AttackType.THRUST, m, p.hand));
			ComboAction.speed += SPEED;
			if (m.hasPower("TheWarrior:Bleeding"))
				ComboAction.comboCardManager.add(new DamageAction(m, new DamageInfo(p, (int) (this.damage * 1.5F), this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			else
				ComboAction.comboCardManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ComboPower(25), 25));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Sword1();
		}

	}

	class Sword2 extends AbstractWarriorSubcard {
		private static final int SPEED = 20;
		private static final int SLASH_DAMAGE = 10;
		private static final int UPGRADE_PLUS_SLASH_DAMAGE = 3;

		public Sword2() {
			super(ID, AttackType.SLASH, COST, EXTENDED_DESCRIPTION[4], CARD_RARITY, CARD_TARGET);

			this.baseDamage = SLASH_DAMAGE;
			this.isMultiDamage = true;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeDamage(UPGRADE_PLUS_SLASH_DAMAGE);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AttackType.SLASH, m, p.hand));
			ComboAction.speed += SPEED;
			// put 2 fatigue in draw pile
			ComboAction.comboCardManager.add(new MakeTempCardInDrawPileAction(new Fatigue(), 2, true, true));
			int i = 0;
			// give 3 Poison to all enemies
			for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
				ComboAction.comboCardManager.add(new ApplyPowerAction(monster, p, new PoisonPower(monster, p, 3), 3));
				// deal 50% more if enemy has bleeding
				if (monster.hasPower("TheWarrior:Bleeding"))
					this.multiDamage[i] = MathUtils.floor(damage * 1.5F);
				i++;
			}
			// deal damage to all enemies
			ComboAction.comboCardManager
					.add(new DamageAllEnemiesAction(m, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL));

		}

		@Override
		public AbstractCard makeCopy() {
			return new Sword2();
		}

	}

	class Sword3 extends AbstractWarriorSubcard {
		private static final int SPEED = 25;
		private static final int STRIKE_DAMAGE = 10;
		private static final int UPGRADE_PLUS_STRIKE_DAMAGE = 3;

		public Sword3() {
			super(ID, AttackType.STRIKE, COST, EXTENDED_DESCRIPTION[6], CARD_RARITY, CARD_TARGET);

			this.baseDamage = STRIKE_DAMAGE;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeDamage(UPGRADE_PLUS_STRIKE_DAMAGE);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AttackType.STRIKE, m, p.hand));
			ComboAction.speed += SPEED;
			if (m.hasPower("TheWarrior:Bleeding"))
				ComboAction.comboCardManager.add(new DamageAction(m, new DamageInfo(p, (int) (this.damage * 1.5F), this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			else
				ComboAction.comboCardManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new DazedPower(m, 10), 10));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Sword3();
		}

	}

}
