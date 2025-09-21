package org.tbee.webstack.vdn.component.splitlayout;

import com.vaadin.flow.component.Component;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

public class SplitLayout extends com.vaadin.flow.component.splitlayout.SplitLayout
implements ComponentMixin<SplitLayout>, SizeMixin<SplitLayout>, StyleMixin<SplitLayout> {

    public SplitLayout() {
    }

    public SplitLayout(Orientation orientation) {
        super(orientation);
    }

    public SplitLayout(Component primaryComponent, Component secondaryComponent) {
        super(primaryComponent, secondaryComponent);
    }

    public SplitLayout(Component primaryComponent, Component secondaryComponent, Orientation orientation) {
        super(primaryComponent, secondaryComponent, orientation);
    }

    public SplitLayout splitterPosition(double position) {
        setSplitterPosition(position);
        return this;
    }

    public SplitLayout orientation(Orientation orientation) {
        setOrientation(orientation);
        return this;
    }

    public SplitLayout primaryStyle(String styleName, String value) {
        setPrimaryStyle(styleName, value);
        return this;
    }

    public SplitLayout secondaryStyle(String styleName, String value) {
        setSecondaryStyle(styleName, value);
        return this;
    }
}
