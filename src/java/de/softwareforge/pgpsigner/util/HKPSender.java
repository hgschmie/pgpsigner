package de.softwareforge.pgpsigner.util;

/*
 * Copyright (C) 2007 Henning P. Schmiedehausen
 *
 * See the NOTICE file distributed with this work for additional
 * information
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

import de.softwareforge.pgpsigner.PGPSigner;

/**
 * A simple HKP protocol uploader.
 *
 * @TODO: Needs some work re. error handling.
 *
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */

public class HKPSender
{

    public static final String UPLOAD_URL = "/pks/add";

    public static final int HKP_DEFAULT_PORT = 11371;

    private final HttpClient httpClient;

    public HKPSender(final String serverName)
    {
        HttpHost httpHost = new HttpHost(serverName, HKP_DEFAULT_PORT);
        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(httpHost);
        this.httpClient = new HttpClient();
        this.httpClient.getParams().setParameter("http.useragent", PGPSigner.APPLICATION_VERSION);
        httpClient.setHostConfiguration(hostConfig);
    }

    public boolean uploadKey(final byte[] armoredKey)
    {

        PostMethod post = new PostMethod(UPLOAD_URL);

        try
        {

            NameValuePair keyText = new NameValuePair("keytext", new String(armoredKey, "ISO-8859-1"));
            post.setRequestBody(new NameValuePair[] { keyText });
            post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

            int statusCode = httpClient.executeMethod(post);

            if (statusCode != HttpStatus.SC_OK)
            {
                return false;
            }

            InputStream responseStream = post.getResponseBodyAsStream();
            InputStreamReader isr = null;
            BufferedReader br = null;
            StringBuffer response = new StringBuffer();

            try
            {
                isr = new InputStreamReader(responseStream);
                br = new BufferedReader(isr);
                String line = null;

                while ((line = br.readLine()) != null)
                {
                    response.append(line);
                }
            }
            finally
            {
                IOUtils.closeQuietly(br);
                IOUtils.closeQuietly(isr);
                IOUtils.closeQuietly(responseStream);

            }
        }
        catch (RuntimeException re)
        {
            throw re;
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            post.releaseConnection();
        }
        return true;
    }
}
