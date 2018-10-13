package thewarrior.ui.panels.energyorb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;

public class EnergyOrbGray implements EnergyOrbInterface { // TODO All of the images are from ironclad. I'll create mine own later. 
	public static float fontScale = 1.0F;
	private static final float ORB_IMG_SCALE = 1.15F * Settings.scale;
	public static int totalCount = 0;
	private float angle5;
	private float angle4;
	private float angle3;
	private float angle2;
	private float angle1;

	@Override
	public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
		if (enabled) {
			sb.setColor(Color.WHITE);
			sb.draw(ImageMaster.ENERGY_RED_LAYER1, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, this.angle1, 0, 0, 128, 128, false, false);

			sb.draw(ImageMaster.ENERGY_RED_LAYER2, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, this.angle2, 0, 0, 128, 128, false, false);

			sb.draw(ImageMaster.ENERGY_RED_LAYER3, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, this.angle3, 0, 0, 128, 128, false, false);

			sb.draw(ImageMaster.ENERGY_RED_LAYER4, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, this.angle4, 0, 0, 128, 128, false, false);

			sb.draw(ImageMaster.ENERGY_RED_LAYER5, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, this.angle5, 0, 0, 128, 128, false, false);

			sb.draw(ImageMaster.ENERGY_RED_LAYER6, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
		} else {
			sb.setColor(Color.WHITE);
			sb.draw(ImageMaster.ENERGY_RED_LAYER1D, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, this.angle1, 0, 0, 128, 128, false, false);

			sb.draw(ImageMaster.ENERGY_RED_LAYER2D, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, this.angle2, 0, 0, 128, 128, false, false);

			sb.draw(ImageMaster.ENERGY_RED_LAYER3D, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, this.angle3, 0, 0, 128, 128, false, false);

			sb.draw(ImageMaster.ENERGY_RED_LAYER4D, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, this.angle4, 0, 0, 128, 128, false, false);

			sb.draw(ImageMaster.ENERGY_RED_LAYER5D, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, this.angle5, 0, 0, 128, 128, false, false);

			sb.draw(ImageMaster.ENERGY_RED_LAYER6, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE,
					ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
		}
	}

	@Override
	public void updateOrb() {
		if (totalCount == 0) {
			this.angle5 += Gdx.graphics.getDeltaTime() * -5.0F;
			this.angle4 += Gdx.graphics.getDeltaTime() * 5.0F;
			this.angle3 += Gdx.graphics.getDeltaTime() * -8.0F;
			this.angle2 += Gdx.graphics.getDeltaTime() * 8.0F;
			this.angle1 += Gdx.graphics.getDeltaTime() * 72.0F;
		} else {
			this.angle5 += Gdx.graphics.getDeltaTime() * -20.0F;
			this.angle4 += Gdx.graphics.getDeltaTime() * 20.0F;
			this.angle3 += Gdx.graphics.getDeltaTime() * -40.0F;
			this.angle2 += Gdx.graphics.getDeltaTime() * 40.0F;
			this.angle1 += Gdx.graphics.getDeltaTime() * 360.0F;
		}
	}

}
