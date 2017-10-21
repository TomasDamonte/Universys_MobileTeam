package universis.universys;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Didier on 05/12/2014.
 *
 * This class is used to perform asynchronous http requests. It supports POST, GET and PUT methods,
 * as well as the setting of headers, parameters and bodies (for POST and PUT only).
 * For ease of use, is recommended to use the static methods of the class to create requests. An
 * example of it:
 *
 * CHTTPRequest req = CHTTPRequest.postRequest(REQ_ID_MAIN_PAGE, "http://demo.quiendamenos.tv/",
 *                                              "Subastas.aspx");
 * req.addListener(this);
 * req.execute();
 *
 * Or even:
 *
 * CHTTPRequest.getRequest("http://www.google.com/").execute().addListener(this);
 */
public class CHTTPRequest extends AsyncTask<String, String, String>
{
    /**
     * From now on, static methods regarding creation of requests will be implemented. These methods
     * are shortcuts of one of them, so only that one will be properly explained.
     */
    public static CHTTPRequest getRequest(String url)
    {
        return getRequest(-1, url, null, null);
    }

    public static CHTTPRequest getRequest(int reqId, String url)
    {
        return getRequest(reqId, url, null, null);
    }

    public static CHTTPRequest getRequest(String base, String path)
    {
        return getRequest(-1, base, path);
    }

    public static CHTTPRequest getRequest(String url, HttpParams params)
    {
        return getRequest(-1, url, params);
    }

    public static CHTTPRequest getRequest(int reqId, String url, String path)
    {
        return getRequest(reqId, url, path, null);
    }

    public static CHTTPRequest getRequest(int reqId, String url, HttpParams params)
    {
        return getRequest(reqId, url, null, params);
    }

    public static CHTTPRequest getRequest(int reqId, String base, String path, HttpParams params)
    {
        return getRequest(reqId, base, path, null, params);
    }

    /**
     * Creates a http request that uses the GET method. Upon creation, the request DOESN'T execute.
     * You should call execute() method.
     *
     * @param reqId The ID this request will have. If your listener listens to many requests, you
     *              may want to give each an ID to identify them properly. This parameters is
     *              optional, set it to -1 if you want to ignore it.
     * @param base The base URL. It is separated from the path because it may be comfortable to
     *             inherit from this class if many requests are made to the same server, thus
     *             writing another method that defaults the base URL to the needed one. This
     *             parameter is mandatory, you may not ignore it.
     * @param path The path of the URL. This is what it comes after the base URL. For example:
     *             http://www.google.com/ would be the base URL, and 404.html the path. This
     *             parameter is optional. You may include the path into the base, if you want.
     * @param headers A map of Strings that will be set as headers of the http request. This
     *                parameter is optional. You may send null if want to ignore it.
     * @param params Parameters that will be put as variables of the GET method. This parameter is
     *               optional. You may send null if you want to ignore it.
     * @return An object holding all this information, ready to be executed.
     */
    public static CHTTPRequest getRequest(int reqId, String base, String path, Map<String, String> headers, HttpParams params)
    {
        CHTTPRequest req = new CHTTPRequest(reqId, HttpGet.METHOD_NAME);
        req.setBaseUrl(base);
        req.setPath(path);
        req.setHeaders(headers);
        req.setParams(params);
        return req;
    }

    public static CHTTPRequest postRequest(String url, JSONObject body)
    {
        return postRequest(-1, url, null, null, body);
    }

    public static CHTTPRequest postRequest(int reqId, String url, JSONObject body)
    {
        return postRequest(reqId, url, null, null, body);
    }

    public static CHTTPRequest postRequest(String url, HttpParams params, JSONObject body)
    {
        return postRequest(-1, url, null, params, body);
    }

    public static CHTTPRequest postRequest(int reqId, String base, String path, HttpParams params)
    {
        return postRequest(reqId, base, path, params, null);
    }

