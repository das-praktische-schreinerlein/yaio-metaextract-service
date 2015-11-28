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


/** 
 * implementation of the Metadata
 * 
 * @FeatureDomain                service
 * @package                      de.yaio.services.metaextract.extractor
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     extractor-services
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 */
public class ExtractedMetaDataVersion {
    private String content;
    private String lang;

    public ExtractedMetaDataVersion() {

    }
    public ExtractedMetaDataVersion(final String content, final String lang) {
        super();
        this.content = content;
        this.setLang(lang);
    }
    public String getContent() {
        return this.content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    /**
     * @return the {@link ExtractedMetaDataVersion#lang}
     */
    public String getLang() {
        return lang;
    }
    /**
     * @param lang the {@link ExtractedMetaDataVersion#lang} to set
     */
    public void setLang(final String lang) {
        this.lang = lang;
    }
}
