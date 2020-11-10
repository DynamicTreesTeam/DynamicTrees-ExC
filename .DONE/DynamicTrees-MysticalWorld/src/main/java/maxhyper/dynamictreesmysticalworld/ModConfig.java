package maxhyper.dynamictreesmysticalworld;

import net.minecraftforge.common.config.Config;

/**
 * @author Harley O'Connor
 */
@Config(modid = DynamicTreesMysticalWorld.MODID)
public final class ModConfig {

    @Config.Name("Silkworm Egg Species")
    @Config.RequiresMcRestart()
    @Config.Comment({"Add species resource locations to this list for them to receive the silkworm egg drop. Note that this also respects Mystical World's config so if leaf drops or the silkworm egg is disabled the drop creators won't be added."})
    public static String[] silkwormSpecies = new String[]{"dynamictrees:oak", "dynamictrees:oakswamp", "dynamictrees:apple", "dynamictrees:spruce", "dynamictrees:megaspruce", "dynamictrees:birch", "dynamictrees:jungle", "dynamictrees:megajungle", "dynamictrees:acacia", "dynamictrees:darkoak"};

    @Config.Name("Require Silkworm by Hand")
    @Config.RequiresMcRestart()
    @Config.Comment("If enabled, the silkworm has to be obtained by breaking the leaves by hand.")
    public static boolean requireHand = false;

}
