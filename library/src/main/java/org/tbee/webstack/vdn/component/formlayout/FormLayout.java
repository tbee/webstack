package org.tbee.webstack.vdn.component.formlayout;

import com.vaadin.flow.component.Component;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

public class FormLayout extends com.vaadin.flow.component.formlayout.FormLayout
implements ComponentMixin<FormLayout>, StyleMixin<FormLayout>, SizeMixin<FormLayout> {

    public FormLayout() {
    }

    public FormLayout(Component... components) {
        super(components);
    }

    public FormLayout responsiveSteps(ResponsiveStep... steps) {
        setResponsiveSteps(steps);
        return this;
    }
}
