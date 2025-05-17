package org.tbee.webstack.vdn.component.orderedlayout;

import com.vaadin.flow.component.Component;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.FlexMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.ThemableLayoutMixin;

public class VerticalLayout extends com.vaadin.flow.component.orderedlayout.VerticalLayout
implements ComponentMixin<VerticalLayout>, SizeMixin<VerticalLayout>, StyleMixin<VerticalLayout>, ThemableLayoutMixin<VerticalLayout>, FlexMixin<VerticalLayout> {

    public VerticalLayout() {
    }

    public VerticalLayout(Component... children) {
        super(children);
    }

    public VerticalLayout(JustifyContentMode justifyContentMode, Component... children) {
        super(justifyContentMode, children);
    }

    public VerticalLayout(Alignment alignment, Component... children) {
        super(alignment, children);
    }
}
