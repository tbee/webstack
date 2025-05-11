package org.tbee.webstack.vdn.component.orderedlayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.ScrollerVariant;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.ThemeVariantMixin;

public class Scroller extends com.vaadin.flow.component.orderedlayout.Scroller
implements ComponentMixin<Scroller>, SizeMixin<Scroller>, StyleMixin<Scroller>, ThemeVariantMixin<ScrollerVariant, Scroller> {
    public Scroller() {
    }

    public Scroller(Component content) {
        super(content);
    }

    public Scroller(Component content, ScrollDirection scrollDirection) {
        super(content, scrollDirection);
    }

    public Scroller(ScrollDirection scrollDirection) {
        super(scrollDirection);
    }

    public Scroller scrollDirection(ScrollDirection scrollDirection) {
        setScrollDirection(scrollDirection);
        return this;
    }
}
