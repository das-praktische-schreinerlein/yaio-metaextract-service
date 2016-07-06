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
public interface Extractor {

    String extractText(InputStream input, String fileName, String lang) throws IOException, ExtractorException;

    String extractText(File file, String lang) throws IOException, ExtractorException;

    String identifyLanguage(String text);

    ExtractedMetaDataVersion extractMetaData(InputStream input, String fileName, String lang)
            throws IOException, ExtractorException;

    ExtractedMetaDataVersion extractMetaData(File file, String lang) throws IOException, ExtractorException;

    String getParserName();
}