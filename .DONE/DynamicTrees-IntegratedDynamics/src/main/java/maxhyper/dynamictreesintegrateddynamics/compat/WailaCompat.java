package maxhyper.dynamictreesintegrateddynamics.compat;

import maxhyper.dynamictreesintegrateddynamics.blocks.BlockDynamicBranchMenril;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaCompat implements IWailaPlugin {
	
	@Override
	public void register(IWailaRegistrar registrar) {
		WailaBranchHandlerMenril branchHandler = new WailaBranchHandlerMenril();
		
		registrar.registerBodyProvider(branchHandler, BlockDynamicBranchMenril.class);
		registrar.registerNBTProvider(branchHandler, BlockDynamicBranchMenril.class);
	}
	
}
