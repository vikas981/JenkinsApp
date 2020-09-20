package com.viksingh.jenkins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.leo.simplearcloader.SimpleArcLoader;
import com.offbytwo.jenkins.model.Build;
import com.viksingh.jenkins.Adapter.JobBuildAdapter;
import com.viksingh.jenkins.Model.JobBuildModel;
import com.viksingh.jenkins.Utils.JenkinServer;
import com.viksingh.jenkins.Utils.TextCompUncomp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildStatus extends AppCompatActivity implements JobBuildAdapter.OnItemClickListner  {

    public static String console;
    private JobBuildAdapter adapter;
    private String jenkinsurl;
    private List<Build> job;
    ArrayList<JobBuildModel> jobStatus;
    private RecyclerView.LayoutManager layoutManager;
    private String password;
    private RecyclerView recyclerView;
    private SimpleArcLoader simpleArcLoader;
    private Toolbar toolbar;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_status);

        simpleArcLoader = (SimpleArcLoader)findViewById(R.id.loader);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = (RecyclerView.LayoutManager)new LinearLayoutManager((Context)this);
        Intent intent = getIntent();
        String job = intent.getStringExtra("jobname");
        jenkinsurl = intent.getStringExtra("jenkinsurl");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(job);
        toolbar.setNavigationIcon(R.drawable.back_to_home_button);
        new JobStatus().execute(jenkinsurl, username, password,job);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                BuildStatus.this.finish();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, BuildLog.class);
        console = TextCompUncomp.compress(jobStatus.get(position).getConsoleOuput(), "UTF-8");
        startActivity(intent);

    }

    class JobStatus extends AsyncTask<String, String, ArrayList<JobBuildModel>> {
        protected ArrayList<JobBuildModel> doInBackground(String... strings) {
            jobStatus = new ArrayList<>();
            job= JenkinServer.getJobStatus(strings[0], strings[1], strings[2], strings[3]);
            if (BuildStatus.this.job.size() > 10) {
                for (int i = 0; i < 1; i++) {
                    try {
                        if (job.get(i).details().isBuilding()) {
                            jobStatus.add(new JobBuildModel(job.get(i).details().getNumber(), job.get(i).details().getConsoleOutputText()));
                        } else {
                            jobStatus.add(new JobBuildModel(job.get(i).details().getNumber(), job.get(i).details().getConsoleOutputText(), job.get(i).details().getResult().name().toString()));
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }}else {
                try {
                    jobStatus.add(new JobBuildModel(job.get(0).details().getNumber(),job.get(0).details().getConsoleOutputText(),job.get(0).details().getResult().name().toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return jobStatus;
        }

        protected void onPostExecute(ArrayList<JobBuildModel> jobBuildModelArrayList) {
            super.onPostExecute(jobBuildModelArrayList);
            BuildStatus buildStatus = BuildStatus.this;
            adapter=new JobBuildAdapter(BuildStatus.this, jobBuildModelArrayList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListner(BuildStatus.this);
            adapter.notifyDataSetChanged();
            simpleArcLoader.stop();
            simpleArcLoader.setVisibility(View.GONE);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            BuildStatus.this.simpleArcLoader.start();
            BuildStatus.this.simpleArcLoader.setVisibility(View.VISIBLE);
        }
    }
}