package buddy.tecnologia.ws.buddy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity implements Runnable{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(this, 2000);

    }

    public void run(){

        UserSession userSession = UserSession.getInstance(getApplicationContext());

        if(!userSession.isUserLoggedIn()){
            startActivity(new Intent(this, LoginActivity.class));
        }else{
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }

}
