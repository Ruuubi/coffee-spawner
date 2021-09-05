package mods.coffeespawner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mods.coffeespawner.block.BlockCoffeeMachine;
import mods.coffeespawner.item.ItemCoffee;
import mods.coffeespawner.tileentity.TileEntityCoffeeMachine;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod("coffeespawner")
public class CoffeeSpawner {
	public static final String MODID = "coffeespawner";
	public static final String MODNAME = "Coffee Spawner";
	public static final String MODNAME_NOSPACE = "CoffeeSpawner";
	public static final Logger LOGGER = LogManager.getLogger();

	public static BlockCoffeeMachine coffee_machine;
	public static BlockCoffeeMachine coffee_machine_pan;
	public static ItemCoffee coffee;
	public static ItemCoffee coffee_milk;
	public static ItemCoffee coffee_sugar;
	public static ItemCoffee coffee_milk_sugar;
	public static BlockEntityType<TileEntityCoffeeMachine> tile_coffee_machine;

	public CoffeeSpawner() {}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			IForgeRegistry<Block> registry = event.getRegistry();
			registry.register(coffee_machine = new BlockCoffeeMachine("coffee_machine", false));
			registry.register(coffee_machine_pan = new BlockCoffeeMachine("coffee_machine_pan", true));
		}

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			IForgeRegistry<Item> registry = event.getRegistry();
			registry.register(new BlockItem(coffee_machine, new Item.Properties().tab(CreativeModeTab.TAB_FOOD)).setRegistryName("coffee_machine"));
			registry.register(new BlockItem(coffee_machine_pan, new Item.Properties().tab(CreativeModeTab.TAB_FOOD)).setRegistryName("coffee_machine_pan"));
			registry.register(coffee = new ItemCoffee("coffee", 4, 0.625F));
			registry.register(coffee_milk = new ItemCoffee("coffee_milk", 5, 0.6F));
			registry.register(coffee_sugar = new ItemCoffee("coffee_sugar", 5, 0.6F));
			registry.register(coffee_milk_sugar = new ItemCoffee("coffee_milk_sugar", 8, 0.5625F));
		}

		@SuppressWarnings("unchecked")
		@SubscribeEvent
		public static void registerTileEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
			event.getRegistry().register(tile_coffee_machine = (BlockEntityType<TileEntityCoffeeMachine>) BlockEntityType.Builder.of(TileEntityCoffeeMachine::new, coffee_machine, coffee_machine_pan).build(null).setRegistryName("tile_coffee_machine"));
		}
	}
}