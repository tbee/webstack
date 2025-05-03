package org.tbee.webstack.vdn.component;

import com.helger.commons.base64.Base64;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

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
	private final FileBuffer buffer = new FileBuffer();
	private final Upload upload = new Upload(buffer);

	public ImageUpload() {
		setPadding(false);
		add(new Div(image), upload);

		image.setHeight("100px");

		upload.setMaxFiles(1);
		upload.setMaxFileSize(512 * 1024);
		upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

		upload.addSucceededListener(event -> {
			String mimeType = URLConnection.guessContentTypeFromName(event.getFileName());
			try (
					InputStream inputStream = inputStream();
			) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				inputStream.transferTo(outputStream);
				image.setSrc("data:" + mimeType + ";base64," + Base64.encodeBytes(outputStream.toByteArray()));
			}
            catch (IOException e) {
				LOG.error("Failed to read image from upload", e);
                throw new RuntimeException(e);
            }
        });
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
		return buffer.getFileName();
	}

	/// @return inputstream, caller needs to close this.
	public InputStream inputStream() {
		if (buffer.getFileData() == null) {
			return null;
		}
		return buffer.getInputStream();
	}

	public boolean hasUpload() {
		return buffer.getFileData() != null;
	}
}