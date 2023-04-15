package com.zhangyj.cmdexecutor.core.common.util;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfUtils {

    /**
     * 读取pdf内容，免费只能前
     * @param pdfPath pdf地址
     * @return pdf内容
     */
    public static String getPdfContentUseIText(String pdfPath) {
        StringBuilder sb = new StringBuilder();
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfPath);
            int pages = reader.getNumberOfPages();
            for (int i = 1; i <= pages; i++) {
                sb.append(PdfTextExtractor.getTextFromPage(reader, i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return sb.toString();
    }

    /**
     * 读取pdf内容
     * @param pdfPath pdf地址
     * @return pdf内容
     */
    public static String getPdfContentUsePdfBox(String pdfPath) throws Exception{
        File file = new File(pdfPath);
        PDDocument document = PDDocument.load(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }
}
