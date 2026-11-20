package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.tbee.webstack.vdn.component.html.Image;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.FlexMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.ThemableLayoutMixin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.vaadin.flow.component.Unit.PERCENTAGE;

public class ImageGallery extends HorizontalLayout
implements ComponentMixin<ImageGallery>, SizeMixin<ImageGallery>, StyleMixin<ImageGallery>, ThemableLayoutMixin<ImageGallery>, FlexMixin<ImageGallery> {

    List<PhotoPanel> photoPanels = new ArrayList<>();
    private String deleteText = "Delete";

    public ImageGallery() {
        wrap();
    }

    public ImageGallery deleteText(String v) {
        this.deleteText = v;
        return this;
    }

    public ImageGallery addImage(byte[] bytes, String contentType) {
        return addImage(bytes, contentType, null);
    }

    public ImageGallery addImage(byte[] bytes, String contentType, Runnable deleteCallback) {
        PhotoPanel photoPanel = new PhotoPanel(bytes, contentType, deleteCallback);
        photoPanels.add(photoPanel);
        add(photoPanel);
        return this;
    }

    class PhotoPanel extends HorizontalLayout {
        private final byte[] bytes;
        private final String contentType;
        private final Runnable deleteCallback;

        public PhotoPanel(byte[] bytes, String contentType, Runnable deleteCallback) {
            this.bytes = bytes;
            this.contentType = contentType;
            this.deleteCallback = deleteCallback;

            spacing(false);
            lean();

            add(new Image(bytes, "photo")
                    .fit(Image.Fit.COVER)
                    .size(150, 150, Unit.PIXELS)
                    .margin("4px")
                    .onClick(e -> showPopup()));
        }

        private void showPopup() {

            // Prepare popup dialog
            ConfirmationDialog confirmationDialog = ConfirmationDialog.confirm("", image(bytes, contentType))
                    .closeIsCancel()
                    .sizeFull()
                    .maxSize(90, PERCENTAGE);

            // Add previous and next buttons (not for a single photo)
            AtomicInteger index = new AtomicInteger(photoPanels.indexOf(this));
            if (photoPanels.size() > 1) {
                title(index.get(), confirmationDialog);
                Button previousButton = new Button("<").onClick(e -> {
                    if (index.get() <= 0) {
                        return;
                    }
                    replaceImage(index.decrementAndGet(), confirmationDialog);
                });
                Button nextButton = new Button(">").onClick(e -> {
                    if (index.get() >= photoPanels.size() - 1) {
                        return;
                    }
                    replaceImage(index.incrementAndGet(), confirmationDialog);
                });
                confirmationDialog.getHeader().addComponentAsFirst(nextButton);
                confirmationDialog.getHeader().addComponentAsFirst(previousButton);
            }

            // Show remove button is necessary
            if (deleteCallback != null) {
                confirmationDialog
                        .rejectable()
                        .rejectText(deleteText)
                        .onReject(deleteCallback);
            }

            confirmationDialog.show();
        }

        private Image image(byte[] bytes, String contentType) {
            return new Image(bytes, "zoom", contentType)
                    .fit(Image.Fit.CONTAIN)
                    .size(99, PERCENTAGE)
                    .sizeIncludesPadding();
        }

        private void replaceImage(int newIndex, ConfirmationDialog confirmationDialog) {
            title(newIndex, confirmationDialog);
            PhotoPanel nextPhotoPanel = photoPanels.get(newIndex);
            Image newImage = image(nextPhotoPanel.bytes, nextPhotoPanel.contentType);

            confirmationDialog.removeAll();
            confirmationDialog.add(newImage);
        }

        private void title(int newIndex, ConfirmationDialog confirmationDialog) {
            confirmationDialog.setHeaderTitle((newIndex + 1) + " / " + photoPanels.size());
        }
    }
}
