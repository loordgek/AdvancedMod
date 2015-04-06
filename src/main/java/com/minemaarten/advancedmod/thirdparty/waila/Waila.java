package com.minemaarten.advancedmod.thirdparty.waila;

import cpw.mods.fml.common.Loader;
import mcp.mobius.waila.api.IWailaRegistrar;

import com.minemaarten.advancedmod.block.BlockCamoMine;

public class Waila{

    public static void onWailaCall(IWailaRegistrar registrar){
        if (Loader.isModLoaded("Waila")){
            registrar.registerStackProvider(new WailaCamoHandler(), BlockCamoMine.class);
            registrar.registerBodyProvider(new WailaCamoHandler(), BlockCamoMine.class);
            registrar.registerNBTProvider(new WailaCamoHandler(), BlockCamoMine.class);
        }
    }
}
