package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.HasText;

public interface TextMixin<C extends HasText> {

    default C text(String text) {
        ((C)this).setText(text);
        return (C)this;
    }
    default String text() {
        return ((C)this).getText();
    }
}
