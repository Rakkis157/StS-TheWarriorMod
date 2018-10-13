package thewarrior.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WoundPain extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:WoundPain";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.SKILL;
	public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;

	private static final int COST = 2;
	private static final int UPGRADE_COST = 1;

	public WoundPain() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);
		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(UPGRADE_COST);
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
			int poison = 0, bleeding = 0;
			if (monster.hasPower("Poison")) {
				poison = monster.getPower("Poison").amount;
				AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(monster, p, "Poison"));
			}
			if (monster.hasPower("TheWarrior:Bleeding")) {
				bleeding = monster.getPower("TheWarrior:Bleeding").amount;
				AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(monster, p, "TheWarrior:Bleeding"));
			}
			if (poison * bleeding > 0)
				AbstractDungeon.actionManager.addToBottom(
						new DamageAction(monster, new DamageInfo(monster, poison * bleeding, DamageType.HP_LOSS), AttackEffect.FIRE));
		}
	}

}
