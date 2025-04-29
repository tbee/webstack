package org.tbee.webstack.vdn.component.googlematerial;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Span;
import org.tbee.webstack.vdn.component.StyleMixin;
import org.tbee.webstack.vdn.component.fontawesome.FasIcon;

// https://fonts.google.com/icons
// https://developers.google.com/fonts/docs/material_icons
@StyleSheet("https://fonts.googleapis.com/icon?family=Material+Icons")
public class GmfIcon extends Span
implements StyleMixin<GmfIcon> {
    private String familyName = null;

    public GmfIcon(String iconname) {
        this("material-icons", iconname);
    }

    public GmfIcon(String familyName, String iconName) {
        family(familyName);
        icon(iconName);
    }

    public GmfIcon family(String name) {
        if (familyName != null) {
            removeClassName(familyName);
        }
        addClassNames(name);
        familyName = name;
        return this;
    }

    public GmfIcon icon(String name) {
        setText(name);
        return this;
    }

    public GmfIcon color(String v) {
        getStyle().set("color", v);
        return this;
    }

    public GmfIcon size(String v) {
        getStyle().set("font-size", v);
        return this;
    }

    public GmfIcon opacity(double v) {
        getStyle().set("opacity", "" + v);
        return this;
    }
}
