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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;

/** 
 * services for metadata-extractions
 *  
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class ExtractedMetaDataFactory {

    protected ObjectMapper extractedMetaDataMapper;

    public static ExtractedMetaDataFactory createExtractedMetaDataFactory() {
        return new ExtractedMetaDataFactory();
    }

    protected ExtractedMetaDataFactory() {
        extractedMetaDataMapper = createExtractedMetaDataMapper();
    }

    /**
     * parse ExtractedMetaData from json
     * @param metaJson               json to be parsed 
     * @return                       returns the ExtractedMetaData
     * @throws IOException           if deserialization fails
     */
    public ExtractedMetaData parseExtractedMetaDataFromJson(String metaJson) throws IOException {
        return extractedMetaDataMapper.readValue(metaJson, ExtractedMetaData.class);
    }

    protected ObjectMapper createExtractedMetaDataMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // configure
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        // add deserializer
        SimpleModule module = new SimpleModule("ExtractedMetaDataDeserializer", new Version(1, 0, 0, null));
        ExtractedMetaDataDeserializer extractedMetaDataDeserializer = new ExtractedMetaDataDeserializer();
        module.addDeserializer(ExtractedMetaData.class, extractedMetaDataDeserializer);

        mapper.registerModule(module);

        return mapper;
    }
}
