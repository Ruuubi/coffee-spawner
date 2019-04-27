package mods.coffeespawner.block;

import mods.coffeespawner.CoffeeSpawner;
import mods.coffeespawner.tileentity.TileEntityCoffeeMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCoffeeMachine extends Block {

	private static final VoxelShape BOUNDING_BOX_NORMAL = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 15.0D, 13.0D);
	private static final VoxelShape BOUNDING_BOX_PAN = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 17.0D, 13.0D);
	private static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
	private static final IntegerProperty MODELID = IntegerProperty.create("modelid", 0, 1);
	private final boolean isPanModel;

	public BlockCoffeeMachine(String name, boolean isPanModel) {
		super(Block.Properties.create(Material.GROUND).hardnessAndResistance(0.5F, 5.0F));
		this.isPanModel = isPanModel;
		this.setRegistryName(name);
		this.setDefaultState(this.stateContainer.getBaseState().with(MODELID, 0).with(FACING, EnumFacing.NORTH));
	}

	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(FACING, MODELID);
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && player != null) {
			TileEntityCoffeeMachine tile = getTile(world, pos);
			if (tile != null) {
				if (tile.hasMug()) {
					this.removeMug(world, pos, tile);
					InventoryHelper.spawnItemStack(world, player.posX, player.posY, player.posZ, new ItemStack(CoffeeSpawner.coffee));
				} else {
					player.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA + "Coffee spawns tomorrow."));
				}
			}
		}
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
		return new TileEntityCoffeeMachine();
	}

	public TileEntityCoffeeMachine getTile(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		return (tile instanceof TileEntityCoffeeMachine) ? (TileEntityCoffeeMachine) tile : null;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(IBlockState state, World world, BlockPos pos, IBlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Deprecated
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	public VoxelShape getCollisionShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return BOUNDING_BOX_NORMAL;
	}

	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return (this.isPanModel) ? BOUNDING_BOX_PAN : BOUNDING_BOX_NORMAL;
	}
	
	@Override
	public boolean isSolid(IBlockState state) {
		return true;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	public void spawnMug(World world, BlockPos pos, TileEntityCoffeeMachine tile) {
		if (world == null || pos == null || tile == null || tile.hasMug())
			return;
		IBlockState state = world.getBlockState(pos);
		if (state != null && state.getBlock() instanceof BlockCoffeeMachine) {
			world.setBlockState(pos, state.with(MODELID, 1));
			tile.setMug(true);
			tile.markDirty();
		}
	}

	public void removeMug(World world, BlockPos pos, TileEntityCoffeeMachine tile) {
		if (world == null || pos == null || tile == null || !tile.hasMug())
			return;
		IBlockState state = world.getBlockState(pos);
		if (state != null && state.getBlock() instanceof BlockCoffeeMachine) {
			world.setBlockState(pos, state.with(MODELID, 0));
			tile.setMug(false);
			tile.markDirty();
		}
	}

}
