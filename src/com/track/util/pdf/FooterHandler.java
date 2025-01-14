package com.track.util.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

/**
 * Handles footer events to print page numbers
 * @author Ravindra Gullapalli
 * @since 04-May-2021
 */
public class FooterHandler implements IEventHandler {
	
	private int numberOfPages;
	private boolean deriveNumberOfPages;
	protected PdfFormXObject placeholder;
    protected float side = 20;
    protected float x = 550;
    protected float y = 25;
    protected float space = 4.5f;
    protected float descent = 3;

    public FooterHandler(boolean deriveNumberOfPages) {
        placeholder = new PdfFormXObject(new Rectangle(0, 0, side, side));
        this.deriveNumberOfPages = deriveNumberOfPages;
        if (deriveNumberOfPages) {
            this.numberOfPages = 0;
        }
    }
    
    public FooterHandler(int numberOfPages) {
        this(false);
        this.numberOfPages = numberOfPages;
    }
    
    public int getNumberOfPages() {
    	return numberOfPages;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        int pageNumber = pdf.getPageNumber(page);
        Rectangle pageSize = page.getPageSize();

        // Creates drawing canvas
        PdfCanvas pdfCanvas = new PdfCanvas(page);
        Canvas canvas = new Canvas(pdfCanvas, pageSize);
        if (deriveNumberOfPages) {
        	numberOfPages++;
        }
        Paragraph p = new Paragraph()
                .add("Page ")
                .add(String.valueOf(pageNumber))
                .add(" of ")
                .add(String.valueOf(numberOfPages));

        canvas.showTextAligned(p, x, y, TextAlignment.RIGHT);
        canvas.close();

        // Create placeholder object to write number of pages
        pdfCanvas.addXObject(placeholder, x + space, y - descent);
        pdfCanvas.release();
    }
}
