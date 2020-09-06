package maxhyper.dynamictreestheaether.compat;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import maxhyper.dynamictreestheaether.blocks.BlockBranchGoldenOak;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaCompat implements IWailaPlugin {
	
	@Override
	public void register(IWailaRegistrar registrar) {
		WailaBranchHandlerAether branchHandler = new WailaBranchHandlerAether();
		
		registrar.registerBodyProvider(branchHandler, BlockBranchGoldenOak.class);
		registrar.registerNBTProvider(branchHandler, BlockBranchGoldenOak.class);
	}
	
}
