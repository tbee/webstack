package org.tbee.webstack.vdn.component.applayout;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/// Pull-tab at the right edge of the drawer that closes it.
/// It is only visible while the drawer is open.
/// '''java
/// addToDrawer(new VerticalLayout(drawerSliderIn).lean().widthFull().alignItems(FlexComponent.Alignment.END));
/// '''
public class DrawerSliderIn extends DrawerSlider {

    public DrawerSliderIn(AppLayout appLayout) {
        this(appLayout, VaadinIcon.ARROW_CIRCLE_LEFT.create(), null);
    }

    public DrawerSliderIn(AppLayout appLayout, Icon icon, String tooltip) {
        super(appLayout, false, icon, tooltip);
    }
}
