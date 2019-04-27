package mods.coffeespawner.tileentity;

import mods.coffeespawner.CoffeeSpawner;
import mods.coffeespawner.block.BlockCoffeeMachine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityCoffeeMachine extends TileEntity implements ITickable {
	
	public TileEntityCoffeeMachine() {
		super(CoffeeSpawner.tile_coffee_machine);
	}

	private boolean mug = false;
	
	@Override
	public void read(NBTTagCompound nbt) {
        super.read(nbt);
    	this.mug = nbt.getBoolean("Mug");
    }
	
	@Override
	public NBTTagCompound write(NBTTagCompound nbt) {
		super.write(nbt);
		nbt.setBoolean("Mug", this.mug);
		return nbt;
	}
	
	@Override
	public void tick() {
		if (!getWorld().isRemote && this.getWorld().getDayTime() % 20L == 0L) {
			long time = this.getWorld().getDayTime() % 24000L;
			if (!mug && time == 40) {
				IBlockState state = this.getWorld().getBlockState(pos);
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
