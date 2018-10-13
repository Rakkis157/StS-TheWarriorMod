package thewarrior.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
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
import thewarrior.powers.DazedPower;
import thewarrior.powers.DistractedPower;

public class WoundingHammer extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:WoundingHammer";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.COMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 3;

	public WoundingHammer() {
		super(ID, NAME, COST, DESCRIPTION, CARD_RARITY, CARD_TARGET, WeaponType.HAMMER);
		changePreviewCards(new Hammer1(), new Hammer2(), new Hammer3());
	}

	@Override
	public AbstractCard makeCopy() {
		return new WoundingHammer();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
		}
	}

	class Hammer1 extends AbstractWarriorSubcard {
		private static final int SPEED = 60;
		private static final int MAGIC = 38;
		private static final int UPGRADE_MAGIC = 12;

		public Hammer1() {
			super(ID, AttackType.DISARM, COST, EXTENDED_DESCRIPTION[2], CARD_RARITY, CARD_TARGET);

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
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new DistractedPower(m, magicNumber), magicNumber));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Hammer1();
		}

	}

	class Hammer2 extends AbstractWarriorSubcard {
		private static final int SPEED = 80;
		private static final int DAMAGE = 14;
		private static final int UPGRADE_DAMAGE = 4;

		public Hammer2() {
			super(ID, AttackType.BLOW, COST, EXTENDED_DESCRIPTION[4], CARD_RARITY, CARD_TARGET);

			this.baseDamage = DAMAGE;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeDamage(UPGRADE_DAMAGE);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AttackType.BLOW, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboCardManager
					.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new DazedPower(m, 25), 25));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Hammer2();
		}

	}

	class Hammer3 extends AbstractWarriorSubcard {
		private static final int SPEED = 250;
		private static final int DMG = 17;
		private static final int UPGRADE_DMG = 5;

		public Hammer3() {
			super(ID, AttackType.HAMMER, COST, EXTENDED_DESCRIPTION[6], CARD_RARITY, CARD_TARGET);

			this.baseDamage = DMG;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeDamage(UPGRADE_DMG);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AttackType.HAMMER, m, p.hand));
			ComboAction.speed += SPEED;
			ComboAction.comboCardManager.add(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SMASH));
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new DazedPower(m, 33), 33));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Hammer3();
		}

	}

}
