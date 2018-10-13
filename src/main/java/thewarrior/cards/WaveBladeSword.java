package thewarrior.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

import thewarrior.actions.ComboAction;
import thewarrior.powers.BleedingPower;
import thewarrior.powers.ComboPower;
import thewarrior.powers.DazedPower;

public class WaveBladeSword extends AbstractWarriorAttackCard {
	public static final String ID = "TheWarrior:WaveBladeSword";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardRarity CARD_RARITY = CardRarity.RARE;
	public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
	private static final int COST = 2;

	public WaveBladeSword() {
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
		private static final int SPEED = 25;
		private static final int THRUST_DAMAGE = 13;
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
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new BleedingPower(m, p, 5), 5));
			ComboAction.comboCardManager.add(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ComboPower(18), 18));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Sword1();
		}

	}

	class Sword2 extends AbstractWarriorSubcard {
		private static final int SPEED = 25;
		private static final int SLASH_DAMAGE = 10;
		private static final int UPGRADE_PLUS_SLASH_DAMAGE = 3;
		private static final int MGC = 4;
		private static final int PLUS_MGC = -1;

		public Sword2() {
			super(ID, AttackType.SLASH, COST, EXTENDED_DESCRIPTION[4], CARD_RARITY, CARD_TARGET);

			this.baseDamage = SLASH_DAMAGE;
			magicNumber = baseMagicNumber = MGC;
			this.isMultiDamage = true;
		}

		@Override
		public void upgrade() {
			if (!this.upgraded) {
				upgradeName();
				upgradeDamage(UPGRADE_PLUS_SLASH_DAMAGE);
				upgradeMagicNumber(PLUS_MGC);
			}
		}

		@Override
		public void use(AbstractPlayer p, AbstractMonster m) {
			AbstractDungeon.actionManager.addToBottom(new ComboAction(AttackType.SLASH, m, p.hand));
			// speed 25
			ComboAction.speed += SPEED;
			// take 4(3) damage
			ComboAction.comboCardManager
					.add(new DamageAction(p, new DamageInfo(p, magicNumber, DamageType.NORMAL), AttackEffect.SLASH_DIAGONAL));
			// deal 10(13) damage to all enemies
			ComboAction.comboCardManager.add(new SFXAction("ATTACK_HEAVY"));
			ComboAction.comboCardManager.add(new VFXAction(p, new CleaveEffect(), 0.1F));
			ComboAction.comboCardManager.add(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.NONE));
			// give 5 Bleeding to all enemies
			for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
				ComboAction.comboCardManager.add(new ApplyPowerAction(monster, p, new BleedingPower(monster, p, 5), 5));
			}
			// put a wound into your draw pile
			ComboAction.comboCardManager.add(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Sword2();
		}

	}

	class Sword3 extends AbstractWarriorSubcard {
		private static final int SPEED = 31;
		private static final int STRIKE_DAMAGE = 16;
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
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new DazedPower(m, 10), 10));
			ComboAction.comboCardManager.add(new ApplyPowerAction(m, p, new BleedingPower(m, p, 5), 5));
			ComboAction.comboCardManager.add(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
		}

		@Override
		public AbstractCard makeCopy() {
			return new Sword3();
		}

	}

}
