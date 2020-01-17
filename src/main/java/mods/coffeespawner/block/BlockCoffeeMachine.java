package mods.coffeespawner.block;

import mods.coffeespawner.CoffeeSpawner;
import mods.coffeespawner.tileentity.TileEntityCoffeeMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCoffeeMachine extends Block {

	private static final VoxelShape BOUNDING_BOX_NORMAL = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 15.0D, 13.0D);
	private static final VoxelShape BOUNDING_BOX_PAN = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 17.0D, 13.0D);
	private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final IntegerProperty MODELID = IntegerProperty.create("modelid", 0, 1);
	private final boolean isPanModel;

	public BlockCoffeeMachine(String name, boolean isPanModel) {
		super(Block.Properties.create(Material.EARTH).hardnessAndResistance(0.5F, 5.0F));
		this.isPanModel = isPanModel;
		this.setRegistryName(name);
		this.setDefaultState(this.stateContainer.getBaseState().with(MODELID, 0).with(FACING, Direction.NORTH));
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, MODELID);
	}

	@Override
	public ActionResultType func_225533_a_(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
		if (!world.isRemote && player != null) {
			TileEntityCoffeeMachine tile = getTile(world, pos);
			if (tile != null) {
				if (tile.hasMug()) {
					this.removeMug(world, pos, tile);
					InventoryHelper.spawnItemStack(world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new ItemStack(CoffeeSpawner.coffee));
				} else {
					player.sendMessage(new StringTextComponent(TextFormatting.DARK_AQUA + "Coffee spawns tomorrow."));
				}
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityCoffeeMachine();
	}

	public TileEntityCoffeeMachine getTile(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		return (tile instanceof TileEntityCoffeeMachine) ? (TileEntityCoffeeMachine) tile : null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selection) {
		return BOUNDING_BOX_NORMAL;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selection) {
		return (this.isPanModel) ? BOUNDING_BOX_PAN : BOUNDING_BOX_NORMAL;
	}

	public void spawnMug(World world, BlockPos pos, TileEntityCoffeeMachine tile) {
		if (world == null || pos == null || tile == null || tile.hasMug()) return;
		BlockState state = world.getBlockState(pos);
		if (state != null && state.getBlock() instanceof BlockCoffeeMachine) {
			world.setBlockState(pos, state.with(MODELID, 1));
			tile.setMug(true);
			tile.markDirty();
		}
	}

	public void removeMug(World world, BlockPos pos, TileEntityCoffeeMachine tile) {
		if (world == null || pos == null || tile == null || !tile.hasMug()) return;
		BlockState state = world.getBlockState(pos);
		if (state != null && state.getBlock() instanceof BlockCoffeeMachine) {
			world.setBlockState(pos, state.with(MODELID, 0));
			tile.setMug(false);
			tile.markDirty();
		}
	}

}
