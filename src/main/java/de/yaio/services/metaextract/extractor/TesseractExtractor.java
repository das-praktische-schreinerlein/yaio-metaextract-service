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
package de.yaio.services.metaextract.extractor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** 
 * services to extract data via tika
 *  
 * @FeatureDomain                service
 * @package                      de.yaio.services.metaextract.extractor
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     extractor-services
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
@Service
public class TesseractExtractor extends AbstractExtractor implements Extractor {
    
    private static Map<String,String>langMap = new HashMap<String, String>();
    static {
        langMap.put("de",  "deu");
        langMap.put("en",  "eng");
    }
    
    @Value("${yaio-metaextract-service.tess.datapath}")
    private String tessDatapath ;
    
    @Override
    public String extractText(final InputStream input, final String fileName, final String lang) throws IOException  {
        File tmpFile = File.createTempFile("metaextractor", "." + FilenameUtils.getExtension(fileName));
        tmpFile.deleteOnExit();
        Files.copy(input , tmpFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        return this.extractText(tmpFile, lang);
    }
    
    @Override
    public String extractText(final File file, final String lang) throws IOException  {
        Tesseract instance = new Tesseract();
        instance.setDatapath(tessDatapath);
        
        // init lang
        String tessLang = "deu";
        if (!StringUtils.isEmpty(lang) && langMap.containsKey(lang)) {
            tessLang = langMap.get(lang);
        }
        instance.setLanguage(tessLang);
        
        String result = "";
        try {
            result = instance.doOCR(file);
        } catch (TesseractException e) {
            if (RuntimeException.class.isInstance(e.getCause())) {
                throw new IOException("error while using tesseract to extract (not supported) from:" + file.getAbsolutePath());
            }
            e.printStackTrace();
            throw new IOException("error while using tesseract to extract from:" + file.getAbsolutePath());
        }
        return result;
    }

    @Override
    public String identifyLanguage(final String text) {
        return "UNKNOWN";
    }
}
