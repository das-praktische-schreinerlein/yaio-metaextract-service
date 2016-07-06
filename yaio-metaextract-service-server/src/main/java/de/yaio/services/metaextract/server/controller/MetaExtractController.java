/** 
 * software for metadata-extraction
 * 
 * @FeatureDomain                Extractor
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     metadata-extraction
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.services.metaextract.server.controller;

import de.yaio.commons.io.IOExceptionWithCause;
import de.yaio.commons.net.PermissionException;
import de.yaio.services.metaextract.api.model.ExtractedMetaData;
import de.yaio.services.metaextract.server.extractor.ExtractorException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;


/** 
 * controller to extract metadata from uploaded files or urls
 */
@Controller
@RequestMapping("${yaio-metaextract-service.baseurl}")
public class MetaExtractController {

    private static final Logger LOGGER = Logger.getLogger(MetaExtractController.class);

    @Autowired
    protected MetaExtractFacade metaExtractFacade;

    /** 
     * downloads the url and extracts metadata
     * @param url                    the url to download and extract metadata from
     * @param lang                   the prevered lang to parse (OCR)
     * @return                       the extracted metadata
     * @throws IOException           possible
     */
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/getByUrl")
    public @ResponseBody ExtractedMetaData getByUrl(@RequestParam(value="url", required=true) String url,
                                                    @RequestParam("lang") String lang)
            throws IOException, IOExceptionWithCause, ExtractorException, PermissionException {
        return metaExtractFacade.extractMetaData(url, lang);
    }

    /** 
     * extract metadata from the uploaded file
     * @param uploadFile             the file to extract metadata from
     * @param lang                   the prevered lang to parse (OCR)
     * @return                       the extracted metadata
     * @throws IOException           possible
     */
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/getByFile")
    public @ResponseBody ExtractedMetaData getByFile(@RequestParam("file") MultipartFile uploadFile,
                                                     @RequestParam("lang") String lang)
            throws IOException, ExtractorException {
        return metaExtractFacade.extractMetaData(uploadFile.getInputStream(), uploadFile.getOriginalFilename(), lang);
    }

    @ExceptionHandler(PermissionException.class)
    public void handleCustomException(final HttpServletRequest request, final PermissionException e,
                                      final HttpServletResponse response) throws IOException {
        LOGGER.info("PermissionException while running request:" + createRequestLogMessage(request), e);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().append("permission denied (firewall...) while extracting metadata for requested resource");
    }

    @ExceptionHandler(IOExceptionWithCause.class)
    public void handleCustomException(final HttpServletRequest request, final IOExceptionWithCause e,
                                      final HttpServletResponse response) throws IOException {
        LOGGER.info("IOExceptionWithCause while running request:" + createRequestLogMessage(request), e);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().append("url-get failed while extracting metadata for requested resource:");
        response.getWriter().append(e.getCause().getMessage());
    }

    @ExceptionHandler(ExtractorException.class)
    public void handleCustomException(final HttpServletRequest request, final ExtractorException e,
                                        final HttpServletResponse response) throws IOException {
        LOGGER.info("ExtractorException while running request:" + createRequestLogMessage(request), e);
        response.setStatus(SC_BAD_REQUEST);
        response.getWriter().append("exception while extracting metadata for requested resource");
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class, IOException.class})
    public void handleAllException(final HttpServletRequest request, final Exception e,
                                     final HttpServletResponse response) {
        LOGGER.info("Exception while running request:" + createRequestLogMessage(request), e);
        response.setStatus(SC_INTERNAL_SERVER_ERROR);
        try {
            response.getWriter().append("exception while extracting metadata for requested resource");
        } catch (IOException ex) {
            LOGGER.warn("exception while exceptionhandling", ex);
        }
    }

    protected String createRequestLogMessage(HttpServletRequest request) {
        final StringBuilder logMessage = new StringBuilder("REST Request - ")
                .append("[HTTP METHOD:")
                .append(request.getMethod())
                .append("] [URL:")
                .append(request.getRequestURL())
                .append("] [REQUEST PARAMETERS:")
                .append(getRequestMap(request))
                .append("] [REMOTE ADDRESS:")
                .append(request.getRemoteAddr())
                .append("]");

        return logMessage.toString();
    }

    private Map<String, String> getRequestMap(HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap<>();
        Enumeration<?> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String)requestParamNames.nextElement();
            String requestParamValue = request.getParameter(requestParamName);
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }
        return typesafeRequestMap;
    }
}