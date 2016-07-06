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

import de.yaio.commons.http.HttpUtils;
import de.yaio.commons.io.IOExceptionWithCause;
import de.yaio.commons.net.NetFirewall;
import de.yaio.commons.net.NetFirewallFactory;
import de.yaio.commons.net.PermissionException;
import de.yaio.services.metaextract.api.model.ExtractedMetaData;
import de.yaio.services.metaextract.api.model.ExtractedMetaDataVersion;
import de.yaio.services.metaextract.server.extractor.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/** 
 * services to extract metadata
 */
@Service
class MetaExtractFacade {
    private static final Logger LOGGER = Logger.getLogger(MetaExtractFacade.class);

    @Autowired
    protected TikaExtractor extractor1;

    @Autowired
    protected TesseractExtractor extractor2;
    
    @Autowired
    protected MetaExtractFirewallConfig firewallConfig;
    
    protected NetFirewall netFirewall;

    /**
     * extract metadata from the inputStream depending on the extension of fileName
     *
     * @param input                 content to extract the metadata from
     * @param fileName              extension to get extension and mimetype to support extraction
     * @param lang                  language-key to support extraction
     * @return                      extracted metadata from the different extractors
     * @throws IOException          possible errors while reading and copying tmpFiles
     * @throws ExtractorException   possible errors while running extractor
     */
    public ExtractedMetaData extractMetaData(final InputStream input, final String fileName, final String lang)
            throws IOException, ExtractorException {
        List<Extractor> extractors = new ArrayList<>();
        extractors.add(extractor1);
        extractors.add(extractor2);

        File tmpFile = File.createTempFile("metaextractor", "." + FilenameUtils.getExtension(fileName));
        tmpFile.deleteOnExit();
        Files.copy(input, tmpFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        ExtractedMetaData extractedMetaData = new ExtractedMetaData();

        for (Extractor extractor : extractors) {
            try {
                ExtractedMetaDataVersion extractedMetaDataVersion = extractor.extractMetaData(tmpFile, lang);
                
                String content = extractedMetaDataVersion.getContent();
                if (StringUtils.isNotBlank(content)) {
                    extractedMetaData.getVersions().put(extractor.getClass().toString(), extractedMetaDataVersion);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        LOGGER.info("done extract metadat for file:" + fileName + " with lang:" + lang + " to " + extractedMetaData);

        return extractedMetaData;
    }

    /**
     * extract metadata from the url
     *
     * @param url                   url to read and extract the metadata from
     * @param lang                  language-key to support extraction
     * @return                      extracted metadata from the different extractors
     * @throws IOException          possible errors while reading and copying tmpFiles
     * @throws ExtractorException   possible errors while running extractor
     */
    public ExtractedMetaData extractMetaData(final String url, final String lang)
            throws PermissionException, IOException, IOExceptionWithCause, ExtractorException {
        // check url
        getNetFirewall().throwExceptionIfNotAllowed(url);

        // call url
        HttpResponse response = HttpUtils.callGetUrlPure(url, "anonymous", "anonymous", null);
        HttpEntity entity = response.getEntity();

        // check response
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOExceptionWithCause("cant read from url", url,
                    new IOException("illegal reponse:" + response.getStatusLine() + " for url:" + url
                            + " response:" + EntityUtils.toString(entity)));
        }
        
        // generate filename from contenttype
        String mimetype = EntityUtils.getContentMimeType(entity);
        MimeTypes allMimeTypes = MimeTypes.getDefaultMimeTypes();
        String fileName;
        try {
            MimeType mime = allMimeTypes.forName(mimetype);
            fileName = "download." + mime.getExtension();

        } catch (MimeTypeException ex) {
            fileName = "download.unknown";
        }

        InputStream input = new ByteArrayInputStream(EntityUtils.toByteArray(entity));
        LOGGER.info("done download data for url:" + url + " with mime:" + mimetype + " to " + fileName);

        return extractMetaData(input, fileName, lang);
    }

    protected NetFirewall getNetFirewall() {
        if (netFirewall == null) {
            netFirewall = NetFirewallFactory.creatNetFirewall(firewallConfig);
        }
        return netFirewall;
    }
}
