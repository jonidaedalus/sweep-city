package kz.alash.naklei.web.screens.advertisement;

import com.haulmont.addon.maps.web.gui.components.GeoMap;
import com.haulmont.addon.maps.web.gui.components.HeatMapOptions;
import com.haulmont.addon.maps.web.gui.components.layer.HeatMapLayer;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.global.ValueLoadContext;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogOutcome;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.Route;
import kz.alash.naklei.entity.*;
import kz.alash.naklei.entity.dict.DCity;
import kz.alash.naklei.entity.dict.DPointCost;
import kz.alash.naklei.entity.dict.EAdvDriverStatus;
import kz.alash.naklei.entity.dict.EAdvStatus;
import kz.alash.naklei.entity.dict.EContractTime;
import kz.alash.naklei.service.AdvertisementService;
import kz.alash.naklei.service.utils.DateUtility;
import kz.alash.naklei.web.screens.advpurpose.AdvPuproseShowFragment;
import kz.alash.naklei.web.screens.advpurpose.AdvPurposeEdit;
import kz.alash.naklei.web.screens.car.CarEdit;
import org.apache.commons.lang3.time.DateUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static kz.alash.naklei.ConstantsRole.ADVERTISER_EMPLOYEE;
import static kz.alash.naklei.entity.Advertisement.DAYS_PER_MONTH;

@UiController("naklei_Advertisement.edit")
@UiDescriptor("advertisement-edit.xml")
@EditedEntityContainer("advertisementDc")
public class AdvertisementEdit extends StandardEditor<Advertisement> {
    //Кол-во дней между датами начала и завершения
    private Long numberOfDaysBetweenStartAndEnd = null;

    private static final Date today = new Date();

    private boolean isInProgressStatus;

    @Inject
    private DateField<Date> endDateField;
    @Inject
    private DateField<Date> startDateField;
    @Inject
    private LookupField<EContractTime> contractLengthField;
    @Inject
    private CollectionLoader<DCity> citiesDl;
    @Inject
    private LookupField<DCity> cityLookupField;
    @Inject
    private CollectionContainer<DCity> citiesDc;

    @Inject
    private DataContext dataContext;

    @Named("advPurposesTable.create")
    private CreateAction<AdvPurpose> advPurposesTableCreate;
    @Inject
    private Notifications notifications;
    @Inject
    private Button commitAndCloseBtn;
    @Inject
    private Button startAdvertisementBtn;
    @Inject
    private Label<String> otherContractTimeLabel;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<AdvPurpose> advPurposesTable;
    @Inject
    private Table<AdvertisementDriver> advertisementDriversTable;
    @Inject
    private Form advertisementDataForm;
    @Inject
    private Tree<KeyValueEntity> tree;
    @Inject
    private VBoxLayout selectedPurposeInfoVbox;
    @Inject
    private Fragments fragments;
    @Inject
    private VBoxLayout container;
    @Inject
    private CollectionLoader<AdvertisementDriver> advertisementDriversDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private InstanceContainer<Advertisement> advertisementDc;
    @Inject
    private KeyValueCollectionLoader menuDl;
    @Inject
    private HBoxLayout purposeButtonsPanel;
    @Inject
    private Label<BigDecimal> totalBudgetField;
    @Inject
    private TextField<Date> startDateReadOnly;
    @Inject
    private TextField<Date> endDateReadOnly;
    @Inject
    private Logger log;
    @Inject
    private InstanceLoader<Advertisement> advertisementDl;
    @Inject
    private Dialogs dialogs;
    @Inject
    private AdvertisementService advertisementService;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private CollectionLoader<Route> advRoutesDl;
    @Inject
    private GeoMap map;
    @Inject
    private DateField<Date> statDateFromField;
    @Inject
    private DateField<Date> statDateToField;
    @Inject
    private LookupField<AdvPurpose> statPurposeField;

    //    private CanvasLayer.Point labelPoint;
    private List<Route> statRoutes;

    //    @Inject
//    private GroupBoxLayout advertiserGroupBox;
    @Inject
    private TabSheet imageTabSheet;
    @Inject
    private KeyValueCollectionLoader dateValueDl;
    private BigDecimal totalDistance;
    private BigDecimal totalOts;
    @Inject
    private TextField<String> statDistanceTextField;
    private BigDecimal totalPoints;
    //    @Inject
//    private TextField<String> statPointsTextField;
    @Inject
    private TextField<String> statOTSTextField;
    @Inject
    private TextField<String> statCPMTextField;
    @Inject
    private LookupField chartTypeLookupField;
    @Inject
    private TextField<String> pastingCostsField;
    @Inject
    private TextField<String> distanceCostsField;
    @Inject
    private TextField<String> rewardCostsField;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private Button pay;

    @Subscribe
    public void onInit(InitEvent event) {
        //Устанавливаем меню(дерево):
        //Цели
        //Машины
        //Сотрудники
        tree.addSelectionListener(selectionEvent ->
                selectionEvent.getSelected().stream().findFirst().ifPresent(
                        selectedMenu -> tree.getItems().getItems().forEach(
                                item -> {
                                    EAdvertisementMenu menu = EAdvertisementMenu.fromId(item.getId().toString());
                                    if (menu != null && menu.equals(EAdvertisementMenu.PURPOSE)) {
                                        advPurposesTable.setSelected(new ArrayList<>());
                                    }
                                    if (menu != null && menu.getContentId() != null) {
                                        Component component = container.getComponent(menu.getContentId());
                                        if (component != null) {
                                            component.setVisible(menu.getId().equals(selectedMenu.getId()));
                                        }
                                    }

                                    if (getEditedEntity().getStatus().equals(EAdvStatus.IN_PROCESS))
                                        tree.setVisible(false);
                                }
                        )
                )
        );

        //убираем отображение контактов водителя для рекламодателя
        changeAdvertiserScreen();
    }

    private void changeAdvertiserScreen() {
        if (isAdvertiser()) {
            advertisementDriversTable.getColumn("driver").setValueProvider(advertisementDriver -> advertisementDriver.getDriver().getUser().getName());
            advertisementDriversTable.getActionNN("view").setVisible(false);
            pay.setEnabled(false);
            pay.setVisible(false);
        }
    }

    private boolean isAdvertiser() {
        return userSessionSource.getUserSession().getRoles().contains(ADVERTISER_EMPLOYEE);
    }

    @Subscribe("map")
    public void onMapMoveEnd(GeoMap.MoveEndEvent event) {
//        if (labelPoint == null) {
//            labelPoint = map.getCanvas().addPoint(event.getCenter());
//        }
        createLabelContent();
//        labelPoint.setStyle(new PointStyle(
//                new DivPointIcon(createLabelContent())
//                        .setIconSize(400, 80)
//                        .setStyles("my-style")));
    }

    private void createLabelContent() {
        totalDistance = BigDecimal.ZERO;
        totalOts = BigDecimal.ZERO;
        totalPoints = BigDecimal.ZERO;

        if (statRoutes != null && statRoutes.size() > 0) {
            totalOts = BigDecimal.valueOf(statRoutes.stream().filter(route -> route.getOts() != null).mapToDouble(Route::getOts).sum());
            totalDistance = BigDecimal.valueOf(statRoutes.stream().filter(route -> route.getDistance() != null).mapToDouble(Route::getDistance).sum());
        }

        statDistanceTextField.setValue(totalDistance.toString());
//        statPointsTextField.setValue(totalPoints.toString());
        statOTSTextField.setValue(totalOts.toString());
        BigDecimal CPM = BigDecimal.ZERO;


        if (totalOts.compareTo(BigDecimal.ZERO) != 0)
            CPM = totalPoints.divide(totalOts, RoundingMode.HALF_UP);

        statCPMTextField.setValue(CPM.toString());

//        if (labelPoint != null)
//            labelPoint.setStyle(new PointStyle(
//                    new DivPointIcon(String.format(
//                            "<h1 style=font-size:25px> " +
//                                    "<br>Пробег: %s<br>OTS: %s</h1>",
//                            totalDistance,
//                            totalOts))
//                            .setIconSize(400, 80)
//                            .setStyles("my-style")));

        //Point labelPointJts = (Point) labelPoint.getGeometry();
        // return
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        Advertisement advertisement;
        if (!PersistenceHelper.isNew(getEditedEntity())) {
            advertisement = dataManager.reload(getEditedEntity(), "advertisement-edit");
            advertisement = getScreenData().getDataContext().merge(advertisement);
            setEntityToEdit(advertisement);
            advertisementDc.setItem(advertisement);
        } else {
            advertisement = getEditedEntity();
        }
        LinkedHashMap<Object, Object> graphTypeOptions = new LinkedHashMap<>();
        graphTypeOptions.put("Расходы", 0);
        graphTypeOptions.put("OTS", 1);
        chartTypeLookupField.setOptionsMap(graphTypeOptions);

        chartTypeLookupField.setValue(0);

        chartTypeLookupField.addValueChangeListener(valueChangeEvent -> {
            dateValueDl.load();
        });

        if (advertisement.getName() == null)
            getEditedEntity().setName("Наименование рекламной кампании");

        setTotalBudget();

        advertisementDriversDl.setParameter("advertisement", getEditedEntity());
        advertisementDriversDl.load();

        menuDl.load();

        if (tree.getItems().size() > 0) {
            tree.setSelected(tree.getItems().getItems().findFirst().orElse(null));
        }

        if (advertisementDriversDl.getContainer().getItems().size() > 0) {
            advRoutesDl.setParameter("drivers", advertisementDriversDl.getContainer().getItems());
            advRoutesDl.load();

            if (advRoutesDl.getContainer().getItems().size() > 0) {
                setDataForStatistics(advRoutesDl.getContainer().getItems(), null, null, null);
            }
        }
        imageTabSheet.getTab("advertiserInfoTab").setCaption(getEditedEntity().getAdvertiser().getFullName());

        if (getEditedEntity().getPurposes() != null && getEditedEntity().getPurposes().size() > 0) {
            getEditedEntity().getPurposes().forEach(advPurpose -> {
                VBoxLayout tab = uiComponents.create(VBoxLayout.NAME);
                tab.setMargin(true, false, false, false);


                Image image = uiComponents.create(Image.NAME);

                if (advPurpose.getSticker() != null) {
                    image.setSource(FileDescriptorResource.class).setFileDescriptor(advPurpose.getSticker());
                    image.setWidthFull();
                    image.setAlignment(Component.Alignment.MIDDLE_CENTER);
                    image.setScaleMode(Image.ScaleMode.CONTAIN);
                    image.setAlignment(Component.Alignment.BOTTOM_LEFT);
                    tab.add(image);
                    tab.expand(image);
                }


                Button checkoutBtn = uiComponents.create(Button.NAME);
                checkoutBtn.setStyleName("small");
                checkoutBtn.setCaption("mockup");
                tab.add(checkoutBtn);
                checkoutBtn.addClickListener(clickEvent -> {
                    if (clickEvent.getButton().getCaption() != null && clickEvent.getButton().getCaption().equals("mockup")) {
                        image.setSource(FileDescriptorResource.class).setFileDescriptor(advPurpose.getMaket());
                        checkoutBtn.setCaption("наклейка");
                    } else {
                        image.setSource(FileDescriptorResource.class).setFileDescriptor(advPurpose.getSticker());
                        checkoutBtn.setCaption("mockup");
                    }
                });

                imageTabSheet.addTab(advPurpose.getStickerType().getName() + " наклейка", tab);
            });
        }
        //advertiserGroupBox.setCaption(getEditedEntity().getAdvertiser().getFullName());

        changeComponentsByStatus();
    }

