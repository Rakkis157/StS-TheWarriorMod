package thewarrior.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class ComboPower extends AbstractWarriorPower {
	public static final String POWER_ID = "TheWarrior:Combo";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public ComboPower(int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/Combo.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/ComboSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.BUFF;
	}

	@Override
	public float atDamageGive(float damage, DamageType type) {
		if (type == DamageType.NORMAL)
			damage *= (100 + amount) / 100.0F;
		return damage;
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		Float tmp = (100 + stackAmount) / 100.0F;
		amount = (int) (amount * tmp);
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(DESCRIPTIONS[0], new Object[] { Integer.valueOf(this.amount) });
	}
	
	@Override
	public void onComboEnd() {
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
	}
}
