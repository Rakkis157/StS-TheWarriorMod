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
		changePreviewCards(new Axe1(), new Axe2());
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
		}
	}

	class Axe1 extends AbstractWarriorSubcard {
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

			ComboAction.comboActionManager.add(new RunDependOnEnemyVulnerableAction(m, () -> {
				AbstractDungeon.actionManager.addToTop(new DamageAction(m, new DamageInfo(p, this.damage + 6, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));

			}, () -> {
				AbstractDungeon.actionManager.addToTop(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_VERTICAL));

			}));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Axe1();
		}

	}

	class Axe2 extends AbstractWarriorSubcard {
		private static final int MAGIC = 2;
		private static final int UPGRADE_MAGIC = 1;

		public Axe2() {
			super(ID, AttackType.DISARM, COST, EXTENDED_DESCRIPTION[4], CARD_RARITY, CARD_TARGET);

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

			ComboAction.comboActionManager.add(new RunDependOnEnemyWeakenedAction(m, () -> {
				AbstractDungeon.actionManager
						.addToTop(new ApplyPowerAction(m, p, new DistractedPower(m, magicNumber + 1), magicNumber + 1));
			}, () -> {
				AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, p, new DistractedPower(m, magicNumber), magicNumber));
			}));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Axe2();
		}

	}

}
