package thewarrior.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class StrongerPower extends AbstractPower {
	public static final String POWER_ID = "TheWarrior:Stronger";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public StrongerPower(int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/Stronger.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/StrongerSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.BUFF;
	}

	@Override
	public void updateDescription() {
		if (amount == 1)
			this.description = String.format(DESCRIPTIONS[0]);
		else
			this.description = String.format(DESCRIPTIONS[1], new Object[] { Integer.valueOf(this.amount) });
	}
}
