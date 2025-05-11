package org.tbee.webstack.vdn.component.contextmenu;

import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.function.SerializableRunnable;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

public class MenuItem extends com.vaadin.flow.component.contextmenu.MenuItem
implements ComponentMixin<MenuItem>, StyleMixin<MenuItem> {
    public MenuItem(ContextMenu contextMenu, SerializableRunnable contentReset) {
        super(contextMenu, contentReset);
    }
}
