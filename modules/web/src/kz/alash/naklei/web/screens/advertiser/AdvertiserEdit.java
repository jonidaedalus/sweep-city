package kz.alash.naklei.web.screens.advertiser;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import kz.alash.naklei.entity.Advertiser;

import javax.inject.Inject;

@UiController("naklei_Advertiser.edit")
@UiDescriptor("advertiser-edit.xml")
@EditedEntityContainer("advertiserDc")
@LoadDataBeforeShow
public class AdvertiserEdit extends StandardEditor<Advertiser> {
    @Inject
    private Image image;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private FileUploadField uploadField;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private DataManager dataManager;
    @Inject
    private InstanceContainer<Advertiser> advertiserDc;
    @Inject
    private Button downloadImageBtn;
    @Inject
    private Button clearImageBtn;
    @Inject
    private HBoxLayout imageBtns;
    @Inject
    private Button commitAndCloseBtn;

    @Subscribe
    public void onInit(InitEvent event) {
        uploadField.addFileUploadSucceedListener(uploadSucceedEvent -> {
            FileDescriptor fd = uploadField.getFileDescriptor();
            try {
                fileUploadingAPI.putFileIntoStorage(uploadField.getFileId(), fd);
            } catch (FileStorageException e) {
                throw new RuntimeException("Error saving file to FileStorage", e);
            }
            getEditedEntity().setLogo(dataManager.commit(fd));
            displayImage();
        });

        advertiserDc.addItemPropertyChangeListener(changeEvent -> {
            if ("imageFile".equals(changeEvent.getProperty()))
                updateImageButtons(changeEvent.getValue() != null);
        });
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        if(isReadOnly()){
            imageBtns.setVisible(false);
            commitAndCloseBtn.setVisible(false);
        }
    }
    
    

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPostCommit(DataContext.PostCommitEvent event) {
        displayImage();
        updateImageButtons(getEditedEntity().getLogo() != null);
    }


    private void updateImageButtons(boolean enable) {
        downloadImageBtn.setEnabled(enable);
        clearImageBtn.setEnabled(enable);
    }

    public void onDownloadImageBtnClick() {
        if (getEditedEntity().getLogo() != null)
            exportDisplay.show(getEditedEntity().getLogo(), ExportFormat.OCTET_STREAM);
    }

    public void onClearImageBtnClick() {
        getEditedEntity().setLogo(null);
        displayImage();
    }

    private void displayImage() {
        if (getEditedEntity().getLogo() != null) {
            image.setSource(FileDescriptorResource.class).setFileDescriptor(getEditedEntity().getLogo());
            image.setVisible(true);
        } else {
            image.setVisible(false);
        }
    }
}