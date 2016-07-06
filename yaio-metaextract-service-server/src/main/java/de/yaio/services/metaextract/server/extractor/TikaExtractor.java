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
package de.yaio.services.metaextract.server.extractor;

import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/** 
 * services to extract data via tika
 */
@Service
public class TikaExtractor extends AbstractExtractor implements Extractor {
    
    @Override
    public String extractText(final File file, final String lang) throws IOException, ExtractorException  {
        FileInputStream inputStream = new FileInputStream(file);
        return extractText(inputStream, file.getAbsolutePath(), lang);
    }
    
    @Override
    public String extractText(final InputStream input, final String fileName, final String lang) throws IOException, ExtractorException  {
        BodyContentHandler handler = new BodyContentHandler();
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();

        try {
            parser.parse(input, handler, metadata);
        } catch (TikaException e) {
            throw new ExtractorException("TikaException while parsing file", fileName, e);
        } catch (SAXException e) {
            throw new ExtractorException("SAXException while parsing file", fileName, e);
        }
        
        return handler.toString();
    }

    @Override
    public String identifyLanguage(final String text) {
        LanguageIdentifier identifier = new LanguageIdentifier(text);
        return identifier.getLanguage();
    }

    @Override
    public String getParserName() {
        return "Textextract TIKA";
    }
}
