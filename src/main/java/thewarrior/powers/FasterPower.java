// package thewarrior.powers;
//
// import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
// import com.badlogic.gdx.math.MathUtils;
// import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
// import com.megacrit.cardcrawl.core.CardCrawlGame;
// import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
// import com.megacrit.cardcrawl.helpers.ImageMaster;
// import com.megacrit.cardcrawl.localization.PowerStrings;
// import com.megacrit.cardcrawl.powers.AbstractPower;
//
// public class FasterPower extends AbstractPower {
// public static final String POWER_ID = "TheWarrior:Faster";
// private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
// public static final String NAME = powerStrings.NAME;
// public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
//
// public FasterPower(int amount) {
// this.name = NAME;
// this.ID = POWER_ID;
// this.owner = AbstractDungeon.player;
// this.amount = amount;
// if (amount > 99)
// amount = 99;
// updateDescription();
//
// this.region128 = new AtlasRegion(ImageMaster.loadImage("images/powers/Faster.png"), 0, 0, 84, 84);
// this.region48 = new AtlasRegion(ImageMaster.loadImage("images/powers/FasterSmall.png"), 0, 0, 32, 32);
//
// this.type = PowerType.BUFF;
// }
//
// @Override
// public void stackPower(int stackAmount) {
// this.fontScale = 8.0F;
// float tmp = 100 - amount;
// if (stackAmount > 100)
// stackAmount = 100;
// tmp *= (100 - stackAmount) / 100.0F;
// amount = 100 - MathUtils.floor(tmp);
// if (amount > 99)
// amount = 99;
// }
//
// @Override
// public void atStartOfTurn() {
// AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new FastPower(amount), amount));
// }
//
// @Override
// public void updateDescription() {
// this.description = String.format(DESCRIPTIONS[0], new Object[] { Integer.valueOf(this.amount) });
// }
// }
