package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.server.AbstractStreamResource;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.TextMixin;

public class Anchor extends com.vaadin.flow.component.html.Anchor
implements ComponentMixin<Anchor>, TextMixin<Anchor> {
    public Anchor() {
    }

    public Anchor(String href, String text) {
        super(href, text);
    }

    public Anchor(String href, String text, AnchorTarget target) {
        super(href, text, target);
    }

    public Anchor(AbstractStreamResource href, String text) {
        super(href, text);
    }

    public Anchor(String href, Component... components) {
        super(href, components);
    }

    public Anchor target(AnchorTarget target) {
        setTarget(target);
        return this;
    }
}
