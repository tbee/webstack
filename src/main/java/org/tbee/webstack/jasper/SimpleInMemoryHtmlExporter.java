package org.tbee.webstack.jasper;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

import java.io.IOException;

public class SimpleInMemoryHtmlExporter {
    private final HtmlExporter htmlExporter = new HtmlExporter();

    public SimpleInMemoryHtmlExporter(JasperPrint jasperPrint) {
        htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    }

    public void toResponse(HttpServletResponse httpServletResponse) throws IOException, JRException {
        httpServletResponse.setHeader("Content-Type", "text/html");

        SimpleHtmlExporterOutput htmlOutput = new SimpleHtmlExporterOutput(httpServletResponse.getOutputStream());
        htmlOutput.setImageHandler(new InMemoryEmbeddedImageHtmlResourceHandler());
        htmlExporter.setExporterOutput(htmlOutput);

        htmlExporter.exportReport();
    }
}
