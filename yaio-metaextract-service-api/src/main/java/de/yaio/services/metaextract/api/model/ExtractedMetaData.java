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
package de.yaio.services.metaextract.api.model;

import java.util.HashMap;
import java.util.Map;

/** 
 * implementation of the Metadata
 */
public class ExtractedMetaData {
    
    protected String lang;
    protected Map<String, ExtractedMetaDataVersion> versions = new HashMap<String , ExtractedMetaDataVersion>();

    public ExtractedMetaData() {
    }
    public ExtractedMetaData(final String lang, HashMap<String , ExtractedMetaDataVersion> versions) {
        super();
        this.lang = lang;
        if (versions != null) {
            this.versions = versions;
        }
    }
    public String getLang() {
        return this.lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
    public Map<String, ExtractedMetaDataVersion> getVersions() {
        return this.versions;
    }
    public void setVersions(Map<String, ExtractedMetaDataVersion> versions) {
        this.versions = versions;
    }

}
