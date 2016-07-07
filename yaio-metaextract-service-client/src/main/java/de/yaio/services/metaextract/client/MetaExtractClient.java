/** 
 * software for projectmanagement and documentation
 * 
 * @FeatureDomain                Collaboration 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 * @category                     collaboration
 * @copyright                    Copyright (c) 2014, Michael Schreiner
 * @license                      http://mozilla.org/MPL/2.0/ Mozilla Public License 2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package de.yaio.services.metaextract.client;

import de.yaio.commons.http.HttpUtils;
import de.yaio.commons.io.IOExceptionWithCause;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** 
 * metaextract-client
 * 
 * @author                       Michael Schreiner <michael.schreiner@your-it-fellow.de>
 */
public class MetaExtractClient {
    protected String metaextracturl;
    protected String metaextractusername;
    protected String metaextractpassword;

    protected MetaExtractClient(final String metaextracturl, final String metaextractusername,
                                final String metaextractpassword) {
        this.metaextracturl = metaextracturl;
        this.metaextractusername = metaextractusername;
        this.metaextractpassword = metaextractpassword;
    }

    public static MetaExtractClient createClient(final String metaextracturl, final String metaextractusername,
                               final String metaextractpassword) {
        if (StringUtils.isEmpty(metaextracturl)) {
            throw new IllegalArgumentException("cant create webshotclient: metaextracturl must not be empty");
        }
        if (StringUtils.isEmpty(metaextractusername)) {
            throw new IllegalArgumentException("cant create webshotclient: metaextractusername must not be empty");
        }
        if (StringUtils.isEmpty(metaextractpassword)) {
            throw new IllegalArgumentException("cant create webshotclient: metaextractpassword must not be empty");
        }
        return new MetaExtractClient(metaextracturl, metaextractusername, metaextractpassword);
    }


    /**
     * create a metaextract of the url
     * @return                       returns the metaextract as JSON
     * @param url                    url to make a metaextract from
     * @throws IOExceptionWithCause  if something went wrong with metaextract
     * @throws IOException           if something went wrong with response-io
     */
    public byte[] getMetaExtractFromUrl(final String url) throws IOExceptionWithCause, IOException {
        // get metadata from url
        Map<String, String> params = new HashMap<String, String>();
        params.put("lang", "de");
        params.put("url", url);

        // call url
        String baseUrl = metaextracturl + "/getByUrl";
        HttpEntity entity;
        HttpResponse response;
        response = HttpUtils.callPostUrlPure(baseUrl, metaextractusername, metaextractpassword, params, null, null);
        entity = response.getEntity();

        // check response
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOExceptionWithCause("error while extracting metadata for url", url,
                    new IOException("illegal reponse:" + response.getStatusLine()
                            + " for baseurl:" + baseUrl + " with url:" + url
                            + " response:" + EntityUtils.toString(entity)));
        }

        return EntityUtils.toByteArray(entity);
    }

    /**
     * create a metaextract of the file
     * @return                       returns the metaextract as JSON
     * @param fileName               file to make a metaextract from
     * @throws IOExceptionWithCause  if something went wrong with metaextract
     * @throws IOException           if something went wrong with response-io
     */
    public byte[] getMetaExtractFromFile(final String fileName) throws IOExceptionWithCause, IOException {
        // get metadata from file
        Map<String, String> params = new HashMap<String, String>();
        params.put("lang", "de");
        Map<String, String> binfileParams = new HashMap<String, String>();
        binfileParams.put("file", fileName);

        // call url
        String baseUrl = metaextracturl + "/getByFile";
        HttpEntity entity;
        HttpResponse response;
        response = HttpUtils.callPostUrlPure(baseUrl, metaextractusername, metaextractpassword, params, null, binfileParams);
        entity = response.getEntity();

        // check response
        int retCode = response.getStatusLine().getStatusCode();
        if (retCode < 200 || retCode > 299) {
            throw new IOExceptionWithCause("error while extracting metadata for file", fileName,
                    new IOException("illegal reponse:" + response.getStatusLine()
                            + " for baseurl:" + baseUrl + " with file:" + fileName 
                            + " response:" + EntityUtils.toString(entity)));
        }

        return EntityUtils.toByteArray(entity);
    }
}
