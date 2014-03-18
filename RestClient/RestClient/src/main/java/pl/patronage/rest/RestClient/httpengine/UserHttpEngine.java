package pl.patronage.rest.RestClient.httpengine;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pl.patronage.rest.RestClient.model.User;

/**
 * Created by gohilukk on 18.03.14.
 */
public class UserHttpEngine implements IHttpEngine<User> {

    private String url;

    public UserHttpEngine(String url) {
        this.url = url;
    }

    public String getAuthenticationCode(User user) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit
        HttpResponse response;
        JSONObject json = new JSONObject();

        try {
            HttpPost post = new HttpPost(url);
            json.put("username", user.getUsername());
            json.put("password", user.getPassword());
            json.put("grant_type", "password");
            StringEntity se = new StringEntity( json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = httpClient.execute(post);

            /*Checking response */
            if(response!=null){
                HttpEntity responseEntity = response.getEntity();
                String HTTP_response = null;
                String stringResponse =null;
                HTTP_response = EntityUtils.toString(responseEntity, HTTP.UTF_8);
                Log.d("TAG", "Jsontext = " + HTTP_response);
                //jezeli odpowiedz zawiera kod Created
                if (HTTP_response.contains("200"))
                {
                    stringResponse = getASCIIContentFromEntity(responseEntity);
                    Log.d("TAG", stringResponse);
                    return null;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getList() {
        return null;
    }

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public boolean create(User user) {
        return false;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void remove(int id) {

    }

    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[4096];
            n =  in.read(b);
            if (n>0) out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}
