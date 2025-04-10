package org.tbee.webstack.vdn.component.fontawesome;

import com.vaadin.flow.component.html.Span;
import org.tbee.webstack.vdn.component.StyleMixin;
import org.tbee.webstack.vdn.component.googlematerial.GmfIcon;

public class FasIcon extends Span
implements StyleMixin<FasIcon> {
    public FasIcon(String... classNames) {
        addClassNames(classNames);
    }

    public FasIcon color(String v) {
        getStyle().set("color", v);
        return this;
    }

    public FasIcon size(String v) {
        getStyle().set("font-size", v);
        return this;
    }
}
