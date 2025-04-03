package org.tbee.webstack.vdn.component.fontawesome;

import com.vaadin.flow.component.html.Span;

public class FasIcon extends Span {
    public FasIcon(String... classNames) {
        addClassNames(classNames);
    }

    public FasIcon color(String v) {
        getStyle().set("color", v);
        return this;
    }
}
