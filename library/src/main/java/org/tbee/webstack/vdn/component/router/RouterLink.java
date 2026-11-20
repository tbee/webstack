package org.tbee.webstack.vdn.component.router;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.router.internal.HasUrlParameterFormat;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;

public class RouterLink extends com.vaadin.flow.router.RouterLink
implements ComponentMixin<RouterLink> {
    public RouterLink() {
    }

    public RouterLink(Class<? extends Component> navigationTarget) {
        super(navigationTarget);
    }

    public RouterLink(String text, Class<? extends Component> navigationTarget) {
        super(text, navigationTarget);
    }

    public <T, C extends Component & HasUrlParameter<T>> RouterLink(Class<? extends C> navigationTarget, T parameter) {
        super(navigationTarget, HasUrlParameterFormat.getParameters(parameter));
    }

    public RouterLink(Class<? extends Component> navigationTarget, RouteParameters parameters) {
        super(navigationTarget, parameters);
    }

    public RouterLink(String text, Class<? extends Component> navigationTarget, RouteParameters parameters) {
        super(text, navigationTarget, parameters);
    }

    public RouterLink(Router router, Class<? extends Component> navigationTarget) throws IllegalArgumentException {
        super(router, navigationTarget);
    }

    public RouterLink(Router router, String text, Class<? extends Component> navigationTarget) throws IllegalArgumentException {
        super(router, text, navigationTarget);
    }

    public <T, C extends Component & HasUrlParameter<T>> RouterLink(Router router, Class<? extends C> navigationTarget, T parameter) {
        super(router, navigationTarget, parameter);
    }

    public <T, C extends Component & HasUrlParameter<T>> RouterLink(Router router, String text, Class<? extends C> navigationTarget, T parameter) {
        super(router, text, navigationTarget, parameter);
    }

    public RouterLink(Router router, Class<? extends Component> navigationTarget, RouteParameters parameters) {
        super(router, navigationTarget, parameters);
    }

    public RouterLink(Router router, String text, Class<? extends Component> navigationTarget, RouteParameters parameters) {
        super(router, text, navigationTarget, parameters);
    }
}
