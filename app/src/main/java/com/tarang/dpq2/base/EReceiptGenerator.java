package com.tarang.dpq2.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;

import com.cloudpos.printer.PrinterDevice;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tarang.dpq2.R;
import com.tarang.dpq2.base.baseactivities.BaseActivity;
import com.tarang.dpq2.base.terminal_sdk.device.SDKDevice;
import com.tarang.dpq2.model.PrinterModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EReceiptGenerator {

    private Element addArabicTextLeft(String text, Font font) {
        PdfPTable naqd = new PdfPTable(1);
        naqd.setWidthPercentage(100);
        naqd.addCell(getCellArabic(text + "\n", PdfPCell.ALIGN_LEFT, font));
        return naqd;
    }

    private Element addArabicTextRight(String text, Font font) {
        PdfPTable naqd = new PdfPTable(1);
        naqd.setWidthPercentage(100);
        naqd.addCell(getCellArabic(text + "\n", PdfPCell.ALIGN_RIGHT, font));
        return naqd;
    }

    private Element addArabicTextCenter(String text, Font font) {
        PdfPTable naqd = new PdfPTable(1);
        naqd.setWidthPercentage(100);
        naqd.addCell(getCellArabic("\n"+text + "\n", PdfPCell.ALIGN_CENTER, font));
        return naqd;
    }

    private Element addEnglishTextLeft(String text, Font font) {
        PdfPTable naqd = new PdfPTable(1);
        naqd.setWidthPercentage(100);
        naqd.addCell(getCell(text + "\n", PdfPCell.ALIGN_LEFT, font));
        return naqd;
    }

    private Element addEnglishTextRight(String text, Font font) {
        PdfPTable naqd = new PdfPTable(1);
        naqd.setWidthPercentage(100);
        naqd.addCell(getCell(text + "\n", PdfPCell.ALIGN_RIGHT, font));
        return naqd;
    }

    private Element addEnglishTextCenter(String text, Font font) {
        PdfPTable naqd = new PdfPTable(1);
        naqd.setWidthPercentage(100);
        naqd.addCell(getCell(text + "\n", PdfPCell.ALIGN_CENTER, font));
        return naqd;
    }

    private PdfPCell getCellArabic(String text, int alignment, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    private PdfPCell getCell(String text, int alignment, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    Context context;

    public EReceiptGenerator(Context context) {
        this.context = context;
    }

    public File downloadReceipt(PrinterModel printerModel) {
        Logger.v("E-recipt : "+printerModel.toString());
        //Create document file
//        Document document = new Document();
        Rectangle rectangle3x5 = new Rectangle(185, 600);
        Document document = new Document(rectangle3x5, 5, 5, 20, 20);
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmm");
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS)
                , "AndroPDF_" + dateFormat.format(Calendar.getInstance().getTime()) + ".pdf"
        );

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            //Open the document
            document.open();
//            document.setPageSize(PageSize.A4);

//            PrinterDevice mPrinter = SDKDevice.getInstance(context).getPrinter();
//            String arial1 = mPrinter.getFontsPath(context, "arial.ttf", true);
//            String arialBold1 = mPrinter.getFontsPath(context, "ARIALBD.TTF", true);
//            String arabic1 = mPrinter.getFontsPath(context, "Tahoma.ttf", true);
//            String arabicBold1 = mPrinter.getFontsPath(context, "tahomabd.ttf", true);
//            String simsun1 = mPrinter.getFontsPath(context, "madam.ttf", true);
            Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.mada_logo_black);

    //        BaseFont base = BaseFont.createFont("assets/fonts/arial.ttf", BaseFont.WINANSI, false);
    //        BaseFont base = BaseFont.createFont("assets/fonts/arial.ttf", "UTF-8", BaseFont.EMBEDDED);
    //        BaseFont baseArabic = BaseFont.createFont("assets/fonts/Tahoma.ttf", BaseFont.IDENTITY_H, true);
    //        BaseFont baseArabic = BaseFont.createFont("assets/fonts/Tahoma.ttf", "UTF-8", BaseFont.EMBEDDED);
//			Font headFont = FontFactory.getFont("Arial", 12, Font.NORMAL, new CMYKColor(0, 0, 0, 255));
//			Font normalFont = FontFactory.getFont("Arial", 8, Font.NORMAL, new CMYKColor(0, 0, 0, 255));
//			Font boldFont = FontFactory.getFont("Arial", 8, Font.NORMAL, new CMYKColor(0, 0, 0, 255));
//			Font basicFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL, new CMYKColor(0, 0, 0, 255));

//            BaseFont base = BaseFont.createFont("assets/subFolder/arial.TTF", "UTF-8",BaseFont.EMBEDDED);
//            BaseFont baseArabic = BaseFont.createFont("assets/subFolder/arial.TTF", "UTF-8",BaseFont.EMBEDDED);
        //    Font urFontName = new Font(base, 12);


            Font font_n10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
            Font font_n9 = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
            Font font_n12_b = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);//FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new CMYKColor(0, 0, 0, 255));
            //Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new CMYKColor(0, 0, 0, 255));
            Font font_n10_b = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);//FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new CMYKColor(0, 0, 0, 255));
            Font basicFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD, new CMYKColor(0, 0, 0, 255));//FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL, new CMYKColor(0, 0, 0, 255));
            Font font_a10 = new Font(Font.FontFamily.TIMES_ROMAN, 10f);
            Font font_a10_b = new Font(Font.FontFamily.TIMES_ROMAN, 10f, Font.BOLD);
            Font headFontAR = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = Image.getInstance(stream.toByteArray());
            img.setAlignment(Element.ALIGN_CENTER);
            img.scaleAbsolute(100, 30);
            document.add(img);

            PdfPTable merName = new PdfPTable(1);
            merName.setWidthPercentage(100);
            merName.addCell(getCellArabic(printerModel.getRetailerNameArabic() + "\n", PdfPCell.ALIGN_CENTER, headFontAR));
            document.add(merName);
            Paragraph p = new Paragraph(printerModel.getRetailerNameEnglish(), font_n12_b);
            p.setAlignment(1);
            document.add(p);

            PdfPTable address = new PdfPTable(1);
            address.setWidthPercentage(100);
            address.addCell(getCellArabic(printerModel.getTerminalCityArabic() + ", " + printerModel.getTerminalStreetArabic() + "\n", PdfPCell.ALIGN_CENTER, font_a10));
            document.add(address);

            String content1 = printerModel.getTerminalStreetEnglish() + ", " + printerModel.getTerminaCityEnglish() + "\n\n";
            //"Whitefield,Bangalore"+"\n"+"080-23232323"+"\n";
            Paragraph p1 = new Paragraph(content1, basicFont);
            p1.setAlignment(1);
            document.add(p1);

            String startDate = printerModel.getStartDate();
            String startTime = printerModel.getStartTime() + "\n";

            String endDate = printerModel.getEndDate();
            String endTime = printerModel.getEndDate() + "\n";

            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(100);
            table2.addCell(getCell(startDate, PdfPCell.ALIGN_LEFT, font_n10));
            table2.addCell(getCell(startTime, PdfPCell.ALIGN_RIGHT, font_n10));
            table2.setSpacingAfter(1);
            document.add(table2);

            PdfPTable table3 = new PdfPTable(2);
            table3.setWidthPercentage(100);
            table3.addCell(getCell(printerModel.getbId() + " " + printerModel.getmId(), PdfPCell.ALIGN_LEFT, font_n9));
            table3.addCell(getCell(printerModel.gettId(), PdfPCell.ALIGN_RIGHT, font_n9));
            table3.setSpacingAfter(1);
            document.add(table3);

            PdfPTable table4 = new PdfPTable(2);
            table4.setWidthPercentage(100);
            table4.addCell(getCell(printerModel.getMcc() + " " + printerModel.getStan() + " " + printerModel.getPosSoftwareVersionNumber(), PdfPCell.ALIGN_LEFT, font_n9));
            table4.addCell(getCell(printerModel.getRrn(), PdfPCell.ALIGN_RIGHT, font_n9));
            table4.setSpacingAfter(3);
            document.add(table4);

            PdfPTable cardAR = new PdfPTable(1);
            cardAR.setWidthPercentage(100);
            cardAR.addCell(getCellArabic(printerModel.getApplicationLabelArabic() + "\n", PdfPCell.ALIGN_LEFT, font_a10_b));
            document.add(cardAR);

            Paragraph cardschemaa = new Paragraph(printerModel.getApplicationLabelEnglish() + "\n\n", font_n10_b);
            cardschemaa.setAlignment(Element.ALIGN_LEFT);
            document.add(cardschemaa);

            PdfPTable txnAR = new PdfPTable(1);
            txnAR.setWidthPercentage(100);
            txnAR.addCell(getCellArabic(printerModel.getTransactionTypeArabic() + "\n", PdfPCell.ALIGN_LEFT, font_a10));
            document.add(txnAR);

            Paragraph txn = new Paragraph(printerModel.getTransactionTypeEnglish() + "\n", font_n10);
            txn.setAlignment(Element.ALIGN_LEFT);
            document.add(txn);
            if (printerModel.getCardExpry().trim().length() != 0) {
                PdfPTable table5 = new PdfPTable(2);
                table5.setWidthPercentage(100);
                table5.addCell(getCell(printerModel.getPan(), PdfPCell.ALIGN_LEFT, font_n10));
                table5.addCell(getCell(printerModel.getCardExpry(), PdfPCell.ALIGN_RIGHT, font_n10));
                table5.setSpacingAfter(2);
                document.add(table5);
            } else {
                PdfPTable table5 = new PdfPTable(2);
                table5.setWidthPercentage(100);
                table5.addCell(getCell(printerModel.getPan(), PdfPCell.ALIGN_LEFT, font_n10));
                table5.setSpacingAfter(2);
                document.add(table5);
            }
            if (printerModel.getPurchaseAmountStringArabic() != null)
                document.add(addArabicTextLeft(printerModel.getPurchaseAmountStringArabic(), font_a10));
            if (printerModel.getPurchaseAmountArabic() != null)
                document.add(addArabicTextRight(printerModel.getPurchaseAmountArabic(), font_a10));
            if (printerModel.getPurchaseWithCashBackAmountStringArabic() != null)
                document.add(addArabicTextLeft(printerModel.getPurchaseWithCashBackAmountStringArabic(), font_a10));
            if (printerModel.getPurchaseWithCashBackAmountArabic() != null)
                document.add(addArabicTextLeft(printerModel.getPurchaseWithCashBackAmountArabic(), font_a10));
            if (printerModel.getTotalAmountArabic() != null)
                document.add(addArabicTextLeft(printerModel.getTotalAmountArabic(), font_a10_b));
            if (printerModel.getAmountArabic() != null)
                document.add(addArabicTextRight("\n"+printerModel.getAmountArabic()+"\n", font_a10_b));

            if (printerModel.getPurchaseAmountStringEnglish() != null)
                document.add(addEnglishTextLeft(printerModel.getPurchaseAmountStringEnglish(), font_n10));
            if (printerModel.getPurchaseAmountEnglish() != null)
                document.add(addEnglishTextRight(printerModel.getPurchaseAmountEnglish(), font_n10));
            if (printerModel.getPurchaseWithCashBackAmountStringEnglish() != null)
                document.add(addEnglishTextLeft(printerModel.getPurchaseWithCashBackAmountStringEnglish(), font_n10));
            if (printerModel.getPurchaseWithCashBackAmountEnglish() != null)
                document.add(addEnglishTextRight(printerModel.getPurchaseWithCashBackAmountEnglish(), font_n10));
            if (printerModel.getTotalAmountEnglish() != null)
                document.add(addEnglishTextLeft(printerModel.getTotalAmountEnglish(), font_n10_b));
            if (printerModel.getAmountEnglish() != null)
                document.add(addEnglishTextRight("\n"+printerModel.getAmountEnglish(), font_n10_b));

            document.add(addArabicTextCenter(printerModel.getTransactionOutcomeArabic(), font_a10_b));
            document.add(addEnglishTextCenter(printerModel.getTransactionOutcomeEnglish(), font_n10_b));

            document.add(addArabicTextCenter(printerModel.getCardHolderVerificationOrReasonForDeclineArabic(), font_a10));
            if (printerModel.getCardHolderVerificationOrReasonForDeclineEnglish() != null)
                if (printerModel.getCardHolderVerificationOrReasonForDeclineEnglish().contains("---")) {
                    String[] splitVal = printerModel.getCardHolderVerificationOrReasonForDeclineEnglish().split("---");
                    document.add(addEnglishTextCenter(splitVal[0].trim(), font_n10));
                    document.add(addEnglishTextCenter(splitVal[1].trim()+"\n", font_n10));
                } else
                    document.add(addEnglishTextCenter(printerModel.getCardHolderVerificationOrReasonForDeclineEnglish()+"\n", font_n10));
            if (printerModel.getUnderline() != null) {
                document.add(addArabicTextRight(printerModel.getSignBelowArabic(), font_a10));
                document.add(addEnglishTextLeft(printerModel.getSignBelowEnglish(), font_n10));
                document.add(addEnglishTextLeft(printerModel.getUnderline(), font_n10));
            }
            if (printerModel.getAccountForTheAmountArabic() != null)
                document.add(addArabicTextRight(printerModel.getAccountForTheAmountArabic(), font_a10));
            if (printerModel.getAccountForTheAmountEnglish() != null)
                document.add(addEnglishTextLeft(printerModel.getAccountForTheAmountEnglish(), font_n10));

            if (printerModel.getApprovalCodeArabic() != null) {
                PdfPTable table5 = new PdfPTable(2);
                table5.setWidthPercentage(100);
                table5.addCell(getCellArabic(printerModel.getApprovalCodeArabic(), PdfPCell.ALIGN_RIGHT, font_a10));
                table5.addCell(getCellArabic(printerModel.getApprovalCodeStringArabic(), PdfPCell.ALIGN_LEFT, font_a10));
                table5.setSpacingAfter(1);
                document.add(table5);
            }
            if (printerModel.getApprovalCodeEnglish() != null) {
                PdfPTable table6 = new PdfPTable(2);
                table6.setWidthPercentage(100);
                table6.addCell(getCell(printerModel.getApprovalCodeStringEnglish(), PdfPCell.ALIGN_LEFT, font_n10));
                table6.addCell(getCell(printerModel.getApprovalCodeEnglish(), PdfPCell.ALIGN_RIGHT, font_n10));
                table6.setSpacingAfter(1);
                document.add(table6);
            }
            PdfPTable table7 = new PdfPTable(2);
            table7.setWidthPercentage(100);
            table7.addCell(getCell(endDate, PdfPCell.ALIGN_LEFT, font_n10));
            table7.addCell(getCell(endTime, PdfPCell.ALIGN_RIGHT, font_n10));
            table7.setSpacingAfter(1);
            document.add(table7);

            document.add(addArabicTextCenter(printerModel.getThankYouArabic(), font_a10));
            document.add(addEnglishTextCenter(printerModel.getThankYouEnglish(), font_n10));

            document.add(addArabicTextCenter(printerModel.getPleaseRetainYourReceiptArabic(), font_a10));
            document.add(addEnglishTextCenter(printerModel.getPleaseRetainYourReceiptEnglish(), font_n10));

            document.add(addArabicTextCenter(printerModel.getReceiptVersionArabic(), font_a10_b));
            document.add(addEnglishTextCenter(printerModel.getReceiptVersionEnglish()+"\n", font_n10_b));

            document.add(addEnglishTextLeft(printerModel.getPosEntryMode() + " " + printerModel.getAlpharesponseCode() + " " + printerModel.getAid() + " " + printerModel.getTvr(), font_n9));

            document.add(addEnglishTextLeft(printerModel.getTsi() + printerModel.getCvr() + printerModel.getApplicationCryptogramInfo() + printerModel.getApplicationCryptogram() + printerModel.getKernalId(), font_n9));

            document.add(addEnglishTextLeft(printerModel.getData44(), font_n9));

        } catch (Exception e) {

        } finally {
            document.close();
        }
        return file;
    }
}