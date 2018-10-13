package thewarrior.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DazedPower extends AbstractPower {
	public static final String POWER_ID = "TheWarrior:Dazed";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public DazedPower(AbstractMonster owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		if (amount > 100)
			amount = 100;
		updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/Dazed.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/DazedSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.DEBUFF;
	}

	@Override
	public float atDamageGive(float damage, DamageType type) {
		if (type == DamageType.NORMAL) {
			damage -= damage * amount / 100.0F;
		}
		return damage;
	}

	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		float tmp = 100 - amount;
		if (stackAmount > 100)
			stackAmount = 100;
		tmp *= (100 - stackAmount) / 100.0F;
		amount = 100 - MathUtils.floor(tmp);
	}
	
	@Override
	public void atEndOfRound() {
		if (amount == 1)
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
		else
			AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, amount / 2));
	}

	@Override
	public void updateDescription() {
		this.description = String.format(DESCRIPTIONS[0],
				new Object[] { Integer.valueOf(this.amount), Integer.valueOf(this.amount) });
	}
}
