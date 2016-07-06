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

import de.yaio.services.metaextract.api.model.ExtractedMetaDataVersion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/** 
 * services to extract data
 */
public abstract class AbstractExtractor implements Extractor {
    @Override
    public ExtractedMetaDataVersion extractMetaData(final InputStream input, final String fileName, final String lang)
            throws IOException, ExtractorException {
        ExtractedMetaDataVersion extractedMetaDataVersion = new ExtractedMetaDataVersion();

        String content = this.extractText(input, fileName, lang);
        content = normalizeText(content);

        extractedMetaDataVersion.setParserName(this.getParserName());
        extractedMetaDataVersion.setContent(content);
        extractedMetaDataVersion.setLang(this.identifyLanguage(extractedMetaDataVersion.getContent()));

        return extractedMetaDataVersion;
    }

    @Override
    public ExtractedMetaDataVersion extractMetaData(final File file, final String lang) throws IOException, ExtractorException {
        ExtractedMetaDataVersion extractedMetaDataVersion = new ExtractedMetaDataVersion();

        String content = this.extractText(file, lang);
        content = normalizeText(content);

        extractedMetaDataVersion.setParserName(this.getParserName());
        extractedMetaDataVersion.setContent(content);
        extractedMetaDataVersion.setLang(this.identifyLanguage(extractedMetaDataVersion.getContent()));

        return extractedMetaDataVersion;
    }
    
    protected String normalizeText(final String content) {
        String newContent = content.replaceAll("\r", "");
        newContent = newContent.replaceAll("\n\n+", "\n\n");
        newContent = newContent.replaceAll("\n[ \n\t]+\n", "\n\n");
        newContent = newContent.replaceAll("\n\n+", "\n\n");

        return newContent;
    }
}
