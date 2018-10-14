package thewarrior.actions.unique;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class SpecialSwordSlashAction extends AbstractGameAction {
	private int damage;

	public SpecialSwordSlashAction(int damage) {
		setValues(null, AbstractDungeon.player, damage);
		this.damage = damage;
	}

	@Override
	public void update() {
		int[] multiDamage = new int[AbstractDungeon.getCurrRoom().monsters.monsters.size()];
		int i = 0;
		for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
			// give 3 Poison to all enemies
			AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(monster, AbstractDungeon.player, new PoisonPower(monster, AbstractDungeon.player, 3), 3));
			// deal 50% more if enemy has bleeding
			if (monster.hasPower("TheWarrior:Bleeding"))
				multiDamage[i] = MathUtils.floor(damage * 1.5F);
			else
				multiDamage[i] = damage;
			i++;
		}
		// deal damage to all enemies
		AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage, DamageType.NORMAL,
				AbstractGameAction.AttackEffect.SLASH_VERTICAL));

		isDone = true;
	}

}
