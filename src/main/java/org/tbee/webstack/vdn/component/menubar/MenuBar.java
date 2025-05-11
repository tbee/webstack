package org.tbee.webstack.vdn.component.menubar;

import com.vaadin.flow.component.menubar.MenuBarVariant;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.ThemeVariantMixin;

public class MenuBar extends com.vaadin.flow.component.menubar.MenuBar
implements ComponentMixin<MenuBar>, SizeMixin<MenuBar>, StyleMixin<MenuBar>, ThemeVariantMixin<MenuBarVariant, MenuBar> {
    public MenuBar() {
    }
}
