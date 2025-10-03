package org.tbee.webstack.jasper;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;

import java.io.IOException;

public class SimplePdfExporter {
    private final JRPdfExporter pdfExporter = new JRPdfExporter();

    public SimplePdfExporter(JasperPrint jasperPrint) {
        pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    }

    public void toResponse(HttpServletResponse httpServletResponse) throws IOException, JRException {
        httpServletResponse.setHeader("Content-Type", "application/pdf");
        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(httpServletResponse.getOutputStream()));
        pdfExporter.exportReport();
    }
}
