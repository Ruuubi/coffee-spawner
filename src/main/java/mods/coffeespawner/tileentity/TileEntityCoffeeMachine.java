package mods.coffeespawner.tileentity;

import mods.coffeespawner.CoffeeSpawner;
import mods.coffeespawner.block.BlockCoffeeMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCoffeeMachine extends TileEntity implements ITickableTileEntity {

	public TileEntityCoffeeMachine() {
		super(CoffeeSpawner.tile_coffee_machine);
	}

	private boolean mug = false;

	@Override
	public void read(CompoundNBT nbt) {
		super.read(nbt);
		this.mug = nbt.getBoolean("Mug");
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		super.write(nbt);
		nbt.putBoolean("Mug", this.mug);
		return nbt;
	}

	@Override
	public void tick() {
		if (!getWorld().isRemote && this.getWorld().getDayTime() % 20L == 0L) {
			long time = this.getWorld().getDayTime() % 24000L;
			if (!mug && time == 40) {
				BlockState state = this.getWorld().getBlockState(pos);
				if (state != null && state.getBlock() instanceof BlockCoffeeMachine) {
					Block block = state.getBlock();
					BlockCoffeeMachine cs = (BlockCoffeeMachine) block;
					cs.spawnMug(this.getWorld(), pos, this);
				}
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
