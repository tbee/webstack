package org.tbee.webstack.vdn.component.googlematerial;

import com.vaadin.flow.component.html.Span;
import org.tbee.webstack.vdn.component.StyleMixin;
import org.tbee.webstack.vdn.component.fontawesome.FasIcon;

// https://fonts.google.com/icons
// https://developers.google.com/fonts/docs/material_icons
public class GmfIcon extends Span
implements StyleMixin<GmfIcon> {

    public GmfIcon(String iconname) {
        addClassNames("material-icons");
        setText(iconname);
    }

    public GmfIcon color(String v) {
        getStyle().set("color", v);
        return this;
    }

    public GmfIcon size(String v) {
        getStyle().set("font-size", v);
        return this;
    }
}
