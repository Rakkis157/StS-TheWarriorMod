package thewarrior.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class BloodExcitementPower extends AbstractPower {
	public static final String POWER_ID = "TheWarrior:BloodExcitement";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public BloodExcitementPower(int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/BloodExcitement.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/BloodExcitementSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.BUFF;
	}

	@Override
	public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
		if (power.ID == "TheWarrior:Bleeding") {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, source, new StrengthPower(owner, amount), amount));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, source, new LoseStrengthPower(owner, amount), amount));
		}
	}

	@Override
	public void updateDescription() {
		this.description = String.format(DESCRIPTIONS[0], new Object[] { Integer.valueOf(this.amount) });
	}
}
