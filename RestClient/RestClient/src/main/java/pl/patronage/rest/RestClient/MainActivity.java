package pl.patronage.rest.RestClient;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import pl.patronage.rest.RestClient.httpengine.HttpEngine;
import pl.patronage.rest.RestClient.httpengine.UserHttpEngine;
import pl.patronage.rest.RestClient.model.Collection;
import pl.patronage.rest.RestClient.model.User;


public class MainActivity extends ActionBarActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new LongRunningGetIO().execute();
        }
        if (id == R.id.action_add) {
            Collection testCollection = new Collection();
            testCollection.setOwner("tomek9");
            testCollection.setName("puzzle9");
            //testCollection.setTags(Arrays.asList("raz", "dwa", "trzy"));
            new LongRunningPostIO().execute(testCollection);
        }
        if (id == R.id.action_get_code) {
            User testUser = new User();
            testUser.setUsername("tomek");
            testUser.setPassword("tomek");

            new getAuthenticationCode().execute(testUser);
        }
        if (id == R.id.action_delete) {
            new removeCollection().execute(107);
        }
        if (id == R.id.action_update) {
            Collection testCollection = new Collection();
            testCollection.setId("89");
            testCollection.setOwner("tomek55");
            testCollection.setName("puzzle55");
            new updateCollection().execute(testCollection);
        }
        return super.onOptionsItemSelected(item);
    }




    private class LongRunningGetIO extends AsyncTask<Void, Collection, List<Collection>> {

        @Override
        protected List<Collection> doInBackground(Void... params) {
            HttpEngine collectionHttpEngine = new HttpEngine<Collection>("http://78.133.154.39:2080/collections");
            return collectionHttpEngine.getList();
        }

        protected void onPostExecute(List<Collection> collections) {
            for (Collection c: collections) {
                Log.d("TAG", c.toString());
            }
        }
    }



    private class LongRunningPostIO extends AsyncTask<Collection, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Collection... params) {
            HttpEngine collectionHttpEngine = new HttpEngine("http://78.133.154.39:2080/collections");
            return collectionHttpEngine.create(params[0]);
        }

        protected void onPostExecute(Boolean response) {
            if (response) {
                Log.d("TAG", "Dodano pomyslnie");
            }
            else {
                Log.d("TAG", "Jakis błąd przy dodawaniu");
            }
        }
    }


    private class getAuthenticationCode extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... params) {
            UserHttpEngine userHttpEngine = new UserHttpEngine("http://78.133.154.18:8080/oauth/token/");
            return userHttpEngine.getAuthenticationCode(params[0]);
        }

        protected void onPostExecute(String response) {
            textView = (TextView) findViewById(R.id.textView);
            if (!response.isEmpty())
            textView.setText(response);
        }
    }



    private class removeCollection extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            HttpEngine collectionHttpEngine = new HttpEngine("http://78.133.154.39:2080/collections");
            collectionHttpEngine.remove(params[0]);
            return null;
        }
    }

    private class updateCollection extends AsyncTask<Collection, Void, Void> {

        @Override
        protected Void doInBackground(Collection... params) {
            HttpEngine collectionHttpEngine = new HttpEngine("http://78.133.154.39:2080/collections");
            collectionHttpEngine.update(params[0],Integer.parseInt(params[0].getId()));
            return null;
        }
    }
}
