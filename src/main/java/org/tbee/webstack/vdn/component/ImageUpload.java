package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.FileRejectedEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

/// Combination of the image and upload component.
/// Will preview the uploaded file in the image component.
///
///  Usage:
/// ```java
/// 		ImageUpload imageUpload = new ImageUpload();
/// 		imageUpload.src(...);
/// 		if (imageUpload.hasUpload()) {
/// 			try (
/// 				InputStream inputStream = imageUpload.inputStream();
/// 			) {
/// 				String extension = FilenameUtils.getExtension(imageUpload.filename());
/// 				//...
///            }
///             catch (IOException e) {
/// 				LOGGER.error("Failed to write icon", e);
///                 throw new RuntimeException(e);
///             }
///         }
/// ```
@Uses(Upload.class)
public class ImageUpload extends HorizontalLayout {
	private static final Logger LOG = LoggerFactory.getLogger(ImageUpload.class);
	private static final String TRANSPARENT = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";

	private final Image image = new Image(TRANSPARENT, "");
	private final Div imageDiv = new Div(image);
	private final Upload upload;
	private String filename;
	private String mimeType;
	private byte[] data;

	public ImageUpload() {
		InMemoryUploadHandler inMemoryHandler = UploadHandler.inMemory((metadata, bytes) -> {
			this.filename = metadata.fileName();
			this.mimeType = metadata.contentType();
			this.data = bytes;
			image.setSrc("data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(bytes));
		});
		upload = new Upload(inMemoryHandler);
		upload.setMaxFiles(1);
		upload.setMaxFileSize(512 * 1024);
		upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

//        upload.addFailedListener((ComponentEventListener<FailedEvent>) event -> {
//            showError(event.getReason().getMessage());
//        });
        upload.addFileRejectedListener((ComponentEventListener<FileRejectedEvent>) event -> {
            showError(event.getErrorMessage());
        });
		image.setHeight("110px");
		image.setWidth("110px");
		image.getStyle()
				.set("object-fit", "cover") // https://developer.mozilla.org/en-US/docs/Web/CSS/Reference/Properties/object-fit
				.set("border-radius", "var(--lumo-border-radius-m)")
				.set("border", "1px dotted gray");
		image.addClickListener(e -> showPopup());

		setPadding(false);
		add(imageDiv, upload);
	}

	private void showError(String event) {
        Notification notification = Notification.show(event, 5000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

	private void showPopup() {
		Image popupImage = new Image();
		popupImage.setSrc(this.image.getSrc());
		popupImage.getStyle()
				.set("object-fit", "contain") // https://developer.mozilla.org/en-US/docs/Web/CSS/Reference/Properties/object-fit
				.set("border-radius", "var(--lumo-border-radius-m)");
		popupImage.setSizeFull();

		ConfirmationDialog.confirm("", popupImage)
				.sizeFull()
				.maxHeight(90, Unit.PERCENTAGE)
				.maxWidth(90, Unit.PERCENTAGE)
				.rejectable()
				.rejectText("")
				.rejectIcon(VaadinIcon.TRASH.create())
				.onReject(() -> {
					this.filename = null;
					this.mimeType = null;
					this.data = null;
					image.setSrc(TRANSPARENT);
				})
				.show();
	}

	public boolean hasImage() {
		return image.getSrc() != null && !TRANSPARENT.equals(image.getSrc());
	}

	public ImageUpload src(String v) {
		image.setSrc(v);
		return this;
	}

	public ImageUpload filetypes(String[] v) {
		upload.setAcceptedFileTypes(v);
		return this;
	}

	public ImageUpload maxFileSize(int v) {
		upload.setMaxFileSize(v);
		return this;
	}

	public ImageUpload imageHeight(String v) {
		image.setHeight(v);
		return this;
	}

	public ImageUpload imageWidth(String v) {
		image.setWidth(v);
		return this;
	}

	public String filename() {
		return this.filename;
	}
	public String mimeType() {
		return this.mimeType;
	}

	/// @return inputstream, caller needs to close this.
	public InputStream inputStream() {
		return new ByteArrayInputStream(data);
	}

	public boolean hasUpload() {
		return data != null;
	}
}