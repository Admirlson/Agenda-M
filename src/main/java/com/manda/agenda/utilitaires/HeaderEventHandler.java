package com.manda.agenda.utilitaires;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEvent;
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEventHandler;
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeaderEventHandler extends AbstractPdfDocumentEventHandler {
    private final String path;
    private final String pathRight;
    private final Paragraph text;

    @Override
    protected void onAcceptedEvent(AbstractPdfDocumentEvent event) {
        // TODO Auto-generated method stub
        PdfDocumentEvent pdfDocumentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = pdfDocumentEvent.getDocument();
        PdfPage pdfPage = pdfDocumentEvent.getPage();

        // Récupération du Canvas de la page

        PdfCanvas pdfCanvas = new PdfCanvas(pdfPage.newContentStreamBefore(), pdfPage.getResources(), pdfDocument);

        // Chargement de l'image

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                throw new FileNotFoundException("L'image n'a pas été trouvé==897977979=====");
            }
            byte[] bytes = inputStream.readAllBytes();
            ImageData imageData = ImageDataFactory.create(bytes);

            // Positionnement de l'image

            // Marge gauche
            float x = 36;

            // Position depuis le haut
            float y = pdfPage.getPageSize().getTop() - 80;

            // Largeur
            float width = 70;

            // Hauteur
            float height = 70;

            // Dessiner l'image
            pdfCanvas.addImageFittedIntoRectangle(imageData,
                    new com.itextpdf.kernel.geom.Rectangle(x, y, width, height), true);

            InputStream inputStreamRight = getClass().getClassLoader().getResourceAsStream(pathRight);

            byte[] bytesRight = inputStreamRight.readAllBytes();
            ImageData imageDataRight = ImageDataFactory.create(bytesRight);

            // Positionnement de l'image

            // Marge gauche
            float xRight = 740;

            // Position depuis le haut
            float yRight = pdfPage.getPageSize().getTop() - 80;

            // Dessiner l'image
            pdfCanvas.addImageFittedIntoRectangle(imageDataRight,
                    new com.itextpdf.kernel.geom.Rectangle(xRight, yRight, width, height), true);

            // Dessiner le Paragraphe

            Canvas canvas = new Canvas(pdfCanvas,
                    new Rectangle(50, pdfPage.getPageSize().getTop() - 108, pdfPage.getPageSize().getWidth() - 100,
                            100));

            // Centrer verticalement avec les images
            // float yText = yRight + (height / 2);

            // Ajout du texte
            // float xText = pdfPage.getPageSize().getWidth() / 2;
            // pdfCanvas.beginText().setFontAndSize(com.itextpdf.kernel.font.PdfFontFactory.createFont(),
            // 12)
            // .moveText(xText, yText).show.endText();
            // pdfCanvas.beginText()
            // .setFontAndSize(com.itextpdf.kernel.font.PdfFontFactory.createFont(), 12)
            // .moveText(xText, yText)
            // .showText(text)
            // .endText();

            canvas.add(text);
            canvas.close();

            pdfCanvas.release();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