    public static CHTTPRequest postRequest(int reqId, String base, String path, JSONObject body)
    {
        return postRequest(reqId, base, path, null, body);
    }

    public static CHTTPRequest postRequest(int reqId, String base, String path, HttpParams params, JSONObject body)
    {
        return postRequest(reqId, base, path, null, params, body);
    }



    /**
     * Creates a http request that uses the POST method. Upon creation, the request DOESN'T execute.
     * You should call execute() method.
     *
     * @param reqId The ID this request will have. If your listener listens to many requests, you
     *              may want to give each an ID to identify them properly. This parameters is
     *              optional, set it to -1 if you want to ignore it.
     * @param base The base URL. It is separated from the path because it may be comfortable to
     *             inherit from this class if many requests are made to the same server, thus
     *             writing another method that defaults the base URL to the needed one. This
     *             parameter is mandatory, you may not ignore it.
     * @param path The path of the URL. This is what it comes after the base URL. For example:
     *             http://www.google.com/ would be the base URL, and 404.html the path. This
     *             parameter is optional. You may include the path into the base, if you want.
     * @param headers A map of Strings that will be set as headers of the http request. This
     *                parameter is optional. You may send null if want to ignore it.
     * @param params Parameters that will be put as variables of the POST method. This
     *                parameter is optional. You may send null if want to ignore it.
     * @param body The body of the POST message. We'll use JSON throughout the application, so it
     *             is a JSONObject right now. This parameter is optional. You may send null if want
     *             to ignore it.
     * @return An object holding all this information, ready to be executed.
     */
    public static CHTTPRequest postRequest(int reqId, String base, String path, Map<String, String> headers, HttpParams params, JSONObject body)
    {
        CHTTPRequest req = new CHTTPRequest(reqId, HttpPost.METHOD_NAME);
        req.setBaseUrl(base);
        req.setPath(path);
        req.setHeaders(headers);
        req.setParams(params);
        req.setBody(body);
        return req;
    }

    public static CHTTPRequest putRequest(String url, HttpParams params)
    {
        return putRequest(url, params, null);
    }

    public static CHTTPRequest putRequest(String url, JSONObject body)
    {
        return putRequest(url, null, body);
    }

    public static CHTTPRequest putRequest(String url, HttpParams params, JSONObject body)
    {
        return putRequest(-1, url, params, body);
    }

    public static CHTTPRequest putRequest(int reqId, String url, HttpParams params, JSONObject body)
    {
        return putRequest(reqId, url, null, params, body);
    }

    public static CHTTPRequest putRequest(int reqId, String base, String path, HttpParams params, JSONObject body)
    {
        return putRequest(reqId, base, path, null, params, body);
    }

    /**
     * Creates a http request that uses the PUT method. Upon creation, the request DOESN'T execute.
     * You should call execute() method.
     *
     * @param reqId The ID this request will have. If your listener listens to many requests, you
     *              may want to give each an ID to identify them properly. This parameters is
     *              optional, set it to -1 if you want to ignore it.
     * @param base The base URL. It is separated from the path because it may be comfortable to
     *             inherit from this class if many requests are made to the same server, thus
     *             writing another method that defaults the base URL to the needed one. This
     *             parameter is mandatory, you may not ignore it.
     * @param path The path of the URL. This is what it comes after the base URL. For example:
     *             http://www.google.com/ would be the base URL, and 404.html the path. This
     *             parameter is optional. You may include the path into the base, if you want.
     * @param headers A map of Strings that will be set as headers of the http request. This
     *                parameter is optional. You may send null if want to ignore it.
     * @param params Parameters that will be put as variables of the PUT method. This
     *                parameter is optional. You may send null if want to ignore it.
     * @param body The body of the PUT message. We'll use JSON throughout the application, so it
     *             is a JSONObject right now. This parameter is optional. You may send null if want
     *             to ignore it.
     * @return An object holding all this information, ready to be executed.
     */
    public static CHTTPRequest putRequest(int reqId, String base, String path, Map<String, String> headers, HttpParams params, JSONObject body)
    {
        CHTTPRequest req = new CHTTPRequest(reqId, HttpPut.METHOD_NAME);
        req.setBaseUrl(base);
        req.setPath(path);
        req.setHeaders(headers);
        req.setParams(params);
        req.setBody(body);
        return req;
    }

