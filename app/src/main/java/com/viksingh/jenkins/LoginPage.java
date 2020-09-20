package com.viksingh.jenkins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputLayout;
import com.viksingh.jenkins.Utils.JenkinServer;


public class LoginPage extends AppCompatActivity {
    private View button;
    private ImageView image;
    private TextInputLayout password;
    private AutoCompleteTextView url;
    private TextInputLayout username;
    private boolean FIRSTIME=true;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String JENKINSURL = "JenkinsUrl";
    public static final String  USERNAME= "userName";
    public static final String PASSWORD = "password";
    SharedPreferences sharedpreferences;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_login_page);
        if (Build.VERSION.SDK_INT > 9)
            StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder()).permitAll().build());

        url = (AutoCompleteTextView)findViewById(R.id.url);
        image = (ImageView)findViewById(R.id.dropdown);
        username = (TextInputLayout)findViewById(R.id.username);
        password = (TextInputLayout)findViewById(R.id.password);
        button = findViewById(R.id.btn);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LoginPage.this.url.showDropDown();
            }
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,getApplicationContext().getResources().getStringArray(R.array.URL));
        this.url.setThreshold(1);
        this.url.setAdapter(arrayAdapter);
        final AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.url, Patterns.WEB_URL, R.string.invaid_url);
        awesomeValidation.addValidation(this, R.id.username, "(?m)^\\s*\\S+[\\s\\S]*$", R.string.invaid_username);
        awesomeValidation.addValidation(this, R.id.password, ".{4,}", R.string.invaid_password);
        this.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                final String jenkinsUrl = LoginPage.this.url.getText().toString();
                final String userName = LoginPage.this.username.getEditText().getText().toString();
                final String passWord = LoginPage.this.password.getEditText().getText().toString();
                LoginPage loginPage = LoginPage.this;
                if (!loginPage.isNetworkAvaliable(loginPage)) {
                    loginPage = LoginPage.this;
                    loginPage.builder(loginPage).show();
                }
                if (!awesomeValidation.validate()) {
                    return;
                }
                final ProgressButton progressButton = new ProgressButton(LoginPage.this,paramView);
                progressButton.buttonActivated();
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        progressButton.buttonFinised();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if (JenkinServer.startServer(jenkinsUrl, userName, passWord)) {

                                        Intent intent = new Intent((Context)LoginPage.this, AllJobs.class);
                                        intent.putExtra("jenkinurl", jenkinsUrl);
                                        intent.putExtra("username", userName);
                                        intent.putExtra("password", passWord);
                                        LoginPage.this.startActivity(intent);
                                        Toast.makeText(LoginPage.this.getApplicationContext(), "login successfully",Toast.LENGTH_SHORT).show();
                                        LoginPage.this.finish();
                                } else {
                                    Toast.makeText(LoginPage.this.getApplicationContext(), "Please check your credentials!",Toast.LENGTH_SHORT).show();
                                    progressButton.userNotValidated();
                                }
                            }
                        },1000);
                    }
                },2000);
            }
        });
    }



    private boolean isUrlValid() {
        String str = this.url.getText().toString().trim();
        if (str.isEmpty()) {
            this.url.setError("Field cannot be empty");
            return false;
        }
        if (str.trim().length() > 0 && !str.matches("^((ftp|http|https):\\/\\/)?(www.)?(?!.*(ftp|http|https|www.))[a-zA-Z0-9_-]+(\\.[a-zA-Z]+)+((\\/)[\\w#]+)*(\\/\\w+\\?[a-zA-Z0-9_]+=\\w+(&[a-zA-Z0-9_]+=\\w+)*)?$")) {
            this.url.setError("Please enter valid Url");
            return false;
        }
        return true;
    }

    private Boolean validatePassword() {
        String str = this.password.getEditText().getText().toString();
        boolean bool = str.isEmpty();
        Boolean bool1 = Boolean.valueOf(false);
        if (bool) {
            this.password.setError("Field cannot be empty");
            return bool1;
        }
        if (!str.matches("^(?=.*[a-zA-Z])(?=\\S+$).{4,}$")) {
            this.password.setError("Password is too weak");
            return bool1;
        }
        this.password.setError(null);
        this.password.setErrorEnabled(false);
        return Boolean.valueOf(true);
    }

    private Boolean validateUsername() {
        String str = this.username.getEditText().getText().toString();
        boolean bool = str.isEmpty();
        Boolean bool1 = Boolean.valueOf(false);
        if (bool) {
            this.username.setError("Field cannot be empty");
            return bool1;
        }
        if (str.length() >= 15) {
            this.username.setError("Username too long");
            return bool1;
        }
        if (!str.matches("\\A\\w{4,20}\\z")) {
            this.username.setError("White Spaces are not allowed");
            return bool1;
        }
        this.username.setError(null);
        this.username.setErrorEnabled(false);
        return Boolean.valueOf(true);
    }

    public AlertDialog.Builder builder(Context paramContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(paramContext);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile or Wifi to Access this Press OK To Connect ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                LoginPage.this.startActivity(intent);
            }
        });
        return builder;
    }

   /* public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        finish();
    }*/

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }

}
