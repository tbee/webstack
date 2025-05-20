package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;

/// Deprecated: use ConfirmationDialog instead
@Deprecated(since = "1.0.0", forRemoval = true)
public class OkCancelDialog extends ConfirmationDialog {

    public OkCancelDialog(String title) {
        this(title, null);
    }

    public OkCancelDialog(String title, Component component) {
        super(title, component);
        confirmable();
        closeIsCancel();
    }

    public OkCancelDialog onOk(Runnable v) {
        onConfirm(v);
        return this;
    }

    public OkCancelDialog okLabel(String v) {
        confirmText(v);
        return this;
    }
}
