package com.company;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.print.Doc;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReportCommand implements Command{
    private Catalog catalog;
    private String argument;

    public ReportCommand(String argument, Catalog catalog) {
        this.catalog = catalog;
        this.argument = argument;
    }

    @Override
    public void execute() {
        if (argument.toLowerCase().equals("html")) {
            try {
                FileOutputStream file = new FileOutputStream("catalog.html");
                Writer writer = new BufferedWriter(new OutputStreamWriter(file, StandardCharsets.UTF_8));

                writer.write("<html>\n\t<head> \n \t\t<title> catalog </title> \n\t</head> \n");
                writer.write("\t<body> \n");

                for (Document document : catalog.getDocuments()) {
                    writer.write("\t\t<p> " + document.toString() + "</p>\n");
                }

                writer.write("\t</body> \n</html>");

                writer.flush();
                file.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        else
        if (argument.toLowerCase().equals("excel")) {
            ApachePOIExcelWrite.createExcel("catalog.xlsx", catalog);
        }
        if (argument.toLowerCase().equals("pdf")) {
            try {
                PDDocument pdf = new PDDocument();

                PDPage blankPage = new PDPage();
                pdf.addPage(blankPage);

                PDPage page = pdf.getPage(0);

                PDPageContentStream contentStream = new PDPageContentStream(pdf, page);
                contentStream.beginText();
                contentStream.setFont( PDType1Font.TIMES_ROMAN, 16 );
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(25, 725);

                for (Document document : catalog.getDocuments()){
                    contentStream.showText(document.toString());
                    contentStream.newLine();
                }

                contentStream.endText();
                contentStream.close();

                pdf.save("catalog.pdf");

                pdf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Unavailable type of format");
        }
    }
}
