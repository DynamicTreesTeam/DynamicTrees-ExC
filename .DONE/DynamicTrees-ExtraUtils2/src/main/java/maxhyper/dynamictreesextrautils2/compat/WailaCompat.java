package maxhyper.dynamictreesextrautils2.compat;

import maxhyper.dynamictreesextrautils2.blocks.BlockBranchFeJuniper;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaCompat implements IWailaPlugin {
	
	@Override
	public void register(IWailaRegistrar registrar) {
		WailaBranchHandlerExtraUtils2 branchHandler = new WailaBranchHandlerExtraUtils2();
		
		registrar.registerBodyProvider(branchHandler, BlockBranchFeJuniper.class);
	}
	
}
