package com.zhangyj.cmdexecutor.core.common.util;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;

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
     * 读取pdf内容，免费只能前
     * @param pdfPath pdf地址
     * @return pdf内容
     */
    public static String getPdfContentUseSpire(String pdfPath) {
        PdfDocument doc = new PdfDocument();
        doc.loadFromFile(pdfPath);
        StringBuilder sb = new StringBuilder();
        for(int i= 0;i<doc.getPages().getCount();i++){
            PdfPageBase page = doc.getPages().get(i);
            String extractText = page.extractText(true);
            sb.append(extractText);
        }
        return sb.toString();
    }
}
