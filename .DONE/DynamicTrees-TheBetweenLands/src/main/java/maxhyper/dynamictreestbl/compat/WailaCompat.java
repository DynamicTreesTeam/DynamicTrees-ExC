package maxhyper.dynamictreestbl.compat;

import maxhyper.dynamictreestbl.blocks.BlockBranchRubber;
import maxhyper.dynamictreestbl.blocks.BlockBranchSap;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaCompat implements IWailaPlugin {
	
	@Override
	public void register(IWailaRegistrar registrar) {
		WailaBranchHandlerSap branchHandler = new WailaBranchHandlerSap();
		
		registrar.registerBodyProvider(branchHandler, BlockBranchSap.class);
		registrar.registerNBTProvider(branchHandler, BlockBranchSap.class);
	}
	
}
