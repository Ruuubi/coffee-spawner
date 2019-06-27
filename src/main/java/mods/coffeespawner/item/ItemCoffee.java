package mods.coffeespawner.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class ItemCoffee extends Item {

	private final String TOOLTIP;

	public ItemCoffee(String name, int heal, float saturation, String tooltip) {
		super(new Item.Properties().group(ItemGroup.FOOD).food(
				new Food.Builder().hunger(heal).saturation(saturation)
				.effect(new EffectInstance(Effects.SPEED, 100, 2), 1.0F)
				.effect(new EffectInstance(Effects.JUMP_BOOST, 100, 2), 1.0F)
				.build()));
		this.setRegistryName(name);
		this.TOOLTIP = tooltip;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<ITextComponent> list, ITooltipFlag advanced) {
		if (TOOLTIP != null) list.add(new StringTextComponent(TOOLTIP));
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

}