    private void setDataForStatistics(List<Route> routes, AdvPurpose advPurpose, Date dateFrom, Date dateTo) {
        if (advPurpose != null)
            routes = routes.stream().filter(route -> route.getAdvertisementDriver().getPurpose().equals(advPurpose)).collect(Collectors.toList());

        if (dateFrom != null)
            routes = routes.stream().filter(route -> route.getDate().after(dateFrom) || route.getDate().equals(dateFrom)).collect(Collectors.toList());

        if (dateTo != null)
            routes = routes.stream().filter(route -> route.getDate().before(dateTo)).collect(Collectors.toList());

        HeatMapOptions options = new HeatMapOptions();
        options.setMaximumIntensity(5D);
        options.setMinOpacity(0.4D);

        HeatMapLayer heatMapLayer = map.getLayerOrNull("heatMapLayer");

        if (heatMapLayer != null)
            map.removeLayer(heatMapLayer);

        heatMapLayer = new HeatMapLayer("heatMapLayer");

        heatMapLayer.setOptions(options);
        heatMapLayer.setEditable(false);
        statRoutes = routes;

        dateValueDl.load();
        createLabelContent();

        heatMapLayer.setDataDelegate(mapLayer -> {
                    Map<Point, Double> result = new HashMap<>();
                    for (Route route : statRoutes) {
                        Coordinate[] coordinates = route.getLine().getCoordinates();
                        GeometryFactory factory = new GeometryFactory();
                        MultiPoint multiPoint = factory.createMultiPointFromCoords(coordinates);

                        for (int i = 0; i < multiPoint.getNumGeometries(); i = i + 5000) {
                            Point point = factory.createPoint(multiPoint.getGeometryN(i).getCoordinate());
                            result.put(point, result.getOrDefault(point, 0D) + 0.001D);
                        }
                    }
                    return result;
                }
        );

        map.addLayer(heatMapLayer);
        // map.addHeatMap(newMap, options);
    }


