package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.theme.lumo.LumoIcon;

import java.util.function.Supplier;

public class OkCancelDialog extends Dialog {

    Button okButton = new Button("Ok");
    private Supplier<Boolean> onOk = null;

    public OkCancelDialog(String title) {
        this(title, null);
    }

    public OkCancelDialog(String title, Component component) {
        setHeaderTitle(title);

        // Close
        Button closeButton = new Button(LumoIcon.CROSS.create(), e -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        getHeader().add(closeButton);

        // Ok
        okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        okButton.addClickListener(e -> ok());
        getFooter().add(okButton);

        // Content
        if (component != null) {
            add(component);
        }
    }

    private void ok() {
        if (onOk != null) {
            if (!onOk.get()) {
                return;
            }
        }
        close();
    }

    /// When the supplier returns true, the dialog is closed, on false it is not.
    /// This allows for a validation when ok is pressed, and possible errors to prevent the dialog to be closed.
    public OkCancelDialog onOk(Supplier<Boolean> v) {
        this.onOk = v;
        return this;
    }

    public OkCancelDialog onOk(Runnable v) {
        return onOk(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                v.run();
                return true;
            }
        });
    }

    public OkCancelDialog okLabel(String v) {
        this.okButton.setText(v);
        return this;
    }

    public OkCancelDialog width(float width, Unit unit) {
        this.setWidth(width, unit);
        return this;
    }

    public OkCancelDialog height(float height, Unit unit) {
        this.setHeight(height, unit);
        return this;
    }
}
