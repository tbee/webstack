package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.dependency.StyleSheet;

import java.util.Locale;

/// https://flagicons.lipis.dev/
@StyleSheet("https://cdn.jsdelivr.net/gh/lipis/flag-icons@7.2.3/css/flag-icons.min.css")
public class FlagIcon extends ExternalIcon<FlagIcon> {

    public FlagIcon(String iso2Code) {
        addClassName("fi");
        addClassName("fi-" + iso2Code.toLowerCase(Locale.ROOT));
    }

    public FlagIcon square() {
        addClassName("fis");
        return this;
    }
}