    @Install(to = "menuDl", target = Target.DATA_LOADER)
    private List<KeyValueEntity> menuDlLoadDelegate(ValueLoadContext valueLoadContext) {
        List<KeyValueEntity> list = new ArrayList<>();

        for (EAdvertisementMenu eMenu : EAdvertisementMenu.values()) {
            KeyValueEntity menu = new KeyValueEntity();
            menu.setIdName("menuId");
            menu.setId(eMenu.getId());
            String name;

            if (eMenu.equals(EAdvertisementMenu.PURPOSE)) {
                int purposeSize = 0;
                if (getEditedEntity().getPurposes() != null) {
                    purposeSize = getEditedEntity().getPurposes().size();
                }
                name = eMenu.getName() + " (" + purposeSize + ")";
            } else if (eMenu.equals(EAdvertisementMenu.CARS)) {
                List<AdvertisementDriver> advDrivers = advertisementDriversDl.getContainer().getItems();
                //показываем количество активных машин
                int advDriversAmount = getActiveCars(advDrivers).size();
                name = eMenu.getName() + " (" + advDriversAmount + ")";
            } else {
                name = eMenu.getName();
                //показываем статистику
                List<AdvertisementDriver> advertisementDrivers = advertisementDriversDl.getContainer().getItems();
                //расходы за стикеры на 1 автомобиль
                if (advertisementDrivers != null && advertisementDrivers.size() > 0) {
                    setPastingCosts(advertisementDrivers);
                    //максимальный расход за сутки на 1 автомобиль
                    List<AdvPurpose> advPurposes = getEditedEntity().getPurposes();
                    setDistanceCosts(getEditedEntity(), advPurposes);
                    setRewardCosts(getStickedCars(advertisementDrivers), advPurposes);
                }
            }

            menu.setValue("name", name);

            list.add(menu);
        }

        return list;
    }

    private void setRewardCosts(List<AdvertisementDriver> stickedAdvDrivers, List<AdvPurpose> advPurposes) {
        int maxCars = 0;
        int rewardedCars = 0;
        BigDecimal maxAmount = BigDecimal.ZERO;
        BigDecimal spentAmount = BigDecimal.ZERO;
        for (AdvPurpose purpose : advPurposes) {
            maxCars += purpose.getCarAmount();
            maxAmount = maxAmount.add(
                    purpose.getRewardAmount()
                            .multiply(BigDecimal.valueOf(maxCars))
            );
        }

        for (AdvertisementDriver advDriver : stickedAdvDrivers) {
            if (advDriver.getStickedWithinPeriod() != null && advDriver.getStickedWithinPeriod()) {
                spentAmount = spentAmount.add(advDriver.getPurpose().getRewardAmount());
                rewardedCars++;
            }
        }

        rewardCostsField.setValue("Вознаграждения " + rewardedCars + "/" + maxCars + "\t" + spentAmount.intValue() + "/" + maxAmount.intValue());
    }

    private void setDistanceCosts(Advertisement advertisement, List<AdvPurpose> advPurposes) {
        for (AdvPurpose advPurpose : advPurposes) {
            long operatedDays = DateUtility.differenceBetween(advertisement.getEndDate(), advertisement.getStartDate());
            int maxCars = advPurpose.getCarAmount();
//            point cost
            DPointCost thePointCost = dataManager.load(DPointCost.class).view("dPointCost-view").list().stream()
                    .filter(pointCost -> pointCost.getCarClass().equals(advPurpose.getCarClass())
                            && pointCost.getStickerType().equals(advPurpose.getStickerType()))
                    .findFirst()
                    .orElse(null);
//            maximum cost
            BigDecimal theoreticalPurposeCost =
                    thePointCost.getAdvertiserCost()
                            .multiply(BigDecimal.valueOf(maxCars * operatedDays))
                            .multiply(BigDecimal.valueOf(advPurpose.getCarClass().getMaxPointPerDay()));

            BigDecimal actualPurposeCost = BigDecimal.ZERO;
            List<AdvertisementDriver> advList = advertisementDriversDl.getContainer().getItems();
            for (AdvertisementDriver advertisementDriver : advList) {
//                add = earned money by driver * advertiser point cost / driver point cost
                BigDecimal pureAdd = advertisementDriver.getStickedWithinPeriod() != null && advertisementDriver.getStickedWithinPeriod()
                        ? advertisementDriver.getEarnedMoney().subtract(advertisementDriver.getPurpose().getRewardAmount())
                        : advertisementDriver.getEarnedMoney();
                BigDecimal add = pureAdd
                        .divide(thePointCost.getDriverCost(), RoundingMode.HALF_UP)
                        .multiply(thePointCost.getAdvertiserCost());
                actualPurposeCost = actualPurposeCost.add(add);
            }

            distanceCostsField.setValue("Пробег " + actualPurposeCost.intValue() + "/" + theoreticalPurposeCost.intValue());
        }
    }

    private void setPastingCosts(List<AdvertisementDriver> advertisementDrivers) {
        int maxCarAmount = getEditedEntity().getPurposes().stream().mapToInt(AdvPurpose::getCarAmount).sum();
        int stickedCarAmount = getStickedCars(advertisementDrivers).size();
        BigDecimal pastingCost = BigDecimal.ZERO;
        List<AdvertisementDriver> stickedAdvDrivers = getStickedCars(advertisementDrivers);
        for (AdvertisementDriver advertisementDriver : stickedAdvDrivers) {
            pastingCost = pastingCost.add(advertisementDriver.getPurpose().getPastingCost());
        }

        BigDecimal maxPastingCost = BigDecimal.ZERO;
        List<AdvPurpose> purposes = getEditedEntity().getPurposes();
        for (AdvPurpose purpose : purposes) {
            maxPastingCost = maxPastingCost.add(
                    purpose.getPastingCost().multiply(BigDecimal.valueOf(purpose.getCarAmount()))
            );
        }
        pastingCostsField.setValue("Оклейка " + stickedCarAmount + "/" + maxCarAmount + "\t" + pastingCost.intValue() + "/" + maxPastingCost.intValue());
    }

