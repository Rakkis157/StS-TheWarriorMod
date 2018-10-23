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
import thewarrior.powers.DazedPower;

public class SpikySword extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:SpikySword";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.COMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 2;

	public SpikySword() {
		super(ID, NAME, COST, DESCRIPTION, CARD_RARITY, CARD_TARGET, WeaponType.SWORD);
		changePreviewCards(new Sword1(), new Sword2());
	}

	@Override
	public AbstractCard makeCopy() {
		return new SpikySword();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
		}
	}

	class Sword1 extends AbstractWarriorSubcard {
		private static final int THRUST_DAMAGE = 6;
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
			
			ComboAction.comboActionManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new BleedingPower(m, p, 3), 3));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Sword1();
		}

	}

	class Sword2 extends AbstractWarriorSubcard {
		private static final int STRIKE_DAMAGE = 8;
		private static final int UPGRADE_PLUS_STRIKE_DAMAGE = 3;

		public Sword2() {
			super(ID, AttackType.STRIKE, COST, EXTENDED_DESCRIPTION[4], CARD_RARITY, CARD_TARGET);

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
			
			ComboAction.comboActionManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new DazedPower(m, 10), 10));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new BleedingPower(m, p, 3), 3));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Sword2();
		}

	}

}
