package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.FailedEvent;
import com.vaadin.flow.component.upload.FileRejectedEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.server.streams.FileUploadHandler;
import com.vaadin.flow.server.streams.InMemoryUploadCallback;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
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

	private final Image image = new Image();
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


		setPadding(false);
		add(new Div(image), upload);
		image.setHeight("100px");
	}

    private void showError(String event) {
        Notification notification = Notification.show(event, 5000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
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