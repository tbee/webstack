package org.tbee.webstack.vdn.component.applayout;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.TextMixin;

/**
 * A pull-tab that opens ({@link DrawerSliderOut}) or closes ({@link DrawerSliderIn}) the drawer of an
 * {@link AppLayout}. Only the relevant tab is shown: the "out" tab while the drawer is closed, the "in" tab
 * while it is open. The visibility follows the layout's {@code drawer-opened-changed} event, so it stays in
 * sync no matter how the drawer was opened or closed.
 */
abstract public class DrawerSlider extends Div
implements ComponentMixin<DrawerSlider>, StyleMixin<DrawerSlider>, SizeMixin<DrawerSlider>, TextMixin<DrawerSlider> {

    private final AppLayout appLayout;
    private final boolean slideOut;

    protected DrawerSlider(AppLayout appLayout, boolean slideOut, Icon icon, String tooltip) {
        super(icon);
        this.appLayout = appLayout;
        this.slideOut = slideOut;

        if (tooltip != null) {
            getElement().setAttribute("aria-label", tooltip);
            getElement().setAttribute("title", tooltip);
        }
        addClickListener(event -> appLayout.setDrawerOpened(slideOut));

        // follow the drawer state, both now and whenever it changes
        appLayout.onDrawerStateChanged(this::updateVisibility);
        updateVisibility(appLayout.isDrawerOpened());
    }

    private void updateVisibility(boolean drawerOpened) {
        // the "out" tab is shown when closed, the "in" tab when opened
        setVisible(slideOut != drawerOpened);
    }
}
