package extension.mcoreconverter;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.extentions.Extension;
import io.github.toberocat.improvedfactions.extentions.ExtensionRegistry;

public class Main extends Extension {
    @Override
    protected ExtensionRegistry register() {
        return new ExtensionRegistry("MCoreConverter", "v1.0", new String[] {
                "BETAv4.0.0"
        });
    }

    @Override
    protected void OnEnable(ImprovedFactionsMain plugin) {
        System.out.println("Eahdkjhdhdfk");
        super.OnEnable(plugin);
    }
}
