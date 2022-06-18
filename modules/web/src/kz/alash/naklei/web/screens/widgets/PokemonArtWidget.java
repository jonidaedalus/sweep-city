package kz.alash.naklei.web.screens.widgets;

import com.haulmont.addon.dashboard.web.annotation.DashboardWidget;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

@UiController("naklei_PokemonArtWidget")
@UiDescriptor("pokemon-art-widget.xml")
@DashboardWidget(name = "Logo")
public class PokemonArtWidget extends ScreenFragment {
}