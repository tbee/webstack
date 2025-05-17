package org.tbee.webstack.vdn.component.orderedlayout;

import com.vaadin.flow.component.Component;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.FlexMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.ThemableLayoutMixin;

public class HorizontalLayout extends com.vaadin.flow.component.orderedlayout.HorizontalLayout
implements ComponentMixin<HorizontalLayout>, SizeMixin<HorizontalLayout>, StyleMixin<HorizontalLayout>, ThemableLayoutMixin<HorizontalLayout>, FlexMixin<HorizontalLayout> {

    public HorizontalLayout() {
    }

    public HorizontalLayout(Component... children) {
        super(children);
    }

    public HorizontalLayout(JustifyContentMode justifyContentMode, Component... children) {
        super(justifyContentMode, children);
    }

    public HorizontalLayout(Alignment alignment, Component... children) {
        super(alignment, children);
    }

    public HorizontalLayout alignItems(Alignment alignment) {
        super.setAlignItems(alignment);
        return this;
    }

}
