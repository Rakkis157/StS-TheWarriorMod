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
import com.megacrit.cardcrawl.powers.PoisonPower;

import thewarrior.actions.ComboAction;
import thewarrior.powers.DistractedPower;

public class PoisonousClaw extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:PoisonousClaw";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.COMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 1;

	public PoisonousClaw() {
		super(ID, NAME, COST, DESCRIPTION, CARD_RARITY, CARD_TARGET, WeaponType.CLAW);
		changePreviewCards(new Claw1(), new Claw2());
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
		}
	}

	class Claw1 extends AbstractWarriorSubcard {
		private static final int SCRATCH_DAMAGE = 2;
		private static final int UPGRADE_PLUS_SCRATCH_DAMAGE = 2;

		public Claw1() {
			super(ID, AttackType.SCRATCH, COST, EXTENDED_DESCRIPTION[2], CARD_RARITY, CARD_TARGET);

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
			ComboAction.comboActionManager.add(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new PoisonPower(m, p, 2), 2));

		}

		@Override
		public AbstractCard makeCopy() {
			return new Claw1();
		}
	}

	class Claw2 extends AbstractWarriorSubcard {
		private static final int DISTRACTED_NUMBER = 1;
		// private static final int UPGRADE_PLUS_DISTRACTED_NUMBER = 4;

		public Claw2() {
			super(ID, AttackType.FEINT, COST, EXTENDED_DESCRIPTION[4], CARD_RARITY, CARD_TARGET);

			this.magicNumber = this.baseMagicNumber = DISTRACTED_NUMBER;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				// upgradeMagicNumber(UPGRADE_PLUS_DISTRACTED_NUMBER);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AbstractWarriorAttackCard.AttackType.FEINT, m, p.hand));
			ComboAction.comboActionManager.add(new ApplyPowerAction(m, p, new DistractedPower(m, magicNumber), magicNumber));

		}

		@Override
		public AbstractCard makeCopy() {
			return new Claw2();
		}
	}

}
