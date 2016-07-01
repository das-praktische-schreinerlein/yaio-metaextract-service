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

import de.yaio.services.metaextract.api.model.ExtractedMetaData;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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
     * @param request                the request-obj to get the servlet-context 
     * @param response               the response-Obj to set contenttype and headers
     * @return                       the extracted metadata
     * @throws IOException           possible
     */
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/getByUrl")
    public @ResponseBody ExtractedMetaData getByUrl(@RequestParam(value="url", required=true) String url,
                                                    @RequestParam("lang") String lang,
                                                    HttpServletRequest request, 
                                                    HttpServletResponse response) throws IOException {
        ExtractedMetaData meta = null;
        try {
            meta = metaExtractFacade.extractMetaData(url, lang);
        } catch (IOException | SAXException | TikaException e) {
            LOGGER.warn("exception start for url:" + url, e);
            response.setStatus(404);
            response.getWriter().append("error while reading:" + e.getMessage());
        }
        return meta;
    }

    /** 
     * extract metadata from the uploaded file
     * @param uploadFile             the file to extract metadata from
     * @param lang                   the prevered lang to parse (OCR)
     * @param request                the request-obj to get the servlet-context 
     * @param response               the response-Obj to set contenttype and headers
     * @return                       the extracted metadata
     * @throws IOException           possible
     */
    @RequestMapping(method = RequestMethod.POST, 
                    value = "/getByFile")
    public @ResponseBody ExtractedMetaData getByFile(@RequestParam("file") MultipartFile uploadFile,
                                                     @RequestParam("lang") String lang,
                                                     HttpServletRequest request, 
                                                     HttpServletResponse response) throws IOException {
        ExtractedMetaData meta = null;
        try {
            meta = metaExtractFacade.extractMetaData(uploadFile.getInputStream(), 
                            uploadFile.getOriginalFilename(), lang);
        } catch (IOException | SAXException | TikaException e) {
            LOGGER.warn("exception start for file:" + uploadFile, e);
            response.setStatus(404);
            response.getWriter().append("error while reading:" + e.getMessage());
        }
        return meta;
    }
}