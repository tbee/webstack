package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://vaadin.com/forum/t/proper-way-to-include-css-for-a-custom-component-class-in-vaadin-14/159725
// https://vaadin.com/docs/latest/flow/advanced/loading-resources
@StyleSheet("context://org/tbee/webstack/vdn/component/select-one-pill-bar.css")
public class SelectOnePillBar<T> extends HorizontalLayout implements StyleMixin<SelectOnePillBar<T>> {
    private final Map<NativeButton, T> buttonToKey = new HashMap<>();
    private final Map<T, NativeButton> keyToButton = new HashMap<>();
    private final Div div = new Div();

    public SelectOnePillBar() {
        div.addClassName("select-one-pill-bar");
        add(div);
    }

    public SelectOnePillBar<T> add(T key, Component component) {
        NativeButton nativeButton = new NativeButton();
        nativeButton.add(component);
        div.add(nativeButton);
        buttonToKey.put(nativeButton, key);
        keyToButton.put(key, nativeButton);

        nativeButton.addClickListener(event -> {
            select(nativeButton);
        });

        return this;
    }

    private void select(NativeButton nativeButton) {
        div.getChildren().forEach(child -> child.removeClassNames("selected"));
        nativeButton.addClassNames("selected");
    }

    private List<T> selection() {
        return div.getChildren()
                .filter(child -> child.hasClassName("selected"))
                .map(child -> buttonToKey.get(child))
                .toList();
    }

    public T selected() {
        List<T> selection = selection();
        return selection.isEmpty() ? null : selection.getFirst();
    }
    public SelectOnePillBar<T> selected(T v) {
        select(keyToButton.get(v));
        return this;
    }
}
