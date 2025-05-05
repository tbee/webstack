package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Unit;

public interface SizeMixin<C extends HasSize> {
    default C sizeFull() {
        ((C)this).setSizeFull();
        return (C)this;
    }

    default C widthFull() {
        ((C)this).setWidthFull();
        return (C)this;
    }
    default C width(String width) {
        ((C)this).setWidth(width);
        return (C)this;
    }
    default C width(float width, Unit unit) {
        ((C)this).setWidth(width, unit);
        return (C)this;
    }

    default C heightFull() {
        ((C)this).setHeightFull();
        return (C)this;
    }
    default C height(String height) {
        ((C)this).setHeight(height);
        return (C)this;
    }
    default C height(float height, Unit unit) {
        ((C)this).setHeight(height, unit);
        return (C)this;
    }
}