    /**
     * Now a series of constructors came up. They are pretty useless as the static creation methods
     * are more comfortable to use, but they are here anyway.
     */
    public CHTTPRequest()
    {
        m_taskId = -1;
    }

    public CHTTPRequest(String method)
    {
        this();
        setMethod(method);
    }

    public CHTTPRequest(int taskId)
    {
        m_taskId = taskId;
    }

    public CHTTPRequest(int taskId, String method)
    {
        this(taskId);
        setMethod(method);
    }

    /**
     * Builds the final URL by joining the base URL and the path, if any. If there is no base set,
     * the result will be null.
     * @return The built URL, or null if there is no base URL.
     */
    public String getFinalUrl()
    {
        String finalUrl = getBaseUrl();

        // No base URL, we can do nothing. Where would the request be made?
        if(getBaseUrl() == null)
        {
            return null;
        }

        // Check there is a path. If there isn't, maybe the user entered it along with the
        // base URL.
        if(getPath() != null)
        {
            // If the base URL has a slash at the end AND the path has one at the beginning, one of
            // them must be stripped. I chose to remove the one from the path.
            if(getBaseUrl().charAt(getBaseUrl().length() - 1) == '/' && getPath().charAt(0) == '/')
            {
                finalUrl += getPath().substring(1);
            }
            // If the base URL DOESN'T have a slash at the end AND the path DOESN'T have one at the
            // beginning, we must add one to join them.
            else if(getBaseUrl().charAt(getBaseUrl().length() - 1) != '/' && getPath().charAt(0) != '/')
            {
                finalUrl += "/" + getPath();
            }
            // If there is only one slash while joining the base URL and the path, everything is ok.
            else
            {
                finalUrl += getPath();
            }
        }

        return finalUrl;
    }

    /**
     * Executes the request with the given information. It returns a pointer to itself. If the URL
     * is badly formed (no base URL), the method returns null instead of itself.
     * @return A pointer to the calling instance or null if failed.
     */
    public CHTTPRequest execute()
    {
        String finalUrl = getFinalUrl();

        if(finalUrl == null)
        {
            return null;
        }

        // This method will later call the doInBackground one, who really does all the magic.
        super.execute(finalUrl);

        return this;
    }

    /**
     * Sets the communication method. Could be GET, POST or PUT. Use METHOD_NAME for safety; that
     * would be HttpGet.METHOD_NAME, HttpPost.METHOD_NAME and HttpPut.METHOD_NAME.
     * @param method The method used to establish the communication.
     */
    public void setMethod(String method)
    {
        m_method = method;
    }

    /**
     * Returns the method that would be used or was used to establish the communication. Options are
     * GET, POST and PUT.
     * @return The method this request used or would use.
     */
    public String getMethod()
    {
        return m_method;
    }

    /**
     * Adds a listener to this request. Listeners will be told when the request has received an
     * answer.
     * @param listener A pointer to an object implementing IRequestListener.
     */
    public void addListener(IRequestListener listener)
    {
        m_listeners.add(listener);
    }

    /**
     * Removes the given listener from the listener list of this request.
     * @param listener The listener to be removed.
     */
    public void removeListener(IRequestListener listener)
    {
        m_listeners.remove(listener);
    }

    /**
     * Returns the ID of the request or task. This ID was set by the user by calling setTaskId or
     * when creating the object using the static methods xxxRequest(...).
     *
     * @return The ID of the request or task.
     */
    public int getTaskId()
    {
        return m_taskId;
    }

