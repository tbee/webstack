package org.tbee.webstack.vdn.component.applayout;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/// Pull-tab at the left edge of the page that opens the drawer.
/// It is only visible while the drawer is closed.
/// ```java
/// // in the AppLayout extending class
/// getElement().appendChild(drawerSliderOut.getElement());
/// ```
public class DrawerSliderOut extends DrawerSlider {

    public DrawerSliderOut(AppLayout appLayout) {
        this(appLayout, VaadinIcon.ARROW_CIRCLE_RIGHT.create(), null);
    }

    public DrawerSliderOut(AppLayout appLayout, Icon icon, String tooltip) {
        super(appLayout, true, icon, tooltip);
    }
}
