package org.tbee.webstack.vdn.component;

import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

import java.util.function.Consumer;

/// VirtualList extension that uses a listener to fetch the visible indexes.
public class VirtualList<T> extends com.vaadin.flow.component.virtuallist.VirtualList<T>
implements ComponentMixin<VirtualList<T>>, SizeMixin<VirtualList<T>>, StyleMixin<VirtualList<T>> {

    private int visibleIndex;
    private int firstVisibleIndex;
    private int lastVisibleIndex;
    private Consumer<ScrollEvent> scrollListener = event -> {};

    public VirtualList() {
        getElement().addEventListener("scroll", e -> {
            queryVisibleIndexes(() -> scrollListener.accept(new ScrollEvent(visibleIndex, firstVisibleIndex, lastVisibleIndex)));
        }).debounce(200);
    }

    private void queryVisibleIndexes(Runnable runnable) {
        getElement().executeJs("return this.firstVisibleIndex + '/' + this.lastVisibleIndex").then(String.class, s -> {
            String[] split = s.split("/");
            firstVisibleIndex = Integer.parseInt(split[0]);
            lastVisibleIndex = Integer.parseInt(split[1]);
            visibleIndex = firstVisibleIndex + ((lastVisibleIndex - firstVisibleIndex) / 2) - 1;
            runnable.run();
        });
    }

    public int visibleIndex() {
        return visibleIndex;
    }

    public int firstVisibleIndex() {
        return firstVisibleIndex;
    }

    public int lastVisibleIndex() {
        return lastVisibleIndex;
    }

    public record ScrollEvent(int visibleIndex, int firstVisibleIndex, int lastVisibleIndex) {}
    public void addScrollListener(Consumer<ScrollEvent> consumer) {
        this.scrollListener = consumer;
    }
}
