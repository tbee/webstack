package org.tbee.webstack.vdn;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import org.jspecify.annotations.NonNull;

public class VaadinUtil {

    public static <FROM extends Component, TO extends Component> @NonNull ClickEvent<TO> clone(ClickEvent<FROM> event) {
        return new ClickEvent<>((Component) event.getSource(),
                event.isFromClient(),
                event.getScreenX(),
                event.getScreenY(),
                event.getClientX(),
                event.getClientY(),
                event.getClickCount(),
                event.getButton(),
                event.isCtrlKey(),
                event.isShiftKey(),
                event.isAltKey(),
                event.isMetaKey());
    }
}