    /**
     * Sets the ID of this request or task. It is to identify later if you are listening to it.
     * @param taskId The new ID for this request.
     */
    public void setTaskId(int taskId)
    {
        m_taskId = taskId;
    }

    /**
     * The body message. Notice that this will be used with POST and PUT methods, not with GET one.
     * You should use setParams() method to send info using GET method.
     * @param params The data to be sent, in JSON format.
     */
    public void setBody(JSONObject params)
    {
        m_body = params;
    }

    /**
     * Returns the body of the message, set by the user.
     * @return The body of the message.
     */
    public JSONObject getBody()
    {
        return m_body;
    }

    /**
     * Sets the params of the message. This would be the ones you see after the ? while using the
     * GET method.
     * @param params The parameters, as key value (HttpParams is mostly that).
     */
    public void setParams(HttpParams params)
    {
        m_params = params;
    }

    /**
     * Returns the parameters of the communication.
     * @return The parameters of the communication.
     */
    public HttpParams getParams()
    {
        return m_params;
    }

    /**
     * Sets custom headers for the communication. All methods can use them. If there were previously
     * set headers, calling this method will erase them.
     * @param headers A map of keys and values that represent the headers and their values.
     */
    public void setHeaders(Map<String, String> headers)
    {
        m_headers = headers;
    }

    /**
     * Sets custom headers for the communication. All methods can use them. This method will not
     * overwrite previous headers, if any. If there weren't headers set at all, the map of them will
     * be created as a HashMap.
     * @param keysValues A variadic range of Strings that represents the keys and the values. The
     *                   correct way of passing them is Key, Value, Key, Value, and so on.
     */
    public void setHeaders(String... keysValues)
    {
        // The object may be null. Create it in that case. Otherwise, use the existing one and
        // overwrite existing headers.
        if(m_headers == null)
        {
            m_headers = new HashMap<String, String>();
        }

        // Iterate through the values given. The order is Key, Value, Key, Value. If there is an
        // odd number of arguments, the last one will be ignored.
        for(int i = 0; i < keysValues.length; i++)
        {
            if(i + 1 < keysValues.length)
            {
                m_headers.put(keysValues[i], keysValues[i + 1]);
            }
        }
    }

    /**
     * Sets an individual header. If there weren't headers set at all, the map of them will be
     * created as a HashMap. If there were previously set headers, they will be kept.
     * @param key The key of the header.
     * @param value The value for that key.
     */
    public void setHeader(String key, String value)
    {
        if(m_headers == null)
        {
            m_headers = new HashMap<String, String>();
        }

        m_headers.put(key, value);
    }

    /**
     * Removes the specified header from the list.
     * @param key The key of the header to be removed.
     */
    public void removeHeader(String key)
    {
        if(m_headers.containsKey(key))
        {
            m_headers.remove(key);
        }
    }

    /**
     * Returns the headers that would be used for this request.
     * @return The Map of headers.
     */
    public Map<String, String> getHeaders()
    {
        return m_headers;
    }

    /**
     * Sets the base URL for this communication. You may also set it as the full URL (including the
     * path) if you wish.
     * @param base The base or full URL for the communication.
     */
    public void setBaseUrl(String base)
    {
        m_baseUrl = base;
    }

    /**
     * Returns the base URL that will be used for this communication.
     * @return The base URL that will be used for this communication.
     */
    public String getBaseUrl()
    {
        return m_baseUrl;
    }

    /**
     * Sets the path of the communication. You can ignore it and use only the base URL with the path
     * integrated there.
     * @param path The path of the communication.
     */
    public void setPath(String path)
    {
        m_path = path;
    }

    /**
     * Returns the path of the communication.
     * @return The pat of the communication.
     */
    public String getPath()
    {
        return m_path;
    }

