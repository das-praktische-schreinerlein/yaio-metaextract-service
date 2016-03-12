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
 */
public class ExtractedMetaDataVersion {
    private String content;
    private String lang;
    private String parserName;

    public ExtractedMetaDataVersion() {

    }
    public ExtractedMetaDataVersion(final String content, final String lang, final String parserName) {
        super();
        this.setContent(content);
        this.setLang(lang);
        this.setParserName(parserName);
        
    }
    public String getContent() {
        return this.content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getLang() {
        return lang;
    }
    public void setLang(final String lang) {
        this.lang = lang;
    }
    public String getParserName() {
        return this.parserName;
    }
    public void setParserName(String parserName) {
        this.parserName = parserName;
    }
}
