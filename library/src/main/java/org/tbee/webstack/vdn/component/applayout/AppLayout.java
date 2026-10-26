package org.tbee.webstack.vdn.component.applayout;

import org.tbee.webstack.vdn.component.mixin.ComponentMixin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Vaadin {@link com.vaadin.flow.component.applayout.AppLayout} with a listener for the drawer's opened state.
 *
 * The web component only exposes {@code isDrawerOpened()} (a synchronised getter), so there is no Java event
 * when the drawer is opened or closed. This subclass listens to the web component's {@code drawer-opened-changed}
 * event, so dependent components (like the {@link DrawerSlider}s) can follow the state regardless of whether the
 * drawer was toggled by a {@code DrawerToggle}, the Escape key, a backdrop click, or a {@code setDrawerOpened} call.
 */
public class AppLayout extends com.vaadin.flow.component.applayout.AppLayout
implements ComponentMixin<AppLayout> {

    private final List<Consumer<Boolean>> drawerOpenedChangedListeners = new ArrayList<>();

    public AppLayout() {
        getElement().addEventListener("drawer-opened-changed", event -> notifyDrawerOpenedChanged());
    }

    /**
     * Notified whenever the drawer is opened or closed, with the new opened state.
     */
    public AppLayout onDrawerStateChanged(Consumer<Boolean> listener) {
        drawerOpenedChangedListeners.add(listener);
        return this;
    }

    private void notifyDrawerOpenedChanged() {
        boolean drawerOpened = isDrawerOpened();
        drawerOpenedChangedListeners.forEach(listener -> listener.accept(drawerOpened));
    }
}
