package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

public interface FlexMixin<C extends FlexComponent> {

    default C alignItems(FlexComponent.Alignment alignment) {
        ((C)this).setAlignItems(alignment);
        return (C)this;
    }

    default C alignSelf(FlexComponent.Alignment alignment, HasElement... components) {
        ((C)this).setAlignSelf(alignment, components);
        return (C)this;
    }

    default C flexGrow(double flexGrow, HasElement... components) {
        ((C)this).setFlexGrow(flexGrow, components);
        return (C)this;
    }

    default C flexShrink(double flexShrink, HasElement... components) {
        ((C)this).setFlexShrink(flexShrink, components);
        return (C)this;
    }

    default C justifyContentMode(FlexComponent.JustifyContentMode justifyContentMode) {
        ((C)this).setJustifyContentMode(justifyContentMode);
        return (C)this;
    }
}
