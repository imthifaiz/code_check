/**
 * Modification's History:
 * =======================
 * 20181226 165140:Ravindra:[1]Enhancement:Supported Case insensitive logo search
 */
package com.track.servlet;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.track.constants.MLoggerConstant;

/**
 * Servlet implementation class GetCustomerLogoServlet
 */
@WebServlet("/GetCustomerLogoByPlantServlet")
public final class GetCustomerLogoByPlantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetCustomerLogoByPlantServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String PLANT = request.getParameter("PLANT");
		response.setContentType("image/gif");
		ServletOutputStream out;
		out = response.getOutputStream();

		Optional<File> optionalLogoFile = findFileIgnoreCase(MLoggerConstant.PROPS_FOLDER + "/track/Logos/", PLANT.toLowerCase() + "Logo.GIF");
		File logoFile = null;
		if (optionalLogoFile.isPresent()) {
			logoFile = optionalLogoFile.get();
		}else {
			logoFile = new File(MLoggerConstant.PROPS_FOLDER + "/track/Logos/NoLogo.JPG");
		}
		FileInputStream fin = new FileInputStream(logoFile);

		BufferedInputStream bin = new BufferedInputStream(fin);
		BufferedOutputStream bout = new BufferedOutputStream(out);
		int ch = 0;
		;
		while ((ch = bin.read()) != -1) {
			bout.write(ch);
		}

		bin.close();
		fin.close();
		bout.close();
		out.close();
	}
	
	public Optional<File> findFileIgnoreCase(String absoluteDirPath, final String fileName) {

        File directory = new File(absoluteDirPath);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Directory '" + absoluteDirPath + "' isn't a directory.");
        }
        IOFileFilter caseInsensitiveFileNameFilter = new IOFileFilter() {
            @Override
            public boolean accept(File dir, String name) {
                boolean isSameFile = fileName.equalsIgnoreCase(name);
                return isSameFile;
            }

            @Override
            public boolean accept(File file) {
                String name = file.getName();
                boolean isSameFile = fileName.equalsIgnoreCase(name);
                return isSameFile;
            }
        };
        Collection<File> foundFiles = FileUtils.listFiles(directory, caseInsensitiveFileNameFilter, null);
        if (foundFiles == null || foundFiles.isEmpty()) {
            return Optional.empty();
        }
//        if (foundFiles.size() > 1) {
//            throw new IllegalStateException(
//                    "More requirements needed to determine what to do with more than one file. Pick the closest match maybe?");
//        }
        // else exactly one file
        File foundFile = foundFiles.iterator().next();
        return Optional.of(foundFile);
    }

}