    private List<AdvertisementDriver> getActiveCars(List<AdvertisementDriver> advertisementDrivers) {
        return advertisementDrivers.stream()
                .filter(advertisementDriver ->
                        advertisementDriver.getStatus().equals(EAdvDriverStatus.ACTIVE))
                .collect(Collectors.toList());
    }

    private List<AdvertisementDriver> getStickedCars(List<AdvertisementDriver> advertisementDrivers) {
        //todo переделать через isSticked
        return advertisementDrivers.stream()
                .filter(advertisementDriver ->
                        (advertisementDriver.getStatus().equals(EAdvDriverStatus.ACTIVE) ||
                                advertisementDriver.getStatus().equals(EAdvDriverStatus.FINISHED) ||
                                advertisementDriver.getStatus().equals(EAdvDriverStatus.RESTICK)) &&
                                advertisementDriver.getStartDate() != null)
                .collect(Collectors.toList());
    }

    @Install(to = "dateValueDl", target = Target.DATA_LOADER)
    private List<KeyValueEntity> dateValueDlLoadDelegate(ValueLoadContext valueLoadContext) {
        List<KeyValueEntity> list = new ArrayList<>();
        if (statRoutes.size() > 0) {
            Map<Date, List<Route>> groupingRoutes = statRoutes.stream().collect(Collectors.groupingBy(route -> new java.sql.Date(route.getDate().getTime())));
            groupingRoutes.forEach((date, routes) -> {
                KeyValueEntity valueEntity = new KeyValueEntity();
                valueEntity.setValue("date", date);

                if (chartTypeLookupField.getValue() == null || chartTypeLookupField.getValue().equals(0))
                    valueEntity.setValue("value", routes.stream().filter(route -> route.getPoints() != null).mapToDouble(Route::getPoints).sum());
                else
                    valueEntity.setValue("value", routes.stream().filter(route -> route.getOts() != null).mapToDouble(Route::getOts).sum());

                list.add(valueEntity);
            });
        }

        return list;
    }

    private void calculateDates() {
        Date startDate = startDateField.getValue();
        Date endDate = endDateField.getValue();

        if (startDate != null && endDate != null) {
            long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
            numberOfDaysBetweenStartAndEnd = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            int numberOfMonth = (int) (numberOfDaysBetweenStartAndEnd / DAYS_PER_MONTH);
            long remainingDays = numberOfDaysBetweenStartAndEnd - (long) numberOfMonth * (DAYS_PER_MONTH);

            EContractTime contractLength;
            //Если в разнице будет остаток в днях (месяц - 30 сейчас) тогда она будет 30 дней
            otherContractTimeLabel.setVisible(remainingDays != 0);
            if (remainingDays != 0) {
                contractLength = EContractTime.OTHER;

                String otherContractTime = "";
                if (numberOfMonth != 0)
                    otherContractTime = "Кол-во месяцев: " + numberOfMonth + ". ";

                otherContractTime += "Кол-во дней: " + remainingDays;
                otherContractTimeLabel.setValue(otherContractTime);
            } else {
                contractLength = EContractTime.fromId(numberOfMonth);
            }

            contractLengthField.setValue(contractLength);

        }
    }

