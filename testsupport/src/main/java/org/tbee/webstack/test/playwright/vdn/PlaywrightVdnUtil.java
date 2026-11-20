package org.tbee.webstack.test.playwright.vdn;

import com.microsoft.playwright.Page;

public class PlaywrightVdnUtil {

    /**
     * Wait until the Flow client is no longer busy.
     * <p>
     * This deliberately does not look at vaadin-connection-indicator. That element only gets its
     * loading attribute once a request is actually in flight, and the inner
     * div.v-loading-indicator only becomes visible after an additional delay. So right after a
     * keystroke or a scroll it still reports "not loading" because the request has not been sent
     * yet, the assertion passes immediately, and the test races ahead. That gap is what the
     * sleep(200) was bridging. Flow's client.isActive() in contrast is true from the moment the
     * request is queued, and vaadin-combo-box.loading covers the data provider round trip.
     */
    static public void waitForIdle(Page page) {
        page.waitForFunction("""
                () => {
                    const clients = window.Vaadin && window.Vaadin.Flow && window.Vaadin.Flow.clients;
                    if (!clients) return false;
                    return Object.values(clients).every(c => typeof c.isActive !== 'function' || !c.isActive());
                }
                """, null, new Page.WaitForFunctionOptions().setTimeout(30 * 1000));
    }

}
