package com.track.util.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.TextAlignment;

/**
 * Handles PDF header events
 * 
 * @author Ravindra Gullapalli
 * @since 04-May-2021
 */
public class HeaderHandler implements IEventHandler {
	private String header;

	public HeaderHandler(String header) {
            this.header = header;
        }

	@Override
	public void handleEvent(Event event) {
		PdfDocumentEvent docEvent = (PdfDocumentEvent) event;

		PdfPage page = docEvent.getPage();
		Rectangle pageSize = page.getPageSize();

		Canvas canvas = new Canvas(new PdfCanvas(page), pageSize);
		canvas.setFontSize(18);

		// Write text at position
		canvas.showTextAligned(header, pageSize.getWidth() / 2, pageSize.getTop() - 30, TextAlignment.CENTER);
		canvas.close();
	}
}
