package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.fluid.WastelandFluid;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

public class FluidInit {

    public static final FlowableFluid STILL_WASTE = (FlowableFluid) register("example_still", new WastelandFluid().getStill());
    public static final FlowableFluid FLOWING_WASTE = (FlowableFluid) register("example_flowing", new WastelandFluid().getFlowing());


    public static Fluid register(String name, Fluid fluid) {
        RegistryKey<Fluid> fluidKey = RegistryKey.of(RegistryKeys.FLUID, Identifier.of(MODID, name));
        return Registry.register(Registries.FLUID, fluidKey, fluid);
    }


}
