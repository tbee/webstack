package org.tbee.webstack.vdn.component.googlematerial;

import com.vaadin.flow.component.dependency.StyleSheet;
import org.tbee.webstack.vdn.component.ExternalIcon;

/// https://fonts.google.com/icons
/// https://developers.google.com/fonts/docs/material_icons
@StyleSheet("https://fonts.googleapis.com/icon?family=Material+Icons")
public class GmfIcon extends ExternalIcon<GmfIcon> {

    public GmfIcon(String iconName) {
        this("material-icons", iconName);
    }

    public GmfIcon(String familyName, String iconName) {
        addClassName(familyName);
        setText(iconName);
    }
}
