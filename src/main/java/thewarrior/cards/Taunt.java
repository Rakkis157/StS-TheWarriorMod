package thewarrior.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Taunt extends AbstractWarriorCard {
	public static final String ID = "TheWarrior:Taunt";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	public static final CardType CARD_TYPE = CardType.SKILL;
	public static final CardRarity CARD_RARITY = CardRarity.COMMON;
	public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;
	private static final int COST = 1;
	private static final int NEW_COST = 0;

	public Taunt() {
		super(ID, NAME, COST, DESCRIPTION, CARD_TYPE, CARD_RARITY, CARD_TARGET);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(NEW_COST);
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int cnt = 0;
		for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (monster.hasPower("Weakened")) {
				int tmp = monster.getPower("Weakened").amount;
				cnt += tmp;
				AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(monster, p, "Weakened", tmp - 1));
				
			}
			if (monster.hasPower("Vulnerable")) {
				int tmp = monster.getPower("Vulnerable").amount;
				cnt += tmp;
				AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(monster, p, "Vulnerable", tmp - 1));
			}
		}
		if (cnt > 0) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, cnt), cnt));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LoseStrengthPower(p, cnt), cnt));
		}
	}

}
