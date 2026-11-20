package org.tbee.webstack.test.playwright.vdn;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ComboBox {

    private final Locator locator;

    public ComboBox(Locator locator) {
        this.locator = locator;
    }

    public ComboBox select(String text) {
        return select(text, true);
    }

    public ComboBox select(String text, boolean exactMatch) {
        locator.locator("input").fill(text); // prefilter the list, this will open the list dropdown
        waitForFilterApplied(text);
        // locator.locator("#toggleButton").click();
        waitForIdle();

        Locator scroller = locator.locator("vaadin-combo-box-scroller");
        Locator item = scroller.locator("vaadin-combo-box-item").getByText(text, new Locator.GetByTextOptions().setExact(exactMatch));

        while (item.count() == 0) { // this re-evaluates the locator
            double before = scrollTop(scroller);
            scroller.evaluate("el => el.scrollTop += el.clientHeight");
            waitForIdle();

            double after = scrollTop(scroller);
            if (before == after) {
                throw new AssertionError("Item '" + text + "' not found in combo box dropdown");
            }
        }
        item.click();
        return this;
    }

    private double scrollTop(Locator scroller) {
        // scrollTop is fractional on hidpi / zoomed viewports, so Playwright hands back a Double there
        return ((Number) scroller.evaluate("el => el.scrollTop")).doubleValue();
    }

    /**
     * The combo box debounces filter changes before it asks the server for the first page, so
     * directly after fill() nothing is loading yet. Wait until the typed text has actually landed
     * in the filter property; only after that is waiting for "not loading" meaningful.
     */
    private void waitForFilterApplied(String text) {
        locator.page().waitForFunction(
                "([el, expected]) => el.filter === expected",
                new Object[] { locator.elementHandle(), text },
                new Page.WaitForFunctionOptions().setTimeout(30 * 1000));
    }

    /**
     * Wait until neither the Flow client nor the combo box itself is busy.
     */
    private void waitForIdle() {
        PlaywrightVdnUtil.waitForIdle(locator.page());
        locator.page().waitForFunction("el => !el.loading", locator.elementHandle(), new Page.WaitForFunctionOptions().setTimeout(30 * 1000));
    }
}
