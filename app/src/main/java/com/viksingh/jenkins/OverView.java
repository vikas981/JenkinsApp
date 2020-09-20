package com.viksingh.jenkins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.leo.simplearcloader.SimpleArcLoader;
import com.offbytwo.jenkins.model.Build;
import com.viksingh.jenkins.Adapter.JobHistoryAdapter;
import com.viksingh.jenkins.Model.JobHistoryModel;
import com.viksingh.jenkins.Utils.JenkinServer;
import com.viksingh.jenkins.Utils.TextCompUncomp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OverView extends AppCompatActivity implements JobHistoryAdapter.OnItemClickListner  {
    public static String consoleOutput;
    private JobHistoryAdapter adapter;
    private String jenkinsUrl;
    private List<Build> job;
    ArrayList<JobHistoryModel> jobHistory;
    private String jobname;
    private RecyclerView.LayoutManager layoutManager;
    private String password;
    private RecyclerView recyclerView;
    private SimpleArcLoader simpleArcLoader;
    private Toolbar toolbar;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);
        this.simpleArcLoader = (SimpleArcLoader)findViewById(R.id.loader);
        this.recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = (RecyclerView.LayoutManager)new LinearLayoutManager(this);
        Intent intent = getIntent();
        this.jobname = intent.getStringExtra("jobName");
        this.jenkinsUrl = intent.getStringExtra("jenkinsurl");
        this.userName = intent.getStringExtra("username");
        this.password = intent.getStringExtra("password");
        this.toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.toolbar.setTitle(this.jobname);
        this.toolbar.setNavigationIcon(R.drawable.back_to_home_button);
        new LoadJobHistory().execute(jenkinsUrl,userName,password,jobname);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                OverView.this.finish();
            }
        });
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(this, JobLog.class);
        consoleOutput = TextCompUncomp.compress(jobHistory.get(position).getConsoleOutputText(), "UTF-8");
        startActivity(intent);
    }

    class LoadJobHistory extends AsyncTask<String, String, ArrayList<JobHistoryModel>> {

        protected ArrayList<JobHistoryModel> doInBackground(String... strings) {
            jobHistory = new ArrayList<>();
            if(jobHistory.isEmpty()) {
            job = new ArrayList<>();
            job = JenkinServer.getJobHistory(strings[0], strings[1], strings[2], strings[3]);
                if (job.size() > 10) {
                    for (int i = 0; i < 10; i++) {
                        try {
                            jobHistory.add(new JobHistoryModel(job.get(i).details().getConsoleOutputText(),
                                    job.get(i).details().getDisplayName(), job.get(i).details().getDuration(),
                                    job.get(i).details().getDisplayName(), job.get(i).details().getNumber(), job.get(i).details().getResult().name().toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (int i = 0; i < job.size(); i++) {
                        try {
                            jobHistory.add(new JobHistoryModel(job.get(i).details().getConsoleOutputText(),
                                    job.get(i).details().getDisplayName(), job.get(i).details().getDuration(),
                                    job.get(i).details().getDisplayName(), job.get(i).details().getNumber(), job.get(i).details().getResult().name().toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
              }else {
                  return jobHistory;
            }
            return jobHistory;
        }

            protected void onPostExecute (ArrayList<JobHistoryModel> jobHistoryModelList) {
                super.onPostExecute(jobHistoryModelList);
                adapter = new JobHistoryAdapter(OverView.this, jobHistoryModelList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListner(OverView.this);
                adapter.notifyDataSetChanged();
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
            }

            protected void onPreExecute(){
                super.onPreExecute();
                OverView.this.simpleArcLoader.start();
                OverView.this.simpleArcLoader.setVisibility(View.VISIBLE);
            }
        }
}