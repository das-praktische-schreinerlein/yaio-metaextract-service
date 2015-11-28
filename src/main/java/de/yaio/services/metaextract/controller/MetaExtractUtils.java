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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

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
        
        return extractedMetaData;
    }
}
