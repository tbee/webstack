package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;

/// Deprecated: use ConfirmationDialog instead
@Deprecated(since = "1.0.0", forRemoval = true)
public class CancelDialog extends ConfirmationDialog {

    public CancelDialog(String title) {
        this(title, null);
    }

    public CancelDialog(String title, Component component) {
        super(title, component);
        closeIsCancel();
    }
}
