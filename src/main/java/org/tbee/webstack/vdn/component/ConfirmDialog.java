package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.ComponentEventListener;

public class ConfirmDialog extends com.vaadin.flow.component.confirmdialog.ConfirmDialog {

    public ConfirmDialog(String header, String text) {
        super(header, text, "Ok", e -> {});
    }

    public ConfirmDialog onConfirm(Runnable v) {
        addConfirmListener((ComponentEventListener<ConfirmEvent>) event -> v.run());
        return this;
    }

    public ConfirmDialog confirmText(String v) {
        setConfirmText(v);
        return this;
    }

    public ConfirmDialog onCancel(Runnable v) {
        setCancelable(true);
        addConfirmListener((ComponentEventListener<ConfirmEvent>) event -> v.run());
        return this;
    }

    public ConfirmDialog cancelText(String v) {
        setCancelText(v);
        return this;
    }

    public ConfirmDialog onReject(Runnable v) {
        setRejectable(true);
        addConfirmListener((ComponentEventListener<ConfirmEvent>) event -> v.run());
        return this;
    }

    public ConfirmDialog rejectText(String v) {
        setRejectText(v);
        return this;
    }
}
