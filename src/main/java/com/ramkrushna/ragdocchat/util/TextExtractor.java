package com.ramkrushna.ragdocchat.util;

import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

public class TextExtractor {
    public static String extractText(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new IllegalArgumentException("File is empty");
        }
        try {
            if (fileName.endsWith(".pdf")) {
                return extractTextFromPdf(file.getInputStream());
            } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
                return extractTextFromDocx(file.getInputStream());
            } else if (fileName.endsWith(".txt")) {
                return new String(file.getBytes());
            } else {
                throw new IllegalArgumentException("Unsupported file type");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text", e);
        }

        // return "";
    }

    private static String extractTextFromPdf(InputStream inputStream) throws Exception {
        System.out.println("PDF extraction started");

        PDDocument document = PDDocument.load(inputStream);
        System.out.println("PDF loaded");

        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);

        String text = stripper.getText(document);
        System.out.println("PDF text extracted");

        document.close();
        System.out.println("PDF closed");

        return text;
    }

    private static String extractTextFromDocx(InputStream inputStream) throws Exception {
        XWPFDocument document = new XWPFDocument(inputStream);
        StringBuilder text = new StringBuilder();
        document.getParagraphs()
                .forEach(p -> text.append(p.getText()).append("\n"));
        document.close();
        return text.toString();
    }
}
