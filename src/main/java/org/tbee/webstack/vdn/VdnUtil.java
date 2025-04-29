package org.tbee.webstack.vdn;

import com.vaadin.flow.component.UI;

public class VdnUtil {
    public static void reloadPage() {
        UI.getCurrent().getPage().reload();
    }
}