    @Subscribe(id = "advertisementDc", target = Target.DATA_CONTAINER)
    public void onAdvertisementDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<Advertisement> event) {
        if (event.getProperty().equals("purposes")) {
            setTotalBudget();
            return;
        }

        if (event.getProperty().equals("city") || event.getProperty().equals("startDate") || event.getProperty().equals("endDate")) {
            enableCreatePurposeBtn();
        }
    }

    private void setTotalBudget() {
        BigDecimal totalBudget = BigDecimal.ZERO;
        if (getEditedEntity().getPurposes() != null) {
            totalBudget = getEditedEntity().getPurposes().stream().map(AdvPurpose::getBudget).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        totalBudgetField.setValue(totalBudget);
    }

    private void changeComponentsByStatus() {
        isInProgressStatus = getEditedEntity().getStatus().equals(EAdvStatus.IN_PROCESS);

//        //Не показывать машины если рекл.кампания со статусом "Драфт"
//        startDateField.setEditable(isInProgressStatus);
//        endDateField.setEditable(isInProgressStatus);
//        contractLengthField.setEditable(isInProgressStatus);
//        cityLookupField.setEditable(isInProgressStatus);

        commitAndCloseBtn.setVisible(isInProgressStatus);
        startAdvertisementBtn.setVisible(isInProgressStatus);
        purposeButtonsPanel.setVisible(isInProgressStatus);

        enableCreatePurposeBtn();

        if (isInProgressStatus) {
            //Загружаем города
            citiesDl.load();

            if (getEditedEntity().getCity() == null)
                cityLookupField.setValue(citiesDc.getItems().stream()
                        .filter(city -> city.getCode().equals(DCity.ALMATY_CITY_CODE))
                        .findFirst().orElse(null));

            calculateDates();
        } else {
            setReadOnly(true);
            getWindow().setCaption("Обзор рекламной кампании");
            advertisementDataForm.setCaptionPosition(Form.CaptionPosition.LEFT);
            advertisementDataForm.getComponents().forEach(component -> {
                component.setStyleName("borderless align-right");
                if (component instanceof DateField) {
                    component.setVisible(false);
                }
            });
            contractLengthField.setVisible(false);

            startDateReadOnly.setVisible(true);
            endDateReadOnly.setVisible(true);
        }
    }

    private void enableCreatePurposeBtn() {
        DCity city = cityLookupField.getValue();
        EContractTime contractLength = contractLengthField.getValue();
        Date startDate = startDateField.getValue();
        Date endDate = endDateField.getValue();
        advPurposesTableCreate.setEnabled(isInProgressStatus && city != null && startDate != null && endDate != null);
    }

    @Subscribe("startAdvertisementBtn")
    public void onStartAdvertisementBtnlick(Button.ClickEvent event) {
        //Дата начала должно быть с запасом в 2 недели
        if (!validateStartDate()) return;

        ValidationErrors errors = validateScreen();
        if (!errors.isEmpty()) return;

        //Должны быть создраны цели
        if (getEditedEntity().getPurposes() == null || getEditedEntity().getPurposes().size() == 0) {
            notifications.create()
                    .withType(Notifications.NotificationType.ERROR)
                    .withDescription("Создайте цели")
                    .show();
            return;
        } else {
            //Проверка наличие наклеек в целях
            if (getEditedEntity().getPurposes().stream().filter(advPurpose -> advPurpose.getSticker() != null).count() != getEditedEntity().getPurposes().size()) {
                notifications.create()
                        .withType(Notifications.NotificationType.ERROR)
                        .withDescription("Для начала рекламной кампании необходима наклейка в целях")
                        .show();
                return;
            }
        }

        //Отнять бюджет с баланса
        BigDecimal newBalance = getEditedEntity()
                .getAdvertiser()
                .getBalance()
                .subtract(totalBudgetField.getValue());

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            notifications.create()
                    .withType(Notifications.NotificationType.ERROR)
                    .withDescription("Баланса не достаточно")
                    .show();
            return;
        }

        //Надо отнять деньги с баланса компании
        getEditedEntity().getAdvertiser()
                .setBalance(newBalance);

        getEditedEntity().setStatus(EAdvStatus.PENDING);

        changeComponentsByStatus();
        getScreenData().getDataContext().commit();
    }

    private boolean validateStartDate() {
        Date startDate = startDateField.getValue();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 1);

        if (startDate == null || startDate.before(cal.getTime())) {
            notifications.create()
                    .withType(Notifications.NotificationType.ERROR)
                    .withDescription("Дата начала должна быть с запасом хотя бы 2 дня")
                    .show();

            return false;
        }

        if (getEditedEntity().getPurposes() != null) {
            //Проверим даты оклейки в целях
            List<AdvPurpose> purposesWithIncorrectPastingDate = getEditedEntity().getPurposes().stream().filter(advPurpose -> !(advPurpose.getPastingDate().after(today) && advPurpose.getPastingDate().before(startDate))).collect(Collectors.toList());

            if (purposesWithIncorrectPastingDate.size() != 0) {

                notifications.create()
                        .withType(Notifications.NotificationType.ERROR)
                        .withDescription("Имеются цели у которых дата оклейки не удовлетворяют условию (Дата оклейки должна быть больше текущей и не меньше дата начала рекл. кампании)")
                        .show();

                return false;
            }
        }

        return true;
    }

    @Subscribe("contractLengthField")
    public void onContractLengthFieldValueChange(HasValue.ValueChangeEvent<EContractTime> event) {
        if (event.getValue() == null)
            return;

        if (event.isUserOriginated() && !event.getValue().equals(EContractTime.OTHER) && startDateField.getValue() != null) {
            if (getEditedEntity().getPurposes() != null && getEditedEntity().getPurposes().size() > 0) {
                dialogs.createOptionDialog()
                        .withCaption("Изменились даты рекламной кампании")
                        .withMessage("Пересчитать кол-во авто в созданных целях?")
                        .withType(Dialogs.MessageType.CONFIRMATION)
                        .withActions(
                                new DialogAction(DialogAction.Type.OK)
                                        .withCaption("Да")
                                        .withHandler(e -> {
                                            Date endDate = DateUtils.addDays(startDateField.getValue(), event.getValue().getId() * DAYS_PER_MONTH);
                                            endDateField.setValue(endDate);
                                            calculateDates();

                                            changeCarAmountForAllPurposes();
                                        }),

                                new DialogAction(DialogAction.Type.CANCEL)
                                        .withHandler(e -> contractLengthField.setValue(event.getPrevValue()))
                        )
                        .show();
            } else {
                Date endDate = DateUtils.addDays(startDateField.getValue(), event.getValue().getId() * DAYS_PER_MONTH);
                endDateField.setValue(endDate);
                calculateDates();
            }
        }
    }

    private void changeCarAmountForAllPurposes() {
        getEditedEntity().getPurposes().forEach(advPurpose -> {
            Long carAmount = null;
            try {
                carAmount = advertisementService.calculateMaxCarAmount(
                        advPurpose.getBudget(),
                        advPurpose.getCarClass(),
                        advPurpose.getStickerType(),
                        advPurpose.getPastingCost(),
                        advPurpose.getRewardAmount(),
                        numberOfDaysBetweenStartAndEnd);
            } catch (Exception e) {
                notifications
                        .create()
                        .withType(Notifications.NotificationType.ERROR)
                        .withDescription(e.getMessage())
                        .show();
                return;
            }

            advPurpose.setCarAmount(Math.toIntExact(carAmount));
        });
        if (getScreenData().getDataContext().hasChanges()) {
            getScreenData().getDataContext().commit();
            advertisementDl.load();
        }

        //Change amount of card for all purposes

        //

        notifications.create()
                .withCaption("Изменены кол-во авто")
                .show();
    }

    @Subscribe("advPurposesTable.create")
    protected void onCustomersTableCreateActionPerformed(Action.ActionPerformedEvent event) {
        AdvPurposeEdit editScreen =
                screenBuilders.editor(advPurposesTable)
                        .newEntity()
                        .withScreenClass(AdvPurposeEdit.class)
                        .withLaunchMode(OpenMode.DIALOG)
                        .build();
        if (numberOfDaysBetweenStartAndEnd == null) {
            log.error("Кол-во дней пустые");
            return;
        }

        editScreen.setNumberOfDaysBetweenStartAndEnd(numberOfDaysBetweenStartAndEnd);
        editScreen.setStartAdvertisementDate(startDateField.getValue());
        editScreen.show();
    }

    @Subscribe("advPurposesTable.edit")
    public void onAdvPurposesTableEdit(Action.ActionPerformedEvent event) {
        if (advPurposesTable.getSingleSelected() == null) return;

        AdvPurposeEdit editScreen =
                screenBuilders.editor(advPurposesTable)
                        .editEntity(advPurposesTable.getSingleSelected())
                        .withScreenClass(AdvPurposeEdit.class)
                        .withLaunchMode(OpenMode.DIALOG)
                        .build();

        if (numberOfDaysBetweenStartAndEnd == null) {
            log.error("Кол-во дней пустые");
            return;
        }

        editScreen.setNumberOfDaysBetweenStartAndEnd(numberOfDaysBetweenStartAndEnd);
        editScreen.setStartAdvertisementDate(startDateField.getValue());
        editScreen.show();

        editScreen.addAfterCloseListener(afterCloseEvent -> {
            if (getScreenData().getDataContext().hasChanges()) {
                dataContext.commit();
            }
            advertisementDl.load();
            advPurposesTable.setSelected(new ArrayList<>());

        });
    }

    @Subscribe("advertisementDriversTable.view")
    public void onAdvertisementDriversTableView(Action.ActionPerformedEvent event) {
        if (advertisementDriversTable.getSingleSelected() == null) return;
        if (isAdvertiser()) return;

        CarEdit viewScreen =
                screenBuilders.editor(Car.class, this)
                        .withScreenClass(CarEdit.class)
                        .withLaunchMode(OpenMode.THIS_TAB)
                        .build();
        viewScreen.setEntityToEdit(advertisementDriversTable.getSingleSelected().getDriver().getCar());
        viewScreen.setReadOnly(true);
        viewScreen.show();
    }

    @Subscribe("advPurposesTable")
    public void onAdvPurposesTableSelection(Table.SelectionEvent<AdvPurpose> event) {
        if (event.getSelected().size() == 0) {
            selectedPurposeInfoVbox.removeAll();
        }
        //Выбрать можно тока одну рекл кампанию
        Set<AdvPurpose> selectedPurposes = event.getSelected();
        AdvPurpose purpose = selectedPurposes.stream().findFirst().orElse(null);
        if (purpose != null) {
            AdvPuproseShowFragment advPurposeShowFragment = fragments.create(this, AdvPuproseShowFragment.class);
            advPurposeShowFragment.setAdvPurposeParam(purpose);
            selectedPurposeInfoVbox.removeAll();
            selectedPurposeInfoVbox.add(advPurposeShowFragment.getFragment());
            selectedPurposeInfoVbox.setVisible(true);
        }
    }

    @Subscribe("startDateField")
    public void onStartDateFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        if (event.isUserOriginated() && event.getValue() != null) {
            if (getEditedEntity().getPurposes() != null && getEditedEntity().getPurposes().size() > 0) {
                dialogs.createOptionDialog()
                        .withCaption("Изменились даты рекламной кампании")
                        .withMessage("Пересчитать кол-во авто в созданных целях?")
                        .withType(Dialogs.MessageType.CONFIRMATION)
                        .withActions(
                                new DialogAction(DialogAction.Type.OK)
                                        .withCaption("Да")
                                        .withHandler(e -> {
                                            calculateDates();
                                            changeCarAmountForAllPurposes();
                                        }),

                                new DialogAction(DialogAction.Type.CANCEL)
                                        .withHandler(e -> startDateField.setValue(event.getPrevValue()))
                        )
                        .show();
            } else {
                if (endDateField.getValue() == null && contractLengthField.getValue() != null) {
                    Date endDate = DateUtils.addDays(event.getValue(), contractLengthField.getValue().getId() * DAYS_PER_MONTH);
                    endDateField.setValue(endDate);
                }
                calculateDates();
            }
        }
    }

    @Subscribe("endDateField")
    public void onEndDateFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        if (event.isUserOriginated()) {
            if (getEditedEntity().getPurposes() != null && getEditedEntity().getPurposes().size() > 0) {
                dialogs.createOptionDialog()
                        .withCaption("Изменились даты рекламной кампании")
                        .withMessage("Пересчитать кол-во авто в созданных целях?")
                        .withType(Dialogs.MessageType.CONFIRMATION)
                        .withActions(
                                new DialogAction(DialogAction.Type.OK)
                                        .withCaption("Да")
                                        .withHandler(e -> {
                                            calculateDates();

                                            changeCarAmountForAllPurposes();
                                        }),

                                new DialogAction(DialogAction.Type.CANCEL)
                                        .withHandler(e -> endDateField.setValue(event.getPrevValue()))
                        )
                        .show();
            }
        }

    }

    @Subscribe("advNameEditBtn")
    public void onAdvNameEditBtnClick(Button.ClickEvent event) {
        dialogs.createInputDialog(this)
                .withCaption("Изменить наименование рекл. кампании")
                .withParameters(
                        InputParameter.parameter("name").withField(() -> {
                            TextArea<String> field = uiComponents.create(TextArea.NAME);
                            field.setValue(getEditedEntity().getName());
                            field.setWidthFull();
                            field.setHeightFull();
                            return field;
                        })
                )

                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.getCloseAction().equals(InputDialog.INPUT_DIALOG_OK_ACTION)) {
                        String name = closeEvent.getValue("name");
                        getEditedEntity().setName(name);
                    }
                })
                .show();
    }

    @Subscribe("statPurposeField")
    public void onStatDriverFieldValueChange(HasValue.ValueChangeEvent<AdvPurpose> event) {
        setDataForStatistics(advRoutesDl.getContainer().getItems(), event.getValue(), statDateFromField.getValue(), statDateToField.getValue());
    }

    @Subscribe("statDateFromField")
    public void onStatDateFromFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        setDataForStatistics(advRoutesDl.getContainer().getItems(), statPurposeField.getValue(), event.getValue(), statDateToField.getValue());
    }

    @Subscribe("statDateToField")
    public void onStatDateToFieldValueChange(HasValue.ValueChangeEvent<Date> event) {
        setDataForStatistics(advRoutesDl.getContainer().getItems(), statPurposeField.getValue(), statDateFromField.getValue(), event.getValue());
    }


    @Subscribe("pay")
    public void onPayClick(Button.ClickEvent event) {
        if (event == null)  return;
        dialogs.createInputDialog(this)
                .withCaption("Введите сумму")
                .withParameters(
                        InputParameter.bigDecimalParameter("summ")
                )
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        AdvertisementDriver selected = advertisementDriversTable.getSingleSelected();
                        if (selected == null)
                            return;
                        BigDecimal summ = closeEvent.getValue("summ");
                        selected = dataManager.reload(selected, "advertisementDriver-payment-view");
                        dataManager.commit(
                                advertisementService.addMoneyToAdvDriver(
                                        selected,
                                        selected.getDriver(),
                                        summ)
                        );
                    }
                })
                .show();
    }
}