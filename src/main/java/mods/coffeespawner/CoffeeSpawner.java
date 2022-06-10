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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod("coffeespawner")
public class CoffeeSpawner {
	public static final String MODID = "coffeespawner";
	public static final String MODNAME = "Coffee Spawner";
	public static final String MODNAME_NOSPACE = "CoffeeSpawner";
	public static final Logger LOGGER = LogManager.getLogger();

	// Blocks
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final RegistryObject<Block> BLOCK_COFFEE_MACHINE = BLOCKS.register("coffee_machine", () -> new BlockCoffeeMachine(false));
	public static final RegistryObject<Block> BLOCK_COFFEE_MACHINE_PAN = BLOCKS.register("coffee_machine_pan", () -> new BlockCoffeeMachine(true));

	// Items
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final RegistryObject<Item> ITEM_COFFEE_MACHINE = ITEMS.register("coffee_machine", () -> new BlockItem(BLOCK_COFFEE_MACHINE.get(), new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));
	public static final RegistryObject<Item> ITEM_COFFEE_MACHINE_PAN = ITEMS.register("coffee_machine_pan", () -> new BlockItem(BLOCK_COFFEE_MACHINE_PAN.get(), new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));
	public static final RegistryObject<Item> ITEM_COFFEE = ITEMS.register("coffee", () -> new ItemCoffee(4, 0.625F));
	public static final RegistryObject<Item> ITEM_COFFEE_MILK = ITEMS.register("coffee_milk", () -> new ItemCoffee(5, 0.6F));
	public static final RegistryObject<Item> ITEM_COFFEE_SUGAR = ITEMS.register("coffee_sugar", () -> new ItemCoffee(5, 0.6F));
	public static final RegistryObject<Item> ITEM_COFFEE_MILK_SUGAR = ITEMS.register("coffee_milk_sugar", () -> new ItemCoffee(8, 0.5625F));

	// Block Entities
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
	public static final RegistryObject<BlockEntityType<TileEntityCoffeeMachine>> TILE_COFFEE_MACHINE = BLOCK_ENTITIES.register("tile_coffee_machine", () -> BlockEntityType.Builder.of(TileEntityCoffeeMachine::new, BLOCK_COFFEE_MACHINE.get(), BLOCK_COFFEE_MACHINE_PAN.get()).build(null));
		
	public CoffeeSpawner() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		BLOCK_ENTITIES.register(modEventBus);
	}

}