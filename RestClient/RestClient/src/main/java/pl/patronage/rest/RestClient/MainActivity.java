package pl.patronage.rest.RestClient;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.List;

import pl.patronage.rest.RestClient.httpengine.CollectionHttpEngine;
import pl.patronage.rest.RestClient.httpengine.UserHttpEngine;
import pl.patronage.rest.RestClient.model.Collection;
import pl.patronage.rest.RestClient.model.User;


public class MainActivity extends ActionBarActivity {

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
            testCollection.setOwner("tomek2");
            testCollection.setName("puzzle2");
            //testCollection.setTags(Arrays.asList("raz", "dwa", "trzy"));
            new LongRunningPostIO().execute(testCollection);
        }
        if (id == R.id.action_get_code) {
            User testUser = new User();
            testUser.setUsername("tomek");
            testUser.setPassword("tomek");

            UserHttpEngine userHttpEngine = new UserHttpEngine("http://78.133.154.18:8080/oauth/token/");
            userHttpEngine.getAuthenticationCode(testUser);
        }
        return super.onOptionsItemSelected(item);
    }




    private class LongRunningGetIO extends AsyncTask<Void, Collection, List<Collection>> {

        @Override
        protected List<Collection> doInBackground(Void... params) {
            CollectionHttpEngine collectionHttpEngine = new CollectionHttpEngine("http://78.133.154.39:2080/collections");
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
            CollectionHttpEngine collectionHttpEngine = new CollectionHttpEngine("http://78.133.154.39:2080/collections");
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
}
