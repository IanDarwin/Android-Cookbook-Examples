package com.darwinsys.pdfgen;

import android.util.Log;
import android.view.View;
import android.graphics.pdf.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class PDFGen {

    final static String TAG = PDFGen.class.getSimpleName();

    static void write(File file, View contentView) {

        try (OutputStream outputStream = Files.newOutputStream(file.toPath())) {

            // create a new document
            PdfDocument document = new PdfDocument();

            // create required page description
            PdfDocument.PageInfo pageInfo =
                    new PdfDocument.PageInfo.Builder(400, 500, 1).create();

            // start a new page
            PdfDocument.Page page = document.startPage(pageInfo);

            // render the given View onto the page
            contentView.draw(page.getCanvas());

            // finish the page
            document.finishPage(page);

            // could add more pages here

            // write the document to persistent storage
            document.writeTo(outputStream);

            // close the document
            document.close();
        } catch (IOException ex) {
            String mesg = "I/O error generating PDF (" + ex + ")";
            Log.e(TAG, mesg, ex);
        }

    }
}
