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

/** 
 * services to extract data
 *  
 * @FeatureDomain                service
 * @package                      de.yaio.services.metaextract.extractor
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     extractor-services
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public interface Extractor {

    public String extractText(InputStream input, String fileName, String lang) throws IOException;

    public String extractText(File file, String lang) throws IOException;

    public String identifyLanguage(String text);

    public ExtractedMetaDataVersion extractMetaData(InputStream input, String fileName, String lang) throws IOException;

    public ExtractedMetaDataVersion extractMetaData(File file, String lang) throws IOException;

    public String getParserName();
}