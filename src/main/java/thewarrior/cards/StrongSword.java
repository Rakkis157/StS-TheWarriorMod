package thewarrior.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.DamageAllButOneEnemyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

import thewarrior.actions.ComboAction;
import thewarrior.powers.BleedingPower;
import thewarrior.powers.ComboPower;
import thewarrior.powers.DazedPower;

public class StrongSword extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:StrongSword";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 3;

	public StrongSword() {
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
		private static final int SPEED = 40;
		private static final int THRUST_DAMAGE = 12;
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
		private static final int SPEED = 40;
		private static final int SLASH_DAMAGE = 11;
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
			ComboAction.comboCardManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new BleedingPower(m, p, 3), 3));

			// deal half that damage to all enemies
			for (int i = 0; i < AbstractDungeon.getCurrRoom().monsters.monsters.size(); i++) {
		        multiDamage[i] = MathUtils.ceil(damage / 2.0F);
			}

			ComboAction.comboCardManager.add(new SFXAction("ATTACK_HEAVY"));
			ComboAction.comboCardManager.add(new VFXAction(p, new CleaveEffect(), 0.1F));
			ComboAction.comboCardManager.add(new DamageAllButOneEnemyAction(p, m, multiDamage, damageTypeForTurn, AttackEffect.NONE));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Sword2();
		}

	}

	class Sword3 extends AbstractWarriorSubcard {
		private static final int SPEED = 25;
		private static final int STRIKE_DAMAGE = 14;
		private static final int UPGRADE_PLUS_STRIKE_DAMAGE = 4;

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
			ComboAction.comboCardManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new WeakPower(m, 2, false), 2));
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new DazedPower(m, 16), 16));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Sword3();
		}

	}

}
