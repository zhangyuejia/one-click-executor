package com.zhangyj.oneclick.core.common.util;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;

/**
 * @author zhanglj
 */
public class PdfUtils {

    /**
     * 读取pdf内容，免费只能前
     * @param pdfPath pdf地址
     * @return pdf内容
     */
    public static String getPdfContentUseItext(String pdfPath) {
        StringBuilder builder = new StringBuilder();
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfPath);
            for (int i = 0; i < reader.getNumberOfPages(); i++) {
                builder.append(PdfTextExtractor.getTextFromPage(reader, i + 1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return builder.toString();
    }

    /**
     * 读取pdf内容
     * @param pdfPath pdf地址
     * @return pdf内容
     */
    public static String getPdfContentUsePdfBox(String pdfPath) throws Exception {
        PDDocument document = null;
        try {
            File file = new File(pdfPath);
            document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if (document != null) {
                document.close();
            }
        }
	}
}
