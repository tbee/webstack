package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.card.CardVariant;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.ThemeVariantMixin;

public class Card extends com.vaadin.flow.component.card.Card
implements ComponentMixin<Card>, ThemeVariantMixin<CardVariant, Card>, SizeMixin<Card>, StyleMixin<Card> {

    public Card() {
        super();
    }

    public Card(Component... components) {
        super();
        add(components);
    }

    public Card contents(Component... components) {
        add(components);
        return this;
    }

    public Card title(String title) {
        setTitle(title);
        return this;
    }
    public Card title(Component title) {
        setTitle(title);
        return this;
    }

    public Card subtitle(String v) {
        setSubtitle(v);
        return this;
    }
    public Card subtitle(Component v) {
        setSubtitle(v);
        return this;
    }

    public Card header(Component v) {
        setHeader(v);
        return this;
    }
    public Card headerPrefix(Component v) {
        setHeaderPrefix(v);
        return this;
    }
    public Card headerSuffix(Component v) {
        setHeaderSuffix(v);
        return this;
    }

    public Card footer(Component v) {
        addToFooter(v);
        return this;
    }

    public Card media(Component v) {
        setMedia(v);
        return this;
    }
}
