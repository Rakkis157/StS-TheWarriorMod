package thewarrior.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BloodyCloudPower extends AbstractPower {
	public static final String POWER_ID = "TheWarrior:BloodyCloud";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public BloodyCloudPower(int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/BloodyCloud.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/BloodyCloudSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.BUFF;
	}

	@Override
	public void atStartOfTurnPostDraw() {
		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			flash();
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				if ((!m.isDead) && (!m.isDying)) {
					AbstractDungeon.actionManager
							.addToBottom(new ApplyPowerAction(m, this.owner, new BleedingPower(m, this.owner, this.amount), this.amount));
				}
			}
		}
	}

	@Override
	public void updateDescription() {
		this.description = String.format(DESCRIPTIONS[0], new Object[] { Integer.valueOf(this.amount) });
	}
}
