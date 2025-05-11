package org.tbee.webstack.vdn.component.icon;

import com.vaadin.flow.component.icon.VaadinIcon;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

public class Icon extends com.vaadin.flow.component.icon.Icon
implements StyleMixin<Icon> {
    public Icon() {
    }

    public Icon(VaadinIcon icon) {
        super(icon);
    }

    public Icon(String icon) {
        super(icon);
    }

    public Icon(String collection, String icon) {
        super(collection, icon);
    }

    public Icon size(String size) {
        super.setSize(size);
        return this;
    }
}
