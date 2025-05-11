package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.server.AbstractStreamResource;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

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

    public Image src(String src) {
        super.setSrc(src);
        return this;
    }
}
