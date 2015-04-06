package com.minemaarten.advancedmod.tileentity;

import cofh.api.energy.IEnergyProvider;
import com.minemaarten.advancedmod.utility.Log;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntitiyGenerator extends TileEntityAdvancedMod implements IInventory, IEnergyProvider{
    public static final int ENERGY_PER_TICK = 40;

    private ItemStack[] GeneratorItemStacks = new ItemStack[1];
    private int getMaxEnergyStore = 40000;
    private int extractEnergy = 40;
    private int getEnergyStore;
    private int burnTime;
    private int totalBurnTime = 50;
    @Override
    public void writeToNBT(NBTTagCompound tag){
        super.writeToNBT(tag);
        tag.setInteger("getMaxEnergyStore", getMaxEnergyStore);
        tag.setInteger("getEnergyStore", getEnergyStore);
        tag.setInteger("extractEnergy", extractEnergy);
        tag.setInteger("burnTime", burnTime);
        tag.setInteger("totalBurnTime", totalBurnTime);

        NBTTagList GeneratorItemStackstag = new NBTTagList();
        for(int i = 0; i < GeneratorItemStacks.length; i++) {
            ItemStack stack = GeneratorItemStacks[i];
            if(stack != null) {
                NBTTagCompound t = new NBTTagCompound();
                stack.writeToNBT(t);
                t.setByte("index", (byte)i);
                GeneratorItemStackstag.appendTag(t);
            }
        }
        tag.setTag("GeneratorItemStacks", GeneratorItemStackstag);
        Log.info(tag);
   }
    @Override
    public void readFromNBT(NBTTagCompound tag){
        super.readFromNBT(tag);
        getMaxEnergyStore = tag.getInteger("getMaxEnergyStore");
        getEnergyStore = tag.getInteger("getEnergyStore");
        extractEnergy = tag.getInteger("extractEnergy");
        burnTime = tag.getInteger("burnTime");
        totalBurnTime = tag.getInteger("totalBurnTime");
        Log.info(tag);

        GeneratorItemStacks = new ItemStack[1];
        NBTTagList GeneratorItemStackstag = tag.getTagList("GeneratorItemStacks", 10);

        for(int i = 0; i < GeneratorItemStackstag.tagCount(); i++) {
            NBTTagCompound t = GeneratorItemStackstag.getCompoundTagAt(i);
            int index = t.getByte("index");
            if(index >= 0 && index < GeneratorItemStacks.length) {
                GeneratorItemStacks[index] = ItemStack.loadItemStackFromNBT(t);
            }
        }
    }
    @Override
    public void writeToPacket(ByteBuf buf){
        for(ItemStack stack : GeneratorItemStacks)
            ByteBufUtils.writeItemStack(buf, stack);



    }

    @Override
    public void readFromPacket(ByteBuf buf){
        for(int i = 0; i < GeneratorItemStacks.length; i++)
            GeneratorItemStacks[i] = ByteBufUtils.readItemStack(buf);

    }
    @Override
    public int getSizeInventory() {
        return GeneratorItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return GeneratorItemStacks[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int decreaseAmount){
        if(GeneratorItemStacks[slot] != null) {
            ItemStack itemstack;

            if(GeneratorItemStacks[slot].stackSize <= decreaseAmount) {
                itemstack = GeneratorItemStacks[slot];
                setInventorySlotContents(slot, null);
                markDirty();
                return itemstack;
            } else {
                itemstack = GeneratorItemStacks[slot].splitStack(decreaseAmount);

                if(GeneratorItemStacks[slot].stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }

                markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }
    @Override
    public ItemStack getStackInSlotOnClosing(int slot){
        if(GeneratorItemStacks[slot] != null) {
            ItemStack itemstack = GeneratorItemStacks[slot];
            GeneratorItemStacks[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack){
        GeneratorItemStacks[slot] = stack;

        if(stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return GeneratorItemStacks.length;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return TileEntityFurnace.isItemFuel(itemStack);
    }
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (burnTime == 0) {
                if (getEnergyStore <= getMaxEnergyStore)
                { this.burnTime = this.totalBurnTime = TileEntityFurnace.getItemBurnTime(this.GeneratorItemStacks[0]);
                    if (this.GeneratorItemStacks[0] != null)
                    {
                        --this.GeneratorItemStacks[0].stackSize;

                        if (this.GeneratorItemStacks[0].stackSize == 0)
                        {
                            this.GeneratorItemStacks[0] = GeneratorItemStacks[0].getItem().getContainerItem(GeneratorItemStacks[0]);
                        }
                    }
                }
            }
            if (burnTime > 0){
                getEnergyStore ++;
                burnTime --;

            }
        }
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return extractEnergy;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return getEnergyStore;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return getMaxEnergyStore;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return false;
    }
}
