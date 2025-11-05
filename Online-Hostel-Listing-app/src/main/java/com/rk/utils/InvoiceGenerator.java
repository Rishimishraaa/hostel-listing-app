package com.rk.utils;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rk.entity.Payment;

public class InvoiceGenerator {

    public static String generateInvoice(String studentName, String email, double amount, String razorpayPaymentId, Payment payment) throws Exception {
        String filePath = "invoice_" + razorpayPaymentId + ".pdf";

        Document document = new Document(PageSize.A4, 36, 36, 64, 36);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // ================== TITLE ===================
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, new BaseColor(0, 102, 204));
        Paragraph title = new Paragraph("Payment Invoice", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Generated on: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"))));
        document.add(new Paragraph(" "));

        // ================== INVOICE DETAILS ===================
        PdfPTable invoiceTable = new PdfPTable(2);
        invoiceTable.setWidthPercentage(100);
        invoiceTable.setSpacingBefore(10f);
        invoiceTable.setSpacingAfter(10f);

        PdfPCell cell1 = new PdfPCell(new Phrase("Invoice No: INV-" + razorpayPaymentId));
        PdfPCell cell2 = new PdfPCell(new Phrase("Payment Date: " + LocalDate.now().toString()));

        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);

        invoiceTable.addCell(cell1);
        invoiceTable.addCell(cell2);
        document.add(invoiceTable);

        document.add(new Paragraph(" "));

        // ================== CUSTOMER DETAILS ===================
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        document.add(new Paragraph("Billed To:", sectionFont));

        PdfPTable custTable = new PdfPTable(1);
        custTable.setWidthPercentage(100);
        custTable.setSpacingBefore(5f);

        PdfPCell c1 = new PdfPCell(new Phrase(studentName));
        PdfPCell c2 = new PdfPCell(new Phrase(email));

        c1.setBorder(Rectangle.NO_BORDER);
        c2.setBorder(Rectangle.NO_BORDER);

        custTable.addCell(c1);
        custTable.addCell(c2);
        document.add(custTable);

        document.add(new Paragraph(" "));

        // ================== PAYMENT DETAILS TABLE ===================
        document.add(new Paragraph("Payment Summary:", sectionFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4, 2, 2});

        // Header row
        PdfPCell h1 = new PdfPCell(new Phrase("Description", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        PdfPCell h2 = new PdfPCell(new Phrase("Date", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        PdfPCell h3 = new PdfPCell(new Phrase("Amount (INR)", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

        BaseColor headerColor = new BaseColor(230, 230, 250);
        h1.setBackgroundColor(headerColor);
        h2.setBackgroundColor(headerColor);
        h3.setBackgroundColor(headerColor);

        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);

        // Data row
        table.addCell(payment.getBooking().getHostel().getName() + 
            " Room No: " + payment.getBooking().getRoom().getRoomNumber() + 
            " (" + payment.getBooking().getRoom().getFloorNumber() + " Floor)");
        table.addCell(payment.getMonth().toString());
        table.addCell("₹ " + amount);

        // Footer Row - Total
        PdfPCell t1 = new PdfPCell(new Phrase("Total"));
        t1.setColspan(2);
        t1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell t2 = new PdfPCell(new Phrase("₹ " + amount));
        t2.setHorizontalAlignment(Element.ALIGN_LEFT);
        BaseColor totalBg = new BaseColor(240, 240, 240);
        t1.setBackgroundColor(totalBg);
        t2.setBackgroundColor(totalBg);

        table.addCell(t1);
        table.addCell(t2);

        document.add(table);

        document.add(new Paragraph(" "));

     // ================== SIGNATURE SECTION ===================
        document.add(new Paragraph(" "));
        Font signTitle = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(0, 102, 204));
        Font signSub = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.DARK_GRAY);

        PdfPTable signTable = new PdfPTable(2);
        signTable.setWidthPercentage(100);
        signTable.setWidths(new float[]{1, 1});
        signTable.setSpacingBefore(30f);

        // Left side empty (for spacing)
        PdfPCell empty = new PdfPCell(new Phrase(" "));
        empty.setBorder(Rectangle.NO_BORDER);

        // Right side signature area
        PdfPCell signCell = new PdfPCell();
        signCell.setBorder(Rectangle.NO_BORDER);
        signCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        // ✅ Load signature image safely from src/main/resources
        try {
            ClassLoader classLoader = InvoiceGenerator.class.getClassLoader();
            java.net.URL resource = classLoader.getResource("signature.jpg"); // <-- your image name

            if (resource != null) {
                com.itextpdf.text.Image signatureImage = com.itextpdf.text.Image.getInstance(resource);
                signatureImage.scaleToFit(100, 50); // adjust size as needed
                signatureImage.setAlignment(Element.ALIGN_RIGHT);
                signCell.addElement(signatureImage);
            } else {
                System.out.println("⚠️ Signature image not found in resources folder!");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Failed to load signature image: " + e.getMessage());
        }

        // Signature line & text
        Paragraph line = new Paragraph("__________________________", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY));
        line.setAlignment(Element.ALIGN_RIGHT);
        Paragraph signedBy = new Paragraph("Accepted & Authorized By", signTitle);
        signedBy.setAlignment(Element.ALIGN_RIGHT);
        Paragraph orgName = new Paragraph("RK Hostel Management", signSub);
        orgName.setAlignment(Element.ALIGN_RIGHT);

        signCell.addElement(line);
        signCell.addElement(signedBy);
        signCell.addElement(orgName);

        signTable.addCell(empty);
        signTable.addCell(signCell);
        document.add(signTable);



        document.add(new Paragraph(" "));

        // ================== FOOTER THANKS ===================
        Paragraph thanks = new Paragraph("Thank you for your payment!", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLDITALIC, BaseColor.DARK_GRAY));
        thanks.setAlignment(Element.ALIGN_CENTER);
        document.add(thanks);

        Paragraph note = new Paragraph("For any queries, contact us at m.abhishek2027@gmail.com", new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY));
        note.setAlignment(Element.ALIGN_CENTER);
        document.add(note);

        document.close();
        writer.close();

        return filePath;
    }
}
