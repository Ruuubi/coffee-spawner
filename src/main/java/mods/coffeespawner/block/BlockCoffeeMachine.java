package mods.coffeespawner.block;

import javax.annotation.Nullable;

import mods.coffeespawner.CoffeeSpawner;
import mods.coffeespawner.tileentity.TileEntityCoffeeMachine;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockCoffeeMachine extends Block implements EntityBlock {

	private static final VoxelShape BOUNDING_BOX_NORMAL = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 15.0D, 13.0D);
	private static final VoxelShape BOUNDING_BOX_PAN = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 17.0D, 13.0D);
	private static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	private static final IntegerProperty MODELID = IntegerProperty.create("modelid", 0, 1);
	private final boolean isPanModel;

	public BlockCoffeeMachine(String name, boolean isPanModel) {
		super(Block.Properties.of(Material.DIRT).strength(0.5F, 5.0F));
		this.isPanModel = isPanModel;
		this.setRegistryName(name);
		this.registerDefaultState(this.stateDefinition.any().setValue(MODELID, 0).setValue(FACING, Direction.NORTH));
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, MODELID);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTrace) {
		if (!level.isClientSide() && player != null) {
			TileEntityCoffeeMachine tile = getTile(level, pos);
			if (tile != null) {
				if (tile.hasMug()) {
					this.removeMug(level, pos, tile);
					Containers.dropItemStack(level, player.position().x, player.position().y, player.position().z, new ItemStack(CoffeeSpawner.coffee));
				} else {
					player.sendMessage(new TextComponent(ChatFormatting.DARK_AQUA + "Coffee spawns tomorrow."), Util.NIL_UUID);
				}
			}
		}
		return InteractionResult.SUCCESS;
	}

	public TileEntityCoffeeMachine getTile(Level level, BlockPos pos) {
		BlockEntity tile = level.getBlockEntity(pos);
		return (tile instanceof TileEntityCoffeeMachine) ? (TileEntityCoffeeMachine) tile : null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selection) {
		return BOUNDING_BOX_NORMAL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selection) {
		return (this.isPanModel) ? BOUNDING_BOX_PAN : BOUNDING_BOX_NORMAL;
	}

	public void spawnMug(Level level, BlockPos pos, TileEntityCoffeeMachine tile) {
		if (level == null || pos == null || tile == null || tile.hasMug()) return;
		BlockState state = level.getBlockState(pos);
		if (state != null && state.getBlock() instanceof BlockCoffeeMachine) {
			level.setBlockAndUpdate(pos, state.setValue(MODELID, 1));
			tile.setMug(true);
			tile.setChanged();
		}
	}

	public void removeMug(Level level, BlockPos pos, TileEntityCoffeeMachine tile) {
		if (level == null || pos == null || tile == null || !tile.hasMug()) return;
		BlockState state = level.getBlockState(pos);
		if (state != null && state.getBlock() instanceof BlockCoffeeMachine) {
			level.setBlockAndUpdate(pos, state.setValue(MODELID, 0));
			tile.setMug(false);
			tile.setChanged();
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityCoffeeMachine(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) {
			return null;
		}
		return (level1, blockPos, blockState, t) -> {
			if (t instanceof TileEntityCoffeeMachine tile) {
				tile.serverTick();
			}
		};
	}

}
