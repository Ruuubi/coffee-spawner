package mods.coffeespawner.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemCoffee extends ItemFood {

	private final String TOOLTIP;

	public ItemCoffee(String name, int heal, float saturation, String tooltip) {
		super(heal, saturation, false, new Item.Properties().group(ItemGroup.FOOD));
		this.setRegistryName(name);
		this.TOOLTIP = tooltip;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<ITextComponent> list, ITooltipFlag advanced) {
		if (TOOLTIP != null) list.add(new TextComponentString(TOOLTIP));
	}

	@Override
	public EnumAction getUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			player.addPotionEffect(new PotionEffect(Potion.getPotionById(1), 5 * 20, 2));
			player.addPotionEffect(new PotionEffect(Potion.getPotionById(8), 5 * 20, 2));
		}
	}

}