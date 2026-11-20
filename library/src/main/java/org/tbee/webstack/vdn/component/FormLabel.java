package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.NativeLabel;
import org.tbee.webstack.BuildInfo;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

// The label looks like a FormLayout label. Its CSS is theme neutral, the theme
// dependent values are tokens in the theme stylesheets (see form-label.css).
@StyleSheet("context://org/tbee/webstack/vdn/component/form-label.css?" + BuildInfo.CACHEBUSTER)
public class FormLabel extends NativeLabel
implements ComponentMixin<FormLabel>, SizeMixin<FormLabel>, StyleMixin<FormLabel>, ClickNotifier<FormLabel> {

    public FormLabel(String label) {
        super(label);
        addClassName("form-item-label");
    }
}
