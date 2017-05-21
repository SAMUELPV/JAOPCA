package thelm.jaopca.modules;

import java.util.List;

import com.google.common.collect.Lists;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.compatibility.ENOCompatibility;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import thelm.jaopca.api.EnumEntryType;
import thelm.jaopca.api.ModuleBase;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.ItemEntry;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.utils.Utils;

public class ModuleExNihiloOmniaEnder extends ModuleBase {

	public static final ItemEntry ORE_ENDER_BROKEN_ENTRY = new ItemEntry(EnumEntryType.ITEM, "oreEnderBroken", new ModelResourceLocation("jaopca:ore_ender_broken#inventory"));
	public static final ItemEntry ORE_ENDER_GRAVEL_ENTRY = new ItemEntry(EnumEntryType.BLOCK, "oreEnderGravel", new ModelResourceLocation("jaopca:ore_ender_gravel#normal")).setBlockProperties(ModuleExNihiloOmnia.GRAVEL_PROPERTIES);

	@Override
	public String getName() {
		return "exnihiloomniaender";
	}

	@Override
	public List<String> getDependencies() {
		return Lists.<String>newArrayList("exnihiloomnia");
	}

	@Override
	public List<ItemEntry> getItemRequests() {
		List<ItemEntry> ret = Lists.<ItemEntry>newArrayList(ORE_ENDER_BROKEN_ENTRY, ORE_ENDER_GRAVEL_ENTRY);
		for(ItemEntry entry : ret) {
			entry.blacklist.addAll(ModuleExNihiloOmnia.EXISTING_ORES);
		}
		return ret;
	}

	@Override
	public void init() {
		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("oreEnderBroken")) {
			ModuleExNihiloOmnia.addOreSieveRecipe(ENOBlocks.GRAVEL_ENDER, Utils.getOreStack("oreEnderBroken", entry, 1), (int)(15D/entry.getEnergyModifier())+2);

			if(ENOCompatibility.add_smeltery_melting && Loader.isModLoaded("tconstruct") && FluidRegistry.isFluidRegistered(entry.getOreName().toLowerCase())) {
				ModuleTinkersConstruct.addMeltingRecipe("oreEnderBroken"+entry.getOreName(), FluidRegistry.getFluid(entry.getOreName().toLowerCase()), 36);
			}
		}

		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("oreEnderGravel")) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(Utils.getOreStack("oreEnderGravel", entry, 1), new Object[] {
					"oreEnderBroken"+entry.getOreName(),
					"oreEnderBroken"+entry.getOreName(),
					"oreEnderBroken"+entry.getOreName(),
					"oreEnderBroken"+entry.getOreName(),
			}));

			Utils.addSmelting(Utils.getOreStack("oreNetherGravel", entry, 1), Utils.getOreStack("ingot", entry, 1), 0);

			ModuleExNihiloOmnia.addOreHammerRecipe(JAOPCAApi.BLOCKS_TABLE.get("oreEnderGravel", entry.getOreName()), Utils.getOreStack("oreCrushed", entry, 1));

			if(ENOCompatibility.add_smeltery_melting && Loader.isModLoaded("tconstruct") && FluidRegistry.isFluidRegistered(entry.getOreName().toLowerCase())) {
				ModuleTinkersConstruct.addMeltingRecipe("oreEnderGravel"+entry.getOreName(), FluidRegistry.getFluid(entry.getOreName().toLowerCase()), 144);
			}

			if(ENOCompatibility.aa_crusher && Loader.isModLoaded("actuallyadditions")) {
				ModuleExNihiloOmnia.addActuallyAdditionsCrusherRecipe(Utils.getOreStack("oreEnderGravel", entry, 1), Utils.getOreStack("oreCrushed", entry, 5), Utils.	getOreStack("oreCrushed", entry, 2), 30);
			}
			
			if(ENOCompatibility.mekanism_crusher && Loader.isModLoaded("Mekanism")) {
				ModuleMekanism.addCrusherRecipe(Utils.getOreStack("oreEnderGravel", entry, 1), Utils.getOreStack("oreCrushed", entry, 6));
			}
			
			if(ENOCompatibility.sag_mill && Loader.isModLoaded("EnderIO")) {
				ModuleExNihiloOmnia.addOreSAGMillRecipe("oreEnderGravel"+entry.getOreName(), "oreCrushed"+entry.getOreName());
			}
		}
	}
}
