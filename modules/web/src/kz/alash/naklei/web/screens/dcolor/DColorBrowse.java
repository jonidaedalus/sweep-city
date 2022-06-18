package kz.alash.naklei.web.screens.dcolor;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.UploadField;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import kz.alash.naklei.entity.dict.car.DColor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@UiController("naklei_DColor.browse")
@UiDescriptor("d-color-browse.xml")
@LookupComponent("dColorsTable")
@LoadDataBeforeShow
public class DColorBrowse extends StandardLookup<DColor> {

    @Inject
    private DataManager dataManager;
    @Inject
    private Notifications notifications;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private FileUploadField uploadExcelBtn;
    @Inject
    private Metadata metadata;
    @Inject
    private MessageTools messageTools;

    //Загрузка данных с excel
    @Subscribe("uploadExcelBtn")
    public void onUploadFieldFileUploadSucceed(FileUploadField.FileUploadSucceedEvent event) throws IOException {
        File file = fileUploadingAPI.getFile(uploadExcelBtn.getFileId());
        Set<String> extensions = new HashSet<>();
        extensions.add(".xls");
        extensions.add(".xlsx");
        extensions.add(".csv");
        uploadExcelBtn.setPermittedExtensions(extensions);

        if (file != null) {
            notifications.create()
                    .withCaption("File is uploaded to temporary storage at " + file.getAbsolutePath())
                    .show();
            FileInputStream stream = new FileInputStream(file);

            Workbook workbook = new HSSFWorkbook(stream);
            //Обрабатываем первую страницу excel
            Sheet sheet = workbook.getSheetAt(0);
            //Получим шапку с индексами
            Map<String, Integer> titleWithIndex = getTitleWithIndex(sheet.getRow(0));
            //Найдем индех колонки "Код"
            Integer codeColumnIndex = null;
            try{
                codeColumnIndex = getCodePropertyIndex(titleWithIndex);
            }catch (NullPointerException e){
                notifications.create(Notifications.NotificationType.ERROR)
                        .withCaption("Не найден колонка с наименованием \"Код\"")
                        .show();
            }
            if(codeColumnIndex != null){
                //Обновляем справочник пройдясь по всему строкам
                for (Row row : sheet) {
                    //пропускаем шапку excel
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    //Получаем код объекта в строке
                    String code = row.getCell(codeColumnIndex).getStringCellValue();
                    //Ищем в базе объект сущности
                    DColor object = dataManager
                                        .load(DColor.class)
                                        .query("select c from naklei_DColor c where c.code=:code")
                                        .parameter("code", code)
                                        .optional().orElse(null);
                    //если объект с кодом не найден, создадим новый
                    if(object == null)
                        object = metadata.create(DColor.class);

                    MetaClass metaClass = metadata.getClassNN(DColor.class);
                    //Заполняем данными
                    //Integer finalCodeColumnIndex = codeColumnIndex;
                    DColor finalObject = object;
                    metaClass.getOwnProperties().forEach(metaProperty -> {
                        titleWithIndex.forEach((propertyCaption, propertyColumnIndex) -> {
                            if(messageTools.getPropertyCaption(metaProperty).equals(propertyCaption)){
                                finalObject.setValue(metaProperty.getName(),row.getCell(propertyColumnIndex).getStringCellValue());
                            }
                        });
                    });
                    dataManager.commit(finalObject);
                    /*titleWithIndex.forEach((propertyName, columnIndex) -> {
                        if(!columnIndex.equals(finalCodeColumnIndex)){
                            object.set
                        }
                    });*/

                }



//                for (Row row : sheet) {
//                    //пропускаем шапку excel
//                    if(row.getRowNum() == 0){
//                        continue;
//                    }
//
//                    DColor pos = null;
//                    for (Cell cell : row) {
//                        if (cell.getColumnIndex() == 0) {
//                            pos = dataManager
//                                    .load(DColor.class)
//                                    .query("select c from naklei_DColor c where c.code=:code")
//                                    .parameter("code", cell.getStringCellValue())
//                                    .optional().orElse(null);
//                            if (pos == null) {
//                                pos = dataManager.create(DColor.class);
//                                pos.setCode(cell.getStringCellValue());
//                            }
//                        }
//                        if (cell.getColumnIndex() == 1 && pos != null)
//                            pos.setName(cell.getStringCellValue());
//                    }
//
//                    if(pos != null)
//                        dataManager.commit(pos);
//
//                }
            }

        }
        //Перезагружаем справочник
        getScreenData().loadAll();
    }

    private int getCodePropertyIndex(Map<String, Integer> titleWithIndex) {
        return titleWithIndex.get("Код");
    }

    private Map<String, Integer> getTitleWithIndex(Row row) {
        Map<String, Integer> result = new HashMap<>();
        for(int i = 0; i < row.getPhysicalNumberOfCells(); i++){
            result.put(row.getCell(i).getStringCellValue(), i);
        }
        return result;
    }

    @Subscribe("uploadExcelBtn")
    public void onUploadFieldFileUploadError(UploadField.FileUploadErrorEvent event) {
        notifications.create()
                .withCaption("File upload error")
                .show();
    }
}