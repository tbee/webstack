package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import org.tbee.webstack.BuildInfo;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link Div} that places its children next to each other while it is wide enough, and stacks them
 * on top of each other once it becomes narrower than the width given to {@link #toggleAt(int)}.
 * <p>
 * The switch reacts to the width of the div itself (a CSS container query), not to the viewport, so it
 * also works when the div sits in a narrow part of a wide page. Because of that the div needs a width
 * that does not depend on its content, so it claims the full available width; cap it with e.g.
 * {@code maxWidth(...)} where needed.
 */
@StyleSheet("context://org/tbee/webstack/vdn/component/toggling-div.css?" + BuildInfo.CACHEBUSTER)
public class TogglingDiv extends Div
implements ComponentMixin<TogglingDiv>, StyleMixin<TogglingDiv>, SizeMixin<TogglingDiv> {

    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    /** The layout the instances share is in toggling-div.css; this marker only scopes the container query below,
     * because every instance may be given its own width at which it switches. */
    private final String marker = "togglingDiv" + SEQUENCE.incrementAndGet();

    private Element styleElement;

    public TogglingDiv(Component... children) {
        super(children);
        addClassNames("togglingDiv", marker);
        widthFull();
    }

    /**
     * Below {@code widthInPx} the children are stacked, at or above it they sit next to each other.
     */
    public TogglingDiv toggleAt(int widthInPx) {
        if (styleElement != null) {
            styleElement.removeFromParent();
        }
        styleElement = new Element("style");
        styleElement.setText("""
                @container (max-width: %2$dpx) {
                    .%1$s > * {
                        flex: 1 0 100%%;
                        max-width: 100%%;
                    }
                }
                """.formatted(marker, widthInPx));
        getElement().appendChild(styleElement);
        return this;
    }
}
