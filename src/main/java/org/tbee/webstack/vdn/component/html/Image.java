package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.streams.DownloadHandler;
import org.tbee.webstack.vdn.component.Button;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

import java.util.Locale;
import java.util.function.Consumer;

public class Image extends com.vaadin.flow.component.html.Image
implements ComponentMixin<Image>, SizeMixin<Image>, StyleMixin<Image> {
    public Image() {
    }

    public Image(String src, String alt) {
        super(src, alt);
    }

    public Image(AbstractStreamResource src, String alt) {
        super(src, alt);
    }

    public Image(DownloadHandler downloadHandler, String alt) {
        super(downloadHandler, alt);
    }

    public Image(byte[] imageContent, String imageName) {
        super(imageContent, imageName);
    }

    public Image(byte[] imageContent, String imageName, String mimeType) {
        super(imageContent, imageName, mimeType);
    }

    public Image src(String src) {
        super.setSrc(src);
        return this;
    }

    /// Determine how the image is rendered inside the available area:
    ///
    /// CONTAIN
    /// The replaced content is scaled to maintain its aspect ratio while fitting within the element's content box. The entire object is made to fill the box, while preserving its aspect ratio, so the object will be "letterboxed" or "pillarboxed" if its aspect ratio does not match the aspect ratio of the box.
    ///
    /// COVER
    /// The replaced content is sized to maintain its aspect ratio while filling the element's entire content box. If the object's aspect ratio does not match the aspect ratio of its box, then the object will be clipped to fit.
    ///
    /// FILL
    /// The replaced content is sized to fill the element's content box. This is the initial value. The entire object will completely fill the box. If the object's aspect ratio does not match the aspect ratio of its box, then the object will be stretched to fit.
    ///
    /// NONE
    /// The replaced content is not resized.
    ///
    /// SCALE-DOWN
    /// The content is sized as if none or contain were specified, whichever would result in a smaller concrete object size.
    ///
    /// https://developer.mozilla.org/en-US/docs/Web/CSS/Reference/Properties/object-fit
    public Image fit(Fit v) {
        getStyle().set("object-fit", v.toString().toLowerCase(Locale.ROOT).replace("_", "-"));
        return this;
    }
    public enum Fit { CONTAIN, COVER, FILL, NONE, SCALE_DOWN }

    public Image onClick(Consumer<ClickEvent<com.vaadin.flow.component.html.Image>> listener) {
        addClickListener(listener::accept);
        return this;
    }
}
