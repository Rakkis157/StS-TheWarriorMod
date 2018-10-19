package thewarrior.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ComboFormPower extends AbstractWarriorPower {
	public static final String POWER_ID = "TheWarrior:ComboForm";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public ComboFormPower(int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		updateDescription();

		this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/ComboForm.png"), 0, 0, 84, 84);
		this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/ComboFormSmall.png"), 0, 0, 32, 32);

		this.type = PowerType.BUFF;
	}

	@Override
	public void updateDescription() {
		this.description = String.format(DESCRIPTIONS[0], new Object[] { Integer.valueOf(this.amount), Integer.valueOf(this.amount) });
	}

	@Override
	public void onFinishCombo(AbstractMonster target) {
		this.flash();
		AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amount));
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(owner, amount));
	}
}
