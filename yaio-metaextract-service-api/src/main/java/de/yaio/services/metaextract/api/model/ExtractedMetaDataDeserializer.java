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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

/** 
 * deserializer for ExtractedMetaData-nodes
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class ExtractedMetaDataDeserializer extends JsonDeserializer<ExtractedMetaData> {

    @Override
    public ExtractedMetaData deserialize(final JsonParser jsonParser, final DeserializationContext ctxt)
            throws IOException {
        ObjectCodec oc = jsonParser.getCodec();

        JsonNode node = oc.readTree(jsonParser);
        
        // create obj from node
        ExtractedMetaData result = deserializeJSONNode(node);
        return result;
    }
    
    protected ExtractedMetaData deserializeJSONNode(final JsonNode node) throws IOException {
        // configure
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        // etxract data
        ExtractedMetaData result = mapper.treeToValue(node, ExtractedMetaData.class);;
        JsonNode childNodes = node.get("versions");
        
        // create Childnodes
        for (JsonNode childNode : childNodes) {
            ExtractedMetaDataVersion version = deserializeJSONChildNode(childNode);
            result.getVersions().put(version.getParserName(), version);
        }
        return result;
    }

    protected ExtractedMetaDataVersion deserializeJSONChildNode(final JsonNode node) throws IOException {
        // configure
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        // etxract data
        ExtractedMetaDataVersion result = mapper.treeToValue(node, ExtractedMetaDataVersion.class);;
        return result;
    }
}
