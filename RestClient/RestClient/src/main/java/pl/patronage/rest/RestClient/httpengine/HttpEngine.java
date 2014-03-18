package pl.patronage.rest.RestClient.httpengine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pl.patronage.rest.RestClient.model.Collection;
import pl.patronage.rest.RestClient.model.IModel;

/**
 * Created by gohilukk on 17.03.14.
 */
public class HttpEngine<T> implements IHttpEngine<T> {

    protected String url;

    public HttpEngine(String url) {
        this.url = url;
    }

    @Override
    public List<T> getList() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(url);
        String stringResponse = null;
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            stringResponse = getASCIIContentFromEntity(entity);

            Type collectionType = new TypeToken<List<Collection>>() {
            }.getType();
            List<T> items = (List<T>) new Gson().fromJson(stringResponse, collectionType);

            return items;
        } catch (Exception e) {
            //return e.getLocalizedMessage();
            return null;
        }
    }

    @Override
    public T get(int id) {
        return null;
    }

    @Override
    public boolean create(IModel item) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit
        HttpResponse response;
        JSONObject json = new JSONObject();

        try {
            HttpPost post = new HttpPost(url);

            json = item.toJson();

            StringEntity se = new StringEntity(json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = httpClient.execute(post);

            /*Checking response */
            if (response != null) {
                HttpEntity responseEntity = response.getEntity();
                String HTTP_response = null;
                HTTP_response = EntityUtils.toString(responseEntity, HTTP.UTF_8);
                Log.d("TAG", "Jsontext = " + HTTP_response);
                //jezeli odpowiedz zawiera kod Created
                if (HTTP_response.contains("error_code")) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(IModel item, int id) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit
        HttpResponse response;


        try {
            HttpPut httpPut = new HttpPut(url + "/" + id);
            JSONObject json = item.toJson();

            StringEntity se = new StringEntity(json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPut.setEntity(se);
            response = httpClient.execute(httpPut);

            /*Checking response */
            if (response != null) {
                HttpEntity responseEntity = response.getEntity();
                String HTTP_response = null;
                HTTP_response = EntityUtils.toString(responseEntity, HTTP.UTF_8);
                Log.d("TAG", "Jsontext = " + HTTP_response);
                //jezeli odpowiedz zawiera kod Created
                if (HTTP_response.contains("error_code")) {
                    Log.d("TAG", "blad");
                } else {
                    Log.d("TAG", "zupdatowano id=" + id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit
        HttpDelete httpDelete = new HttpDelete(url+"/"+Integer.toString(id));

        Log.d("TAG",url+"/"+Integer.toString(id));

        try {
            HttpResponse httpResponse = httpClient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0) out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}