    /**
     * Method of the base class, called when the result of the communication arrives. On it, the
     * listeners are notified of the arrival of the response. This method is used internally and
     * should not be called manually.
     * If any listener returns true, the other won't be notified.
     * @param result The result String of the response from the server.
     */
    @Override
    protected void onPostExecute(String result)
    {
        for(IRequestListener listener : m_listeners)
        {
            if(listener.onResponse(this, m_response))
            {
                break;
            }
        }
    }

    /**
     * Returns the response that came from the server.
     * @return The response that came from the server.
     */
    public String getResponse()
    {
        return m_response;
    }

    /**
     * This is where the request is really executed. This method is used internally by AsyncTask and
     * should not be called manually.
     * @param uri The URL to hit.
     * @return The response of the server.
     */
    @Override
    protected String doInBackground(String... uri)
    {
        // Create a client to perform the operation.
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        //Para q salga el simbolito de 'cargando' =P

        try
        {
            // Initialize a variable as null to later hold the real request object.
            HttpRequestBase req = null;

            // Create the request depending on the selected method (POST, GET, PUT).
            // GET doesn't use the body.
            if(HttpPost.METHOD_NAME.equalsIgnoreCase(getMethod()))
            {
                HttpPost preq = new HttpPost(uri[0]);
                if(getBody() != null)
                {
                    preq.setEntity(new StringEntity(getBody().toString()));
                }

                if(getParams() != null)
                {
                    preq.setParams(getParams());
                }
                req = preq;
            }
            else if(HttpGet.METHOD_NAME.equalsIgnoreCase(getMethod()))
            {
                HttpGet greq = new HttpGet(uri[0]);
                if(getParams() != null)
                {
                    greq.setParams(getParams());
                }
                req = greq;
            }
            else if(HttpPut.METHOD_NAME.equalsIgnoreCase(getMethod()))
            {
                HttpPut preq = new HttpPut(uri[0]);
                if(getBody() != null)
                {
                    preq.setEntity(new StringEntity(getBody().toString()));
                }

                if(getParams() != null)
                {
                    preq.setParams(getParams());
                }
                req = preq;
            }

            // Set the headers for the request, if any.
            if(getHeaders() != null && req != null)
            {
                for(String key : getHeaders().keySet())
                {
                    req.setHeader(key, getHeaders().get(key));
                }
            }
            // Execute the http request and wait for a response. This method is synchronized, but
            // this method runs in another thread, as of AsyncTask implementation.
            response = httpclient.execute(req);
          /*  try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            // Response arrived, Check what is the status.
            StatusLine statusLine = response.getStatusLine();
            // If everything is OK, convert the response into a String.

            if(statusLine.getStatusCode() <= HttpStatus.SC_MULTIPLE_CHOICES)
            {
              /*ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = new String(out.toByteArray(), "ISO-8859-1");
                 */
                responseString = EntityUtils.toString(response.getEntity());

                //Hardcodeo de respuesta jej >.>
                try {
                  responseString = DataBase.respuestaDB(this).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CacheHelper.setStringProperty(m_taskId + m_body.toString(),responseString);

            }
            // If something happened, throw an exception with the error.
            else
            {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
            responseString = CacheHelper.getStringProperty(m_taskId + m_body.toString());
            //TODO Handle problems..
        }
        catch (IOException e)
        {
            responseString = CacheHelper.getStringProperty(m_taskId + m_body.toString());
            e.printStackTrace();
            //TODO Handle problems..
        }

        // Store the response for later access
        m_response = responseString;
        return responseString;
    }

    public JSONObject getJsonResponse() throws JSONException {
        return new JSONObject(m_response);
    }

    public JSONArray getJsonArrayResponse() throws JSONException {
        return new JSONArray(m_response);
    }

    private ArrayList<IRequestListener> m_listeners = new ArrayList<>();
    protected int m_taskId;
    private String m_response = null;
    private String m_method = "POST";
    private JSONObject m_body = null;
    private HttpParams m_params = null;
    private Map<String, String> m_headers = null;
    private String m_baseUrl;
    private String m_path;

}
