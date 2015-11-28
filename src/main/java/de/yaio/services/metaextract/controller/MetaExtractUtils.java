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
package de.yaio.services.metaextract.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import de.yaio.commons.http.HttpUtils;
import de.yaio.services.metaextract.extractor.ExtractedMetaData;
import de.yaio.services.metaextract.extractor.ExtractedMetaDataVersion;
import de.yaio.services.metaextract.extractor.Extractor;
import de.yaio.services.metaextract.extractor.TesseractExtractor;
import de.yaio.services.metaextract.extractor.TikaExtractor;

/** 
 * services to extract metadata
 *  
 * @FeatureDomain                service
 * @package                      de.yaio.services.metaextract.controller
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     metadata-services
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Service
class MetaExtractUtils {

    private static final Logger LOGGER = Logger.getLogger(MetaExtractUtils.class);

    @Autowired
    protected TikaExtractor extractor1;

    @Autowired
    protected TesseractExtractor extractor2;

    public ExtractedMetaData extractMetaData(final InputStream input, final String fileName, final String lang) throws IOException, SAXException, TikaException  {
        List<Extractor> extractors = new ArrayList<Extractor>();
        extractors.add(extractor1);
        extractors.add(extractor2);

        File tmpFile = File.createTempFile("metaextractor", "." + FilenameUtils.getExtension(fileName));
        tmpFile.deleteOnExit();
        Files.copy(input , tmpFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        ExtractedMetaData extractedMetaData = new ExtractedMetaData();

        for (Extractor extractor : extractors) {
            try {
                ExtractedMetaDataVersion extractedMetaDataVersion = extractor.extractMetaData(tmpFile, lang);
                extractedMetaData.getVersions().put(extractor.getClass().toString(), extractedMetaDataVersion);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        LOGGER.info("done extract metadat for file:" + fileName + " with lang:" + lang + " to " + extractedMetaData);

        return extractedMetaData;
    }

    public ExtractedMetaData extractMetaData(final String url, final String lang) throws IOException, SAXException, TikaException  {
        // call url
        HttpResponse response = HttpUtils.callGetUrlPure(url, "anonymous", "anonymous", null);
        HttpEntity entity = response.getEntity();

        // check response
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOException("illegal reponse:" + response.getStatusLine() 
                            + " for url:" + url 
                            + " response:" + EntityUtils.toString(entity));
        }
        
        // generate filename from contenttype
        String mimetype = EntityUtils.getContentMimeType(entity);
        MimeTypes allMimeTypes = MimeTypes.getDefaultMimeTypes();
        MimeType mime = allMimeTypes.forName(mimetype);
        String fileName = "download." + mime.getExtension();

        InputStream input = new ByteArrayInputStream(EntityUtils.toByteArray(entity));
        LOGGER.info("done download data for url:" + url + " with mime:" + mimetype + " to " + fileName);

        return extractMetaData(input, fileName, lang);
    }

}
