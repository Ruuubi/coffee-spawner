package mods.coffeespawner.tileentity;

import mods.coffeespawner.CoffeeSpawner;
import mods.coffeespawner.block.BlockCoffeeMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCoffeeMachine extends BlockEntity {

	public TileEntityCoffeeMachine(BlockPos pos, BlockState state) {
		super(CoffeeSpawner.TILE_COFFEE_MACHINE.get(), pos, state);
	}

	private boolean mug = false;

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.mug = nbt.getBoolean("Mug");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.putBoolean("Mug", this.mug);
	}

	public void serverTick() {
		if (!mug && this.getLevel().getDayTime() % 24000L == 40) {
			BlockState state = this.getLevel().getBlockState(this.getBlockPos());
			if (state != null && state.getBlock() instanceof BlockCoffeeMachine) {
				Block block = state.getBlock();
				BlockCoffeeMachine cs = (BlockCoffeeMachine) block;
				cs.spawnMug(this.getLevel(), this.getBlockPos(), this);
			}
		}
	}

	public boolean hasMug() {
		return mug;
	}

	public void setMug(boolean state) {
		mug = state;
	}

}
