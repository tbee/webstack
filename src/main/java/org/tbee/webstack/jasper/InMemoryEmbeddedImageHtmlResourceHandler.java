package org.tbee.webstack.jasper;

import com.helger.commons.base64.Base64;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryEmbeddedImageHtmlResourceHandler implements HtmlResourceHandler  {
    private final Map<String, String> images = new HashMap<>();

    @Override
    public void handleResource(String id, byte[] data) {
        String mimeType = URLConnection.guessContentTypeFromName(id);
        images.put(id, "data:" + mimeType + ";base64," + Base64.encodeBytes(data));
    }

    @Override
    public String getResourcePath(String id) {
        return images.get(id);
    }
}
