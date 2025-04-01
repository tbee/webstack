package org.tbee.webstack.vdn.component.fontawesome;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;

/// Font Awesome (FAS) based Anchor icon
/// https://fontawesome.com/search?o=r&s=solid&ip=classic
///
/// ```java
/// new FASAnchorIcon(href, "fas", "fa-map-pin")
/// ```
public class FASAnchorIcon extends Anchor {

    public FASAnchorIcon(String href, String... classNames) {
        Span spanIcon = new Span();
        for (String className : classNames) {
            spanIcon.addClassName(className);
        }

        Span spanWrapper = new Span(spanIcon);
        spanWrapper.addClassName("icon");

        add(spanWrapper);
        setHref(href);
        setTarget("_blank");
    }

    public FASAnchorIcon href(String v) {
        setHref(v);
        return this;
    }

    public FASAnchorIcon target(String v) {
        setTarget(v);
        return this;
    }
}
