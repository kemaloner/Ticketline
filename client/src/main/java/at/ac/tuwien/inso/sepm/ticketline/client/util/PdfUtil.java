package at.ac.tuwien.inso.sepm.ticketline.client.util;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.TicketSeatDTO;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfUtil {


    public static final String BOLD = PdfUtil.class.getResource("/font/CaviarDreams_Bold.ttf").toString();
    public static final String REGULAR = PdfUtil.class.getResource("/font/CaviarDreams.ttf").toString();

    static Double nettTotal = 0d;
    static Double taxTotal = 0d;
    static Double totalSum = 0d;

    public String createPDF(InvoiceDTO invoice, List<TicketSeatDTO> ticketSeatDTOList) throws FileNotFoundException{
        nettTotal = 0d;
        taxTotal = 0d;
        totalSum = 0d;

        PdfFont bold = null;
        PdfFont regular = null;

        try {
            regular = PdfFontFactory.createFont(REGULAR, true);
            bold = PdfFontFactory.createFont(BOLD, true);
        } catch (IOException e) {
            throw  new FileNotFoundException(e.getMessage());
        }

        DateTimeFormatter dateTimeFormatterFile = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");

        String property = "java.io.tmpdir";
        String tempDir = System.getProperty(property);
        String dest = tempDir + "invoice-" + invoice.getInvoiceNumber() + "-" + dateTimeFormatterFile.format(LocalDateTime.now()) + ".pdf";


        PdfWriter pdfWriter = new PdfWriter(dest);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        document.setFont(regular).setFontSize(12);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        document.add(
            new Paragraph()
                .setTextAlignment(TextAlignment.CENTER)
                .setMultipliedLeading(1)
                .add(new Text("Invoice\n").setFontSize(18).setFont(bold))
        );


        Table table = new Table(new UnitValue[]{
            new UnitValue(UnitValue.PERCENT, 60),
            new UnitValue(UnitValue.PERCENT, 40)})
            .setWidth(500);

        Cell cell1 = new Cell();
        Cell cell2 = new Cell();
        cell1.add(new Paragraph("Date : " + dateTimeFormatter.format(invoice.getDateOfIssue())).setMultipliedLeading(1f));
        cell1.setBorder(Border.NO_BORDER);
        cell2.add(new Paragraph("Invoice Number : " + invoice.getInvoiceNumber()).setMultipliedLeading(1f));
        cell2.setBorder(Border.NO_BORDER);
        table.addCell(new Cell().setBorder(Border.NO_BORDER));
        table.addCell(cell1);
        table.addCell(new Cell().setBorder(Border.NO_BORDER));
        table.addCell(cell2);
        document.add(new Paragraph("\n"));
        document.add(table);

        ImageData data = ImageDataFactory.create(PdfUtil.class.getResource("/image/ticketlineLogo.png"));
        Image logo = new Image(data);
        logo.setHeight(80);
        logo.setWidth(120);
        logo.setFixedPosition(30, 735);

        BarcodeQRCode barcodeQRCode = new BarcodeQRCode(String.valueOf(invoice.getInvoiceNumber()));
        Image image = new Image(barcodeQRCode.createFormXObject(pdfDocument));
        image.setHeight(100);
        image.setWidth(100);
        image.setFixedPosition(pdfDocument.getDefaultPageSize().getWidth()/2 - image.getImageWidth(),0);


        document.add(getAddressTable(invoice, bold));

        Color red = new DeviceRgb(255, 0, 0);

        if(invoice.getTicket().getStatus() == TicketStatus.IC){
            document.add(new Paragraph().add(new Text("Items   ").setFont(bold).setFontSize(15))
                .add(new Text("Canceled").setFontSize(15).setFont(bold).setFontColor(red)));
        }else{
            document.add(new Paragraph().add(new Text("Items").setFont(bold).setFontSize(15)));
        }


        document.add(getLineItemTable(ticketSeatDTOList, bold));

        Table price = new Table(new UnitValue[]{
            new UnitValue(UnitValue.PERCENT, 72),
            new UnitValue(UnitValue.PERCENT, 10),
            new UnitValue(UnitValue.PERCENT, 15),
            new UnitValue(UnitValue.PERCENT, 3)})
            .setWidth(500);

        if(invoice.getTicket().getStatus() != TicketStatus.IC){
            Paragraph paragraph1 = new Paragraph();
            Paragraph paragraph2 = new Paragraph();

            paragraph1.add(new Text("NETT :\n").setFont(bold).setTextAlignment(TextAlignment.RIGHT))
                .add(new Text("TAX :\n").setFont(bold).setTextAlignment(TextAlignment.RIGHT))
                .add(new Text("TOTAL :\n").setFont(bold).setTextAlignment(TextAlignment.RIGHT));

            paragraph2.add(new Text(String.format("%.2f\n", nettTotal)))
                .add(new Text(String.format("%.2f\n", taxTotal)))
                .add(new Text(String.format("%.2f\n", totalSum)));

            price.addCell(new Cell().setBorder(Border.NO_BORDER));
            price.addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).add(paragraph1));
            price.addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).add(paragraph2));
            price.addCell(new Cell().add(new Paragraph().add(new Text("€\n")).add(new Text("€\n")).add(new Text("€\n"))).setBorder(Border.NO_BORDER));

            document.add(price);
        } else{
            Table cancelPrice = new Table(new UnitValue[]{
                new UnitValue(UnitValue.PERCENT, 72),
                new UnitValue(UnitValue.PERCENT, 10),
                new UnitValue(UnitValue.PERCENT, 15),
                new UnitValue(UnitValue.PERCENT, 3)})
                .setWidth(500);

            Paragraph paragraph1 = new Paragraph();
            Paragraph paragraph2 = new Paragraph();
            Paragraph cancelParagraph = new Paragraph();

            paragraph1.add(new Text("NETT :\n").setFont(bold).setTextAlignment(TextAlignment.RIGHT))
                .add(new Text("TAX :\n").setFont(bold).setTextAlignment(TextAlignment.RIGHT))
                .add(new Text("TOTAL :\n").setFont(bold).setTextAlignment(TextAlignment.RIGHT));

            paragraph2.add(new Text(String.format("%.2f\n", nettTotal)).setLineThrough())
                .add(new Text(String.format("%.2f\n", taxTotal)).setLineThrough())
                .add(new Text(String.format("%.2f\n", totalSum)).setLineThrough());

            cancelParagraph
                .add(new Text("0.00\n").setFontColor(red))
                .add(new Text("0.00\n").setFontColor(red))
                .add(new Text("0.00\n").setFontColor(red));

            cancelPrice.addCell(new Cell().setBorder(Border.NO_BORDER));
            cancelPrice.addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).add(paragraph1).setFontColor(red));
            cancelPrice.addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).add(cancelParagraph));
            cancelPrice.addCell(new Cell().add(new Paragraph().add(new Text("€\n")).add(new Text("€\n")).add(new Text("€\n"))).setBorder(Border.NO_BORDER));

            price.addCell(new Cell().setBorder(Border.NO_BORDER));
            price.addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).add(paragraph1));
            price.addCell(new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT).add(paragraph2));
            price.addCell(new Cell().add(new Paragraph().add(new Text("€\n")).add(new Text("€\n")).add(new Text("€\n"))).setBorder(Border.NO_BORDER));

            document.add(price);

            document.add(cancelPrice);

        }



        document.add(logo);
        document.add(image);
        document.close();

        return dest;
    }

    private Table getAddressTable(InvoiceDTO invoice, PdfFont bold) {
        Table table = new Table(new UnitValue[]{
            new UnitValue(UnitValue.PERCENT, 50),
            new UnitValue(UnitValue.PERCENT, 50)})
            .setWidth(500);
        if(invoice.getTicket().getCustomer().getId() != 1){
            table.addCell(getPartyAddress("To:",
                invoice.getTicket().getCustomer().getFirstname() + " " + invoice.getTicket().getCustomer().getSurname(),
                invoice.getTicket().getCustomer().getAddress(),
                invoice.getTicket().getCustomer().getPhoneNumber(),
                invoice.getTicket().getCustomer().getEmail(),
                bold));
        }else{
            table.addCell(new Cell().setBorder(Border.NO_BORDER));
        }

        table.addCell(getPartyAddress("From:",
            "TICKETLINE - 10 OG",
            "Wiedner Hauptstrasse 10",
            "01 3024342-002",
            "ticketline10@tuwien.ac.at",
            bold));
        table.addCell(new Cell().setBorder(Border.NO_BORDER));
        table.addCell(getPartyTax("ATU12345678", bold));

        return table;
    }

    private Cell getPartyAddress(String who, String name,
                                       String address, String number, String mail, PdfFont bold) {
        Paragraph p = new Paragraph()
            .setMultipliedLeading(1.0f)
            .add(new Text(who).setFont(bold)).add("\n")
            .add(name).add("\n")
            .add(address).add("\n")
            .add(number).add("\n")
            .add(mail).add("\n");
        Cell cell = new Cell()
            .setBorder(Border.NO_BORDER)
            .add(p);
        return cell;
    }
    private Cell getPartyTax(String taxSchema, PdfFont bold) {
        Paragraph p = new Paragraph()
            .setFontSize(10).setMultipliedLeading(1.0f)
            .add(new Text("Tax ID:").setFont(bold));
        p.add("\n")
            .add(taxSchema);
        return new Cell().setBorder(Border.NO_BORDER).add(p);
    }

    private Table getLineItemTable(List<TicketSeatDTO> ticketSeatDTOList, PdfFont bold) {
        Table table = new Table(
                new UnitValue[]{
                        new UnitValue(UnitValue.PERCENT, 43.75f),
                        new UnitValue(UnitValue.PERCENT, 12.5f),
                        new UnitValue(UnitValue.PERCENT, 6.25f),
                        new UnitValue(UnitValue.PERCENT, 12.5f),
                        new UnitValue(UnitValue.PERCENT, 12.5f),
                        new UnitValue(UnitValue.PERCENT, 12.5f)})
                .setWidth(500)
                .setMarginTop(10).setMarginBottom(10);
        table.addHeaderCell(createCell("Item", bold));
        table.addHeaderCell(createCell("Price", bold));
        table.addHeaderCell(createCell("Qty", bold));
        table.addHeaderCell(createCell("Subtotal", bold));
        table.addHeaderCell(createCell("Tax %13", bold));
        table.addHeaderCell(createCell("Total", bold));

        fillSellList(ticketSeatDTOList ,table);

        return table;
    }

    private Cell createCell(String text) {
        return new Cell().setPadding(5f)
            .add(new Paragraph(text)
                .setMultipliedLeading(1));
    }

    private Cell createCell(String text, PdfFont font) {
        return new Cell().setPadding(5f)
            .add(new Paragraph(text)
                .setFont(font)
                .setMultipliedLeading(1));
    }

    private void fillSellList(List<TicketSeatDTO> ticketSeatDTOList, Table table){
        Map<Double, List<TicketSeatDTO>> map = new HashMap<>();
        for (TicketSeatDTO ticketSeatDTO : ticketSeatDTOList) {
            double key = ticketSeatDTO.getSeat().getMultiplier();
            if(!map.containsKey(key)){
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(ticketSeatDTO);
        }

        for (Map.Entry<Double, List<TicketSeatDTO>> entry : map.entrySet()){
            List<TicketSeatDTO> list = entry.getValue();
            TicketSeatDTO ticketSeat = list.get(0);
            TicketDTO ticket = ticketSeat.getTicket();
            String title = ticket.getPerformance().getEvent().getTitle();
            String sector = " Sector " + ticketSeat.getSeat().getSector();

            double gross = round(ticketSeat.getSeat().getMultiplier() * ticket.getPerformance().getBasePrice(), 2);
            int amount = list.size();
            double net = round(gross * 100 / 113,2);
            //double tax = round(gross * 13 / 113,2);
            double sub_total_net = round(gross * 100 / 113 * amount, 2);
            double sub_total_tax = round(gross * 13 / 113 * amount, 2);
            double total = sub_total_net + sub_total_tax;

            nettTotal += sub_total_net;
            taxTotal += sub_total_tax;
            totalSum += total;

            table.addCell(createCell(title + sector));
            table.addCell(createCell(
                String.format("%.2f", net))
                .setTextAlignment(TextAlignment.RIGHT));
            table.addCell(createCell(String.valueOf(amount))
                .setTextAlignment(TextAlignment.RIGHT));
            table.addCell(createCell(
                String.format("%.2f", sub_total_net))
                .setTextAlignment(TextAlignment.RIGHT));
            table.addCell(createCell(
                String.format("%.2f", sub_total_tax))
                .setTextAlignment(TextAlignment.RIGHT));
            table.addCell(createCell(
                String.format("%.2f", total))
                .setTextAlignment(TextAlignment.RIGHT));
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
