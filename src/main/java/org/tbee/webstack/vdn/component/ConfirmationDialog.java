package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoIcon;
import org.tbee.webstack.vdn.component.html.Span;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;

import java.util.function.Function;

/// A twist on ConfirmDialog, which does not necessarily close on button click (so a confirm can have an error message).
/// This is done by using the `onXXX(Function)` callback methods; returning false does not close the dialog.
/// It also provides the dialog as a parameter to the callback method, so it can be closed later.
public class ConfirmationDialog extends Dialog
implements ComponentMixin<ConfirmationDialog>, SizeMixin<ConfirmationDialog> {

    private final Button confirmButton = new Button("Ok").visible(false);
    private final Button cancelButton = new Button("Cancel").visible(false);
    private final Button rejectButton = new Button("Reject").visible(false);
    private final Button closeButton = new Button(LumoIcon.CROSS.create()).visible(false).onClick(e -> cancelButton.click());
    private final Registration confirmButtonDefaultBehavior = confirmButton.addClickListener(e -> close());
    private final Registration cancelButtonDefaultBehavior = cancelButton.addClickListener(e -> close());
    private final Registration rejectButtonDefaultBehavior = rejectButton.addClickListener(e -> close());

    public ConfirmationDialog() {
        this(null, (Component)null);
    }

    public ConfirmationDialog(String title) {
        this(title, (Component)null);
    }

    public ConfirmationDialog(String title, String text) {
        this(title, new Span(text));
    }

    public ConfirmationDialog(String title, Component component) {
        setHeaderTitle(title == null || title.isEmpty() ? " " : title);

        // Add buttons
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickShortcut(Key.ENTER);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickShortcut(Key.ESCAPE);
        rejectButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        rejectButton.addClickShortcut(Key.PAUSE);
        footer(rejectButton, cancelButton, confirmButton);

        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        header(closeButton);

        // Content
        if (component != null) {
            add(component);
        }
    }

    public ConfirmationDialog escapeIsCancel() {
        getElement().addEventListener("keydown", e -> {
            cancelButton.click();
        }).setFilter("event.code === 'Escape'").addEventData("event.stopPropagation()");
        return this;
    }
    public ConfirmationDialog closeIsCancel() {
        closeButton.visible(true);
        return this;
    }

    public ConfirmationDialog overlayRole(String role) {
        super.setOverlayRole(role);
        return this;
    }

    public ConfirmationDialog header(Component... components) {
        getHeader().add(components);
        return this;
    }

    public ConfirmationDialog footer(Component... components) {
        getFooter().add(components);
        return this;
    }

    public ConfirmationDialog show() {
        super.open();
        return this;
    }


    // Confirm
    public ConfirmationDialog confirmable() {
        confirmButton.visible(true);
        return this;
    }
    public ConfirmationDialog confirmText(String v) {
        confirmButton.text(v);
        return confirmable();
    }
    public ConfirmationDialog confirmIcon(Icon v) {
        confirmButton.setIcon(v);
        return confirmable();
    }
    public ConfirmationDialog confirmTheme(String v) {
        confirmButton.setThemeName(v);
        return confirmable();
    }
    public ConfirmationDialog confirmThemeDanger() {
        return confirmTheme("error primary");
    }
    public ConfirmationDialog onConfirm(Runnable v) {
        confirmButtonDefaultBehavior.remove();
        confirmButton
                .onClick(event -> {
                    close();
                    v.run();
                });
        return confirmable();
    }
    public ConfirmationDialog onConfirm(Function<ConfirmationDialog, Boolean> v) {
        confirmButtonDefaultBehavior.remove();
        confirmButton
                .onClick(event -> {
                    if (v.apply(this)) {
                        close();
                    }
                });
        return confirmable();
    }

    // Cancel
    public ConfirmationDialog cancelable() {
        cancelButton.visible(true);
        return this;
    }
    public ConfirmationDialog cancelText(String v) {
        cancelButton.text(v);
        return cancelable();
    }
    public ConfirmationDialog cancelIcon(Icon v) {
        cancelButton.setIcon(v);
        return cancelable();
    }
    public ConfirmationDialog cancelTheme(String v) {
        cancelButton.setThemeName(v);
        return cancelable();
    }
    public ConfirmationDialog onCancel(Runnable v) {
        cancelButtonDefaultBehavior.remove();
        cancelButton
                .onClick(event -> {
                    close();
                    v.run();
                });
        return cancelable();
    }
    public ConfirmationDialog onCancel(Function<ConfirmationDialog, Boolean> v) {
        cancelButtonDefaultBehavior.remove();
        confirmButton
                .onClick(event -> {
                    if (v.apply(this)) {
                        close();
                    }
                });
        return cancelable();
    }

    // Reject
    public ConfirmationDialog rejectable() {
        rejectButton.visible(true);
        return this;
    }
    public ConfirmationDialog rejectText(String v) {
        rejectButton.text(v);
        return rejectable();
    }
    public ConfirmationDialog rejectIcon(Icon v) {
        rejectButton.setIcon(v);
        return rejectable();
    }
    public ConfirmationDialog rejectTheme(String v) {
        rejectButton.setThemeName(v);
        return rejectable();
    }
    public ConfirmationDialog onReject(Runnable v) {
        rejectButtonDefaultBehavior.remove();
        rejectButton
                .onClick(event -> {
                    close();
                    v.run();
                });
        return rejectable();
    }
    public ConfirmationDialog onReject(Function<ConfirmationDialog, Boolean> v) {
        rejectButtonDefaultBehavior.remove();
        confirmButton
                .onClick(event -> {
                    if (v.apply(this)) {
                        close();
                    }
                });
        return rejectable();
    }

    // =================================================
    // Factory

    public static ConfirmationDialog confirm(String title, String text) {
        return new ConfirmationDialog(title, text)
                .confirmable()
                .escapeIsCancel();
    }
    public static ConfirmationDialog confirm(String title, Component component) {
        return new ConfirmationDialog(title, component)
                .confirmable()
                .escapeIsCancel();
    }

    public static ConfirmationDialog confirmCancel(String title, String text) {
        return new ConfirmationDialog(title, text)
                .confirmable()
                .cancelable()
                .escapeIsCancel();
    }
    public static ConfirmationDialog confirmCancel(String title, Component component) {
        return new ConfirmationDialog(title, component)
                .confirmable()
                .cancelable()
                .escapeIsCancel();
    }
}
