package com.minemaarten.advancedmod.init;

import com.minemaarten.advancedmod.block.BlockGenerator;
import net.minecraft.block.Block;

import com.minemaarten.advancedmod.block.BlockCamoMine;
import com.minemaarten.advancedmod.block.BlockDutchFlag;
import com.minemaarten.advancedmod.reference.Reference;
import com.minemaarten.advancedmod.utility.Log;
import com.minemaarten.advancedmod.utility.Names;

import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks{
    public static final Block dutchFlag = new BlockDutchFlag();
    public static final Block camoMine = new BlockCamoMine();
    public static final Block Block_gen = new BlockGenerator();

    public static void init(){
        GameRegistry.registerBlock(dutchFlag, Names.Blocks.DUTCH_FLAG);
        GameRegistry.registerBlock(camoMine, Names.Blocks.CAMO_MINE);
        GameRegistry.registerBlock(Block_gen, Names.Blocks.Block_gen);

        Log.info("Modblocks initialized");
    }
}
