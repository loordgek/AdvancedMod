package com.minemaarten.advancedmod.block;

import com.minemaarten.advancedmod.reference.Reference;
import com.minemaarten.advancedmod.tileentity.TileEntitiyGenerator;
import com.minemaarten.advancedmod.utility.Names;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by stefan on 1-2-2015.
 */
public class BlockGenerator extends BlockAdvancedModTileEntity {
    public BlockGenerator(){
        setBlockName(Names.Blocks.Block_gen);
        setBlockTextureName(Reference.MOD_ID_LOWER + ":" + Names.Blocks.DUTCH_FLAG);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntitiyGenerator();
    }
}
