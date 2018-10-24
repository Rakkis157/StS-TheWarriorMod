package thewarrior.patches;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import thewarrior.TheWarriorMod;
import thewarrior.cards.AbstractWarriorCard;
import thewarrior.cards.attack.AbstractWarriorAttackCard;
import thewarrior.cards.attack.AbstractWarriorAttackCard.AttackType;
import thewarrior.cards.attack.AbstractWarriorAttackCard.WeaponType;
import thewarrior.characters.TheWarriorEnum;

public class WarriorAttackCardRenderPatch {
	@SpirePatch(clz = TipHelper.class, method = "render")
	public static class TipHelperPatch {
		public static void Prefix(SpriteBatch sb) {
			// I'm too lazy so I just try-catch the entire thing :)
			try {
				// check if render is needed
				Field field = null;
				field = TipHelper.class.getDeclaredField("renderedTipThisFrame");
				field.setAccessible(true);
				boolean renderedTipThisFrame = field.getBoolean(null);
				if (!Settings.hidePopupDetails && renderedTipThisFrame) {
					// get everything we need
					field = TipHelper.class.getDeclaredField("isCard");
					field.setAccessible(true);
					boolean isCard = field.getBoolean(null);
					field = TipHelper.class.getDeclaredField("card");
					field.setAccessible(true);
					AbstractCard card = (AbstractCard) field.get(null);
					// check cards
					if ((isCard) && (card != null)) {
						// only render warrior attack card
						if (card.color != TheWarriorEnum.WARRIOR_GREY || card.type != CardType.ATTACK)
							return;
						if (card.cardID == AbstractWarriorCard.tmpCardId)
							return;
						if (AbstractWarriorAttackCard.cardToPreview.isEmpty()) {
							TheWarriorMod.logger.info("Why is cardToPreview empty?");
							return;
						}
						// card need to be unlocked and seen
						if (!card.isSeen || card.isLocked)
							return;
						// get rid of stupid keywords
						field = TipHelper.class.getDeclaredField("KEYWORDS");
						field.setAccessible(true);
						Object object = field.get(null);
						object.getClass().getDeclaredMethod("clear", new Class[] {}).invoke(object);
						// variables
						float tmpScale = 0.8F;
						final float CARD_TIP_PAD = 12.0F * Settings.scale;
						final float BOX_EDGE_H = 32.0F * Settings.scale;
						final float BOX_W = 320.0F * Settings.scale;
						int i, size = 0;
						for (AbstractCard c : AbstractWarriorAttackCard.cardToPreview)
							if (c != null)
								size++;
						// locate where to render those cards
						float x = card.current_x;
						if (card.current_x > Settings.WIDTH * 0.75F) {
							x += ((AbstractCard.IMG_WIDTH / 2.0F) + CARD_TIP_PAD) * card.drawScale
									+ (AbstractCard.IMG_WIDTH * tmpScale / 2.0F);
							if (x + AbstractCard.IMG_WIDTH / 2.0F * tmpScale > Settings.WIDTH)
								x = Settings.WIDTH - AbstractCard.IMG_WIDTH / 2.0F * tmpScale;
							// render combo type tip
							renderComboTypeTip(card.current_x - AbstractCard.IMG_WIDTH / 2.0F - CARD_TIP_PAD - BOX_W,
									card.current_y + AbstractCard.IMG_HEIGHT / 2.0F - BOX_EDGE_H, sb, card);
						} else {
							x -= ((AbstractCard.IMG_WIDTH / 2.0F) + CARD_TIP_PAD) * card.drawScale
									+ (AbstractCard.IMG_WIDTH * tmpScale / 2.0F);
							if (x - AbstractCard.IMG_WIDTH / 2.0F * tmpScale < 0)
								x = 0 + AbstractCard.IMG_WIDTH / 2.0F * tmpScale;
							// render combo type tip
							renderComboTypeTip(card.current_x + AbstractCard.IMG_WIDTH / 2.0F + CARD_TIP_PAD,
									card.current_y + AbstractCard.IMG_HEIGHT / 2.0F - BOX_EDGE_H, sb, card);
						}
						float y = Settings.HEIGHT / 2.0F - AbstractCard.IMG_HEIGHT / 2.0F * (size - 1) * tmpScale - CARD_TIP_PAD;
						for (i = 2 - 1; i >= 0; i--) {
							if (AbstractWarriorAttackCard.cardToPreview.get(i) == null)
								continue;
							AbstractWarriorAttackCard.cardToPreview.get(i).current_y = y;
							y += AbstractCard.IMG_HEIGHT * tmpScale;
							AbstractWarriorAttackCard.cardToPreview.get(i).current_x = x;
						}
						// render cards
						for (AbstractCard c : AbstractWarriorAttackCard.cardToPreview) {
							if (c == null)
								continue;
							if (SingleCardViewPopup.isViewingUpgrade) {
								AbstractCard copy = c.makeStatEquivalentCopy();
								copy.current_x = c.current_x;
								copy.current_y = c.current_y;
								copy.drawScale = tmpScale;
								copy.upgrade();
								copy.displayUpgrades();
								copy.render(sb);
							} else {
								c.drawScale = tmpScale;
								c.render(sb);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void renderComboTypeTip(float x, float y, SpriteBatch sb, AbstractCard card) {
		float textHeight;
		final float BOX_EDGE_H = 32.0F * Settings.scale;
		final float BODY_TEXT_WIDTH = 280.0F * Settings.scale;
		final float TIP_DESC_LINE_SPACING = 26.0F * Settings.scale;

		// What weapon type is this card?
		int weaponType = 0;
		for (ArrayList<String> list : AbstractWarriorAttackCard.listWeaponType) {
			if (list.contains(card.cardID))
				break;
			weaponType++;
		}
		if (weaponType >= WeaponType.WEAPON_NUM) {
			TheWarriorMod.logger.info("Error: trying to render combo type tip for a card that is not a weapon of The Warrior");
			return;
		}
		// render combo tip box
		for (AttackType a : WeaponType.getAttacktypeByWeapontype.get(weaponType)) {
			if (!card.rawDescription.contains(a.toString()))
				continue;
			textHeight = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, AttackType.getCombotypeTip.get(AttackType.getId(a)),
					BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING) - 7.0F * Settings.scale;
			renderComboTipBox(sb, a, x, y, textHeight);
			y -= textHeight + BOX_EDGE_H * 3.15F;
		}
	}

	private static void renderComboTipBox(SpriteBatch sb, AttackType a, float x, float y, float textHeight) {
		float h = textHeight;

		final Color BASE_COLOR = new Color(0F, 0F, 0F, 1.0F);
		final float SHADOW_DIST_Y = 14.0F * Settings.scale;
		final float SHADOW_DIST_X = 9.0F * Settings.scale;
		final float BOX_EDGE_H = 32.0F * Settings.scale;
		final float BOX_BODY_H = 64.0F * Settings.scale;
		final float BOX_W = 320.0F * Settings.scale;
		final float TEXT_OFFSET_X = 22.0F * Settings.scale;
		final float HEADER_OFFSET_Y = 12.0F * Settings.scale;
		final float BODY_OFFSET_Y = -20.0F * Settings.scale;
		final float BODY_TEXT_WIDTH = 280.0F * Settings.scale;
		final float TIP_DESC_LINE_SPACING = 26.0F * Settings.scale;

		sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
		sb.draw(ImageMaster.loadImage("images/ui/tip/warriorTipTop.png"), x + SHADOW_DIST_X, y - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
		sb.draw(ImageMaster.loadImage("images/ui/tip/warriorTipMid.png"), x + SHADOW_DIST_X, y - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W,
				h + BOX_EDGE_H);
		sb.draw(ImageMaster.loadImage("images/ui/tip/warriorTipBot.png"), x + SHADOW_DIST_X, y - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W,
				BOX_EDGE_H);

		sb.setColor(Color.WHITE);
		sb.draw(ImageMaster.loadImage("images/ui/tip/warriorTipTop.png"), x, y, BOX_W, BOX_EDGE_H);
		sb.draw(ImageMaster.loadImage("images/ui/tip/warriorTipMid.png"), x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
		sb.draw(ImageMaster.loadImage("images/ui/tip/warriorTipBot.png"), x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);

		FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(a.toString()), x + TEXT_OFFSET_X,
				y + HEADER_OFFSET_Y, Settings.GOLD_COLOR);

		FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, AttackType.getCombotypeTip.get(AttackType.getId(a)), x + TEXT_OFFSET_X,
				y + BODY_OFFSET_Y, BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING, BASE_COLOR);
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "render")
	public static class SingleCardViewPatch {
		public static void Postfix(SingleCardViewPopup __instance, SpriteBatch sb) {
			// try catch the entire thing is so convenient :)
			try {
				// the card instance is all we need
				Field field = null;
				AbstractCard card = null;
				field = __instance.getClass().getDeclaredField("card");
				field.setAccessible(true);
				card = (AbstractCard) field.get(__instance);
				if (card == null) {
					TheWarriorMod.logger.info("SingleCardViewPatch: Why is the card null?");
					return;
				}
				// only render warrior attack card
				if (card.color != TheWarriorEnum.WARRIOR_GREY || card.type != CardType.ATTACK)
					return;
				List<AbstractCard> cardToPreview = new ArrayList<>();
				for (int i = 1; i <= 2; i++) {
					AbstractCard tmp = AbstractWarriorAttackCard.getSubcard(card.cardID, i);
					if (SingleCardViewPopup.isViewingUpgrade) {
						tmp.upgrade();
						tmp.displayUpgrades();
					}
					cardToPreview.add(tmp);
				}
				// if card is not seen or is locked, skip it
				if (!card.isSeen || card.isLocked)
					return;
				// get rid of stupid keywords
				card.keywords.clear();
				// variables
				float tmpScale = 0.8F;
				final float CARD_TIP_PAD = 12.0F * Settings.scale;
				int i, size = cardToPreview.size();
				// render combo type tip
				popupRenderComboTypeTip(card);
				// locate where to render those cards
				float x = Settings.WIDTH / 2.0F;
				x -= (250.0F + CARD_TIP_PAD) + (AbstractCard.IMG_WIDTH * tmpScale / 2.0F);
				x -= 160.0F * Settings.scale; // the width of "prevHb"
				if (x - AbstractCard.IMG_WIDTH / 2.0F * tmpScale < 0)
					x = 0 + AbstractCard.IMG_WIDTH / 2.0F * tmpScale;
				float y = Settings.HEIGHT / 2.0F - AbstractCard.IMG_HEIGHT * tmpScale - CARD_TIP_PAD;
				for (i = size - 1; i >= 0; i--) {
					cardToPreview.get(i).current_y = y;
					y += AbstractCard.IMG_HEIGHT * tmpScale;
					cardToPreview.get(i).current_x = x;
				}
				// render cards
				for (i = 0; i < size; i++) {
					cardToPreview.get(i).drawScale = tmpScale;
					cardToPreview.get(i).render(sb);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void popupRenderComboTypeTip(AbstractCard card) {
		ArrayList<PowerTip> t = new ArrayList<>();
		final String[] TEXT = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup").TEXT;
		if (card.isLocked) {
			t.add(new PowerTip(TEXT[4], (String) GameDictionary.keywords.get(TEXT[4].toLowerCase())));
		} else if (!card.isSeen) {
			t.add(new PowerTip(TEXT[5], (String) GameDictionary.keywords.get(TEXT[5].toLowerCase())));
		} else {
			// What weapon type is this card?
			int weaponType = 0;
			for (ArrayList<String> list : AbstractWarriorAttackCard.listWeaponType) {
				if (list.contains(card.cardID))
					break;
				weaponType++;
			}
			if (weaponType >= WeaponType.WEAPON_NUM) {
				TheWarriorMod.logger.info("Error: trying to render combo type tip for a card that is not a weapon of The Warrior");
				return;
			}
			for (AttackType a : WeaponType.getAttacktypeByWeapontype.get(weaponType)) {
				t.add(new PowerTip(a.toString(), AttackType.getCombotypeTip.get(AttackType.getId(a))));
			}
		}
		if (!t.isEmpty()) {
			TipHelper.queuePowerTips(1300.0F * Settings.scale, 440.0F * Settings.scale, t);
		}
	}
}
