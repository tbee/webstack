package org.tbee.webstack.vdn.component.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import org.tbee.webstack.BuildInfo;
import org.tbee.webstack.vdn.component.FormLabel;
import org.tbee.webstack.vdn.component.TogglingDiv;
import org.tbee.webstack.vdn.component.html.Div;
import org.tbee.webstack.vdn.component.html.H1;
import org.tbee.webstack.vdn.component.html.Image;
import org.tbee.webstack.vdn.component.orderedlayout.VerticalLayout;

/**
 * The login page: the logo above the {@link LoginForm}, side by side while the page is wide enough and stacked
 * below that width.
 * <p>
 * It carries no text and no route of its own: the application owns the page, its title and its way to the
 * registration form (see {@link #title(String)} and {@link #register(String, Class)}), and it supplies the image
 * of the logo through its own stylesheet through {@link #logoClassname(String)}. The layer around the logo and the
 * form is in login-form-extended.css, which is also what makes the title readable on any background image.
 */
@StyleSheet("context://org/tbee/webstack/vdn/component/login-form-extended.css?" + BuildInfo.CACHEBUSTER)
public class LoginFormExtended extends Div {

    /** The logo and the form are drawn at the width of a form's fields. */
    private static final String DEFAULT_MAXWIDTH = "312px";
    private static final int DEFAULT_TOGGLE_AT = 700;

    private final LoginForm loginForm = new LoginForm();
    private final Image logoImage = new Image();
    private final H1 titleH1 = new H1().classNames("loginTitle");
    private final Div logoPanel;
    private final VerticalLayout loginFormLayout;
    private final VerticalLayout contentLayout;

    public LoginFormExtended() {
        super();

        // fill the width, the height follows from the aspect ratio of the image so it is not distorted
        logoImage.addClassName("loginLogo");
        logoPanel = new Div(logoImage)
                .sizeIncludesPadding()
                .classNames("loginLogoPanel");

        // the login form, with the way to the registration form added to it by register(...)
        loginFormLayout = new VerticalLayout(loginForm)
                .lean().spacing(false)
                .alignItems(FlexComponent.Alignment.CENTER);

        // the logo and the login form must be the same width
        maxWidth(DEFAULT_MAXWIDTH);

        // these two toggle horizontal/vertical based on the width of the page, not of the window
        TogglingDiv logoAndFormDiv = new TogglingDiv(logoPanel, loginFormLayout).toggleAt(DEFAULT_TOGGLE_AT);

        contentLayout = new VerticalLayout(logoAndFormDiv)
                .sizeFull()
                .alignItems(FlexComponent.Alignment.CENTER)
                .justifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        add(contentLayout);
    }

    /** The title above the logo; without it the page shows only the logo and the form. */
    public LoginFormExtended title(String v) {
        titleH1.setText(v);
        if (titleH1.getParent().isEmpty()) {
            contentLayout.addComponentAsFirst(titleH1);
        }
        return this;
    }

    /** Adds a link to the given view below the form, to let a new visitor register. */
    public LoginFormExtended register(String text, Class<? extends Component> view) {
        loginFormLayout.add(new FormLabel(text)
                .cursorPointer()
                .onClick(e -> UI.getCurrent().navigate(view)));
        return this;
    }

    /** The width of the logo and the form; 312px by default, so both line up with a form's fields. */
    public LoginFormExtended maxWidth(String v) {
        logoPanel.maxWidth(v);
        loginFormLayout.maxWidth(v);
        return this;
    }

    /**
     * The class name that supplies the image of the logo from the application's stylesheet (via {@code content:
     * url(...)}), so the same component can show a different illustration per theme.
     */
    public LoginFormExtended logoClassname(String v) {
        logoImage.addClassName(v);
        return this;
    }

    public LoginFormExtended action(String v) {
        loginForm.setAction(v);
        return this;
    }

    public LoginFormExtended forgotPasswordListener(ComponentEventListener<AbstractLogin.ForgotPasswordEvent> v) {
        loginForm.addForgotPasswordListener(v);
        return this;
    }

    public LoginFormExtended i18n(LoginI18n v) {
        loginForm.setI18n(v);
        return this;
    }

    public void setError(boolean v) {
        loginForm.setError(v);
    }
}
