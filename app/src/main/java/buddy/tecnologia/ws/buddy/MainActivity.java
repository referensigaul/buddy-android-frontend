package buddy.tecnologia.ws.buddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        JSONObject loggedUser = loggedUser();

        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.email_user);

        try {
            nav_user.setText(loggedUser.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        displayFrag(R.id.nav_cronograma);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displayFrag(id);
        return true;
    }

    private void displayFrag(int itemId){
        Fragment fragment = null;

        switch (itemId){
            case R.id.nav_cronograma:
                fragment = new Cronogramas();
                break;

            case R.id.nav_logout:
                logoutUser();
                break;
        }

        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void logoutUser(){

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet clientGet = new HttpGet("http://buddy.tecnologia.ws/api/user/logout");
        String token = UserSession.getInstance(this).getUserToken();
        clientGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        clientGet.setHeader("Accept", "application/json");
        clientGet.setHeader("Authorization", "Bearer " + token);
        Integer codeStatus = null;
        Boolean retunValue = false;
        JSONObject result = null;

        try {

            HttpResponse response = httpClient.execute(clientGet);

            String json = EntityUtils.toString(response.getEntity());
            result = new JSONObject(json);
            codeStatus = response.getStatusLine().getStatusCode();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if(codeStatus != null && codeStatus == 200){
            UserSession.getInstance(this).clearSession();
        }

        startActivity(new Intent(this, LoginActivity.class));
    }

    public JSONObject loggedUser(){

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet clientGet = new HttpGet("http://buddy.tecnologia.ws/api/user");
        String token = UserSession.getInstance(this).getUserToken();
        clientGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        clientGet.setHeader("Accept", "application/json");
        clientGet.setHeader("Authorization", "Bearer " + token);
        Integer codeStatus = null;
        Boolean retunValue = false;
        JSONObject result = null;

        try {

            HttpResponse response = httpClient.execute(clientGet);

            String json = EntityUtils.toString(response.getEntity());
            result = new JSONObject(json);
            codeStatus = response.getStatusLine().getStatusCode();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return result;


    }
}
