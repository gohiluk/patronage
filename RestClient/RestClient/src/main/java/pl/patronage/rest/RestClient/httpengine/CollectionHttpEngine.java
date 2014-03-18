package pl.patronage.rest.RestClient.httpengine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

/**
 * Created by gohilukk on 17.03.14.
 */
public class CollectionHttpEngine implements IHttpEngine<Collection> {

    private String url;

    public CollectionHttpEngine(String url) {
        this.url = url;
    }

    @Override
    public List<Collection> getList() {
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
            List<Collection> collections = (List<Collection>) new Gson().fromJson(stringResponse, collectionType);

            return collections;
        } catch (Exception e) {
            //return e.getLocalizedMessage();
            return null;
        }
    }

    @Override
    public Collection get(int id) {
        return null;
    }

    @Override
    public boolean create(Collection collection) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit
        HttpResponse response;
        JSONObject json = new JSONObject();

        try {
            HttpPost post = new HttpPost(url);
            json.put("owner", collection.getOwner());
            json.put("name", collection.getName());
            json.put("description", collection.getDescription());

            //Tagi obecnie nie dzialaja na serwerze

            /*JSONArray arrayTags = new JSONArray();
            for (String s: collection.getTags()) {
                arrayTags.put(s);
            }

            json.put("tags", arrayTags);*/
            json.put("items_number", collection.getItems_number());
            json.put("created_date", collection.getCreated_date());
            json.put("modified_date", collection.getModified_date());
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
    public void update(Collection o) {

    }

    @Override
    public void remove(int id) {

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