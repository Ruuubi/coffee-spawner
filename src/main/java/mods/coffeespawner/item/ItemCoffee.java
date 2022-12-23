package mods.coffeespawner.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;


public class ItemCoffee extends Item {

	public ItemCoffee(int nutrition, float saturation) {
		super(new Item.Properties().food(new FoodProperties.Builder().
				nutrition(nutrition).saturationMod(saturation).
				effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2), 1.0F).
				effect(new MobEffectInstance(MobEffects.JUMP, 100, 2), 1.0F).
				build()));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

}