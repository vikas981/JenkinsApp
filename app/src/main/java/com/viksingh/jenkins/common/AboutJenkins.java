package com.viksingh.jenkins.common;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;


import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.viksingh.jenkins.R;
import com.viksingh.jenkins.Utils.JenkinServer;

import java.util.HashMap;
import java.util.Map;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutJenkins extends AppCompatActivity {
    public static Map<String,Object> aboutMap=new HashMap<>();;;
    SimpleArcDialog mDialog;
    ArcConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = new SimpleArcDialog(this);



// Using this configuration with ArcLoader
        Intent intent=getIntent();
        new About().execute(intent.getStringExtra("jenkinsurl"),intent.getStringExtra("username"),intent.getStringExtra("password"));



    }

   class About extends AsyncTask<String,String,Map<String,Object>>{

        @Override
        protected Map<String,Object> doInBackground(String... strings) {
            if(aboutMap.isEmpty()) {
                aboutMap = JenkinServer.getAboutJenkins(strings[0], strings[1], strings[2]);
            }
            return aboutMap;
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            super.onPostExecute(stringObjectMap);
            Element versionElement = new Element();
            versionElement.setTitle("Version "+stringObjectMap.get("version"));
            Element adsElement = new Element();
            adsElement.setTitle("Total no of executors "+stringObjectMap.get("totalNoofExecutors"));
            View aboutPage = new AboutPage(AboutJenkins.this)
                    .isRTL(false)
                    .setDescription(getString(R.string.about_page_description))
                    .setImage(R.drawable.jenkins)
                    .addItem(versionElement)
                    .addItem(adsElement)
                    .addGroup("Connect with us")
                    .addEmail("vs98990@gmail.com")
                    .addFacebook("the.medy")
                    .addTwitter("af9acb6213f44be")
                    .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                    .addPlayStore("com.ideashower.readitlater.pro")
                    .addInstagram("im_singhji")
                    .addGitHub("medyo")
                    .create();

            setContentView(aboutPage);
           mDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setConfiguration(new ArcConfiguration(AboutJenkins.this));
            mDialog.show();

        }
    }

}