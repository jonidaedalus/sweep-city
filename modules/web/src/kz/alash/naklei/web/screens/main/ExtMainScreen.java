package kz.alash.naklei.web.screens.main;

import com.haulmont.addon.helium.web.theme.HeliumThemeVariantsManager;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.mainwindow.SideMenu;
import com.haulmont.cuba.gui.screen.MessageBundle;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.vaadin.server.Page;

import javax.inject.Inject;
import java.util.stream.Stream;


@UiController("extMainScreen")
@UiDescriptor("ext-main-screen.xml")
public class ExtMainScreen extends MainScreen{

    @Inject
    protected SideMenu sideMenu;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected UserSession userSession;
    @Inject
    protected ScreenBuilders screenBuilders;
    @Inject
    protected HeliumThemeVariantsManager heliumThemeVariantsManager;
    @Inject
    protected Button switchThemeModeBtn;
    @Inject
    protected MessageBundle messageBundle;

    @Subscribe
    protected void initMainMenu(AfterShowEvent event) {
        //createMyVisitMenuItem();
        //openPetclinicMenuItem();

        final HeliumThemeSwitchBtnMode currentThemeMode = HeliumThemeSwitchBtnMode
                .fromId(heliumThemeVariantsManager.loadUserAppThemeModeSettingOrDefault());
        updateHeliumSwitchBtn(currentThemeMode);

        initMenuIcons();

    }

    private void initMenuIcons() {
        Stream.of(SideMenuIcon.values())
                .forEach(sideMenuIcon -> {

                    final SideMenu.MenuItem menuItem = sideMenu.getMenuItem(sideMenuIcon.getMenuId());

                    if (menuItem != null) {
                        menuItem.setIcon(sideMenuIcon.source());
                    }

                });
    }

    private void updateHeliumSwitchBtn(HeliumThemeSwitchBtnMode mode) {
        switchThemeModeBtn.setIconFromSet(mode.getIcon());
        switchThemeModeBtn.setStyleName(mode.getStyleName());
    }


//    private void openPetclinicMenuItem() {
//        final SideMenu.MenuItem petclinicMenu = sideMenu.getMenuItem("application-petclinic");
//        final SideMenu.MenuItem menuItem = petclinicMenu.getChildren().get(1);
//        petclinicMenu.setExpanded(true);
//        sideMenu.setSelectOnClick(true);
//        sideMenu.setSelectedItem(menuItem);
//    }

//    private void createMyVisitMenuItem() {
//        SideMenu.MenuItem myVisits = sideMenu.createMenuItem("myVisits");
//        myVisits.setBadgeText(messageBundle.formatMessage("myVisitMenuItemBadge", amountOfVisits()));
//        myVisits.setCaption(messageBundle.getMessage("myVisitsMenuItem"));
//        myVisits.setIcon(CubaIcon.USER_CIRCLE.source());
//        myVisits.setCommand(menuItem ->
//                screenBuilders.screen(this)
//                        .withScreenClass(MyVisits.class)
//                        .withOpenMode(OpenMode.DIALOG)
//                        .show()
//        );
//        sideMenu.addMenuItem(myVisits, 0);
//    }

//    private int amountOfVisits() {
//        return dataManager.load(Visit.class)
//                .query(
//                        "e.assignedNurse = :currentUser and e.treatmentStatus <> @enum(com.haulmont.sample.petclinic.entity.visit.VisitTreatmentStatus.DONE)")
//                .parameter("currentUser", userSession.getCurrentOrSubstitutedUser())
//                .list().size();
//    }

//    @Subscribe("refreshMyVisits")
//    protected void onRefreshMyVisitsTimerAction(Timer.TimerActionEvent event) {
//        sideMenu.getMenuItem("myVisits")
//                .setBadgeText(amountOfVisits() + " Visits");
//    }

    @Subscribe("switchThemeMode")
    protected void onSwitchThemeMode(Action.ActionPerformedEvent event) {

        final HeliumThemeSwitchBtnMode newTargetThemeMode = newTargetThemeMode();

        heliumThemeVariantsManager.setUserAppThemeMode(newTargetThemeMode.getName());
        Page.getCurrent().reload();
        updateHeliumSwitchBtn(newTargetThemeMode);
    }

    private HeliumThemeSwitchBtnMode newTargetThemeMode() {

        return HeliumThemeSwitchBtnMode.fromId(
                heliumThemeVariantsManager
                        .getAppThemeModeList()
                        .stream()
                        .filter(mode -> !mode.equals(
                                heliumThemeVariantsManager.loadUserAppThemeModeSettingOrDefault())
                        )
                        .findFirst()
                        .orElse("light")
        );
    }

}