package com.track.util.pdf;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;

/**
 * Utility methods related to PDF generation
 * 
 * @author Ravindra Gullapalli
 * @since 11-Apr-2021
 */
public class PdfUtil {

	/**
	 * Provides HTML to generate PDF after extracting necessary HTML form the source
	 * HTML This method searches for delimiters <!-- PDF Print Start {blockNumber}
	 * --> and <!-- PDF Print End {blockNumber} --> and extracts the HTML between
	 * these delimiters. Block number starts from 1
	 * 
	 * @param html Source HTML
	 * @return HTML to generate PDF
	 * @author Ravindra Gullapalli
	 */
	public static String getHtmlForPDF(String html) {
		// Replace br tags
		html = html.replaceAll("<br>", "<div></div>").replaceAll("<br(.*?)\\/>", "<div></div>").replaceAll("<thead>", "").replaceAll("</thead>", "");
		StringBuffer sbPdfData = new StringBuffer();
		for (int pdfPrintSectionIndex = 1; pdfPrintSectionIndex < 10; pdfPrintSectionIndex ++) {
			Pattern pattern = Pattern.compile("<!-- PDF Print Start " + pdfPrintSectionIndex + " -->(.*?)<!-- PDF Print End " + pdfPrintSectionIndex + " -->", Pattern.DOTALL);
			Matcher matcher = pattern.matcher(html);
			while (matcher.find()) {
			    sbPdfData.append(matcher.group(1));
			}
		}
		html = sbPdfData.toString();
		html = html.replace("<figure", "<div");
		html = html.replace("</figure", "</div");
		html = html.replace("</head>", "<style>body{font-weight:bold;}</style></head>");
		return html;
	}

	public static void createPDF(String htmlSource, String pdfDest, String resourceLoc, PageSize pageSize,
			float screenWidth) throws IOException {

		// Clean HTML
		htmlSource = PdfUtil.getHtmlForPDF(htmlSource);

//		try(java.io.FileOutputStream fos = new java.io.FileOutputStream(new java.io.File(pdfDest + ".html" ))){
//			fos.write(htmlSource.getBytes());
//		}

		WriterProperties wp = new WriterProperties();
		wp.setPdfVersion(PdfVersion.PDF_2_0);
		FooterHandler footerHandler = new FooterHandler(true);
		generatePdfDocument(new PdfWriter(new ByteArrayOutputStream(), wp), resourceLoc, pageSize, screenWidth, htmlSource, footerHandler);
		footerHandler = new FooterHandler(footerHandler.getNumberOfPages());
		generatePdfDocument(new PdfWriter(pdfDest, wp), resourceLoc, pageSize, screenWidth, htmlSource, footerHandler);
	}

	private static void generatePdfDocument(PdfWriter writer, String resourceLoc, PageSize pageSize, float screenWidth, String htmlSource, FooterHandler footerHandler) {
		PdfDocument pdfDocument = new PdfDocument(writer);
		
		// Set the result to be tagged
		pdfDocument.setTagged();
		pdfDocument.setDefaultPageSize(pageSize);
		
		if (footerHandler != null) {
//			pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, new HeaderHandler("Test"));
			pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);
		}
		ConverterProperties converterProperties = new ConverterProperties();

		// Set media device description details
		MediaDeviceDescription mediaDescription = new MediaDeviceDescription(MediaType.SCREEN);
		mediaDescription.setWidth(screenWidth);
		converterProperties.setMediaDeviceDescription(mediaDescription);

		FontProvider fp = new DefaultFontProvider();

		// Register external font directory
		fp.addDirectory(resourceLoc);

		converterProperties.setFontProvider(fp);
		// Base URI is required to resolve the path to source files
		converterProperties.setBaseUri(resourceLoc);

		// Create acroforms from text and button input fields
		//converterProperties.setCreateAcroForm(true);

		HtmlConverter.convertToPdf(htmlSource, pdfDocument, converterProperties);
		pdfDocument.close();
	}
}
