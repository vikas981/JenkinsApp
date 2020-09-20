package com.viksingh.jenkins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.leo.simplearcloader.SimpleArcLoader;
import com.offbytwo.jenkins.model.Job;
import com.viksingh.jenkins.Adapter.AllJobAdapter;
import com.viksingh.jenkins.Model.AllJobModel;
import com.viksingh.jenkins.Utils.JenkinServer;
import com.viksingh.jenkins.common.AboutJenkins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllJobs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<AllJobModel> array_sort;
    public static ArrayList<AllJobModel> jobList;
    private ActionBarDrawerToggle actionBar;
    AllJobModel allJobModel;

    private DrawerLayout drawerLayout;
    private String jenkinsUrl;
    private AllJobAdapter jobAdapter;
    Map<String, Job> jobMap;
    private ListView listView;
    private NavigationView navigationView;
    private String password;
    private EditText search;
    private SearchView searchView;
    private SimpleArcLoader simpleArcLoader;
    private TextView textView;
    int textlength = 0;
    private Toolbar toolbar;
    private String userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        array_sort=new ArrayList<>();
        jobList=new ArrayList<>();
        jobMap = new HashMap<>();
        setContentView(R.layout.activity_all_jobs);
        search = (EditText) findViewById(R.id.search_bar);
        listView = (ListView) findViewById(R.id.listMode);
        simpleArcLoader = (SimpleArcLoader) findViewById(R.id.loader);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        textView = (TextView) findViewById(R.id.textView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener((DrawerLayout.DrawerListener) this.actionBar);
        actionBar.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.bringToFront();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        Intent intent = getIntent();
        this.jenkinsUrl = intent.getStringExtra("jenkinurl");
        this.userName = intent.getStringExtra("username");
        this.password = intent.getStringExtra("password");
        new LoadDataInBackGround().execute(jenkinsUrl, userName, password);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AllJobs allJobs = AllJobs.this;
                allJobs.startActivity((new Intent(allJobs.getApplicationContext(), JobDetail.class)).putExtra("position", i).putExtra("jenkinsurl", AllJobs.this.jenkinsUrl).putExtra("username", AllJobs.this.userName).putExtra("password", AllJobs.this.password));
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                array_sort.clear();
                textlength = AllJobs.this.search.getText().length();
                for (i = 0; i < jobList.size(); i++) {
                    if (textlength <= (jobList.get(i).getJob().length()) && (jobList.get(i).getJob().toLowerCase().trim().contains(search.getText().toString().toLowerCase().trim())))
                        array_sort.add(jobList.get(i));
                }
                jobAdapter=new AllJobAdapter(AllJobs.this, array_sort);
                listView.setAdapter(jobAdapter);
                jobAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        System.out.println(id);
        System.out.println(R.id.nav_home);
        if (id==R.id.nav_home){

        }
        else if(id==R.id.nav_profile) {
            Intent intent = new Intent(this, Profile.class);
            intent.putExtra("username", this.userName);
            startActivity(intent);
        }else if(id==R.id.nav_logout) {
            startActivity(new Intent(this, LoginPage.class));
            finish();
            Toast.makeText((Context) this, "Successfully Logout!", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.nodes) {
            Intent intent = new Intent(this, Nodes.class);
            intent.putExtra("jenkinsurl", this.jenkinsUrl);
            intent.putExtra("username", this.userName);
            intent.putExtra("password", this.password);
            startActivity(intent);
        }else if(id==R.id.nav_about){
                Intent intent = new Intent(this, AboutJenkins.class);
                intent.putExtra("jenkinsurl", this.jenkinsUrl);
                intent.putExtra("username", this.userName);
                intent.putExtra("password", this.password);
                startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.search_bar);
        searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getData(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void getData(String text){
        array_sort.clear();
        ArrayList<AllJobModel> filteredList =new ArrayList<>();
        for(int i=0;i<jobList.size();i++){
            array_sort.add(jobList.get(i));
        }
        if(searchView!=null){
            for (AllJobModel model:array_sort){
                if(model.getJob().toLowerCase().startsWith(text.toLowerCase())) {
                    filteredList.add(model);
                }
            }
        }else {
            filteredList=array_sort;
        }
        array_sort=filteredList;
        jobAdapter=new AllJobAdapter(AllJobs.this,filteredList);
        listView.setAdapter(jobAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return this.actionBar.onOptionsItemSelected(item) ? true : super.onOptionsItemSelected(item);
    }



    class LoadDataInBackGround extends AsyncTask<String, String, ArrayList<AllJobModel>> {


        protected ArrayList<AllJobModel> doInBackground(String... strings) {
            array_sort.clear();
            jobList.clear();
            jobMap = JenkinServer.getAllJobs(strings[0], strings[1], strings[2]);
            for (String list : jobMap.keySet()) {
                allJobModel = new AllJobModel(list);
                jobList.add(allJobModel);
                array_sort.add(allJobModel);
            }
            return jobList;
        }

        protected void onPostExecute(ArrayList<AllJobModel> jobList) {
            super.onPostExecute(jobList);
            jobAdapter=new AllJobAdapter(AllJobs.this, jobList);
            listView.setAdapter(jobAdapter);
            jobAdapter.notifyDataSetChanged();
            simpleArcLoader.stop();
            simpleArcLoader.setVisibility(View.GONE);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            simpleArcLoader.start();
            simpleArcLoader.setVisibility(View.VISIBLE);
        }
    }
}