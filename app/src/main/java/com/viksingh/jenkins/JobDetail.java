package com.viksingh.jenkins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.viksingh.jenkins.Utils.JenkinServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.viksingh.jenkins.AllJobs.array_sort;

public class JobDetail extends AppCompatActivity {

    private Button build;
    private ImageView exploreIcon;
    private String jenkinsurl;
    private Map<String, String> jobDetails;
    private List<String> keySet = new ArrayList<>();
    private LinearLayout lin;
    private String password;
    private int position;
    private EditText rowEditView;
    private SimpleArcLoader simpleArcLoader;
    private TextView textView;
    private Toolbar toolbar;
    private String username;
    private List<String> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        jobDetails = new HashMap<>();
        values = new ArrayList<>();
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        jenkinsurl = intent.getStringExtra("jenkinsurl");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        textView = (TextView) findViewById(R.id.text);
        build = (Button) findViewById(R.id.build);
        simpleArcLoader = (SimpleArcLoader) findViewById(R.id.loader);
        exploreIcon = (ImageView) findViewById(R.id.explore_icon);

        this.textView.setText(array_sort.get(position).getJob());
        this.toolbar = findViewById(R.id.toolbar);
        this.toolbar.setTitle("Job Details");

        this.toolbar.setNavigationIcon(R.drawable.back_to_home_button);
        new LoadAllData().execute(jenkinsurl, username, password, array_sort.get(this.position).getJob());

        onCliclBackButton(toolbar);

        this.exploreIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                Intent intent = new Intent(JobDetail.this, OverView.class);
                intent.putExtra("jobName", array_sort.get(JobDetail.this.position).getJob());
                intent.putExtra("jenkinsurl", JobDetail.this.jenkinsurl);
                intent.putExtra("username", JobDetail.this.username);
                intent.putExtra("password", JobDetail.this.password);
                JobDetail.this.startActivity(intent);
            }
        });
    }
    public void onCliclBackButton(Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                JobDetail.this.finish();
            }
        });
    }

    public class LoadAllData extends AsyncTask<String, String, Map<String, String>> {
        protected Map<String, String> doInBackground(String... strings) {
            jobDetails= JenkinServer.getParameter(strings[0], strings[1], strings[2], strings[3]);
            return jobDetails;
        }

        protected void onPostExecute(Map<String, String> paramMap) {
            super.onPostExecute(paramMap);
            final HashMap<Object, Object> jobData = new HashMap<>();
            if (!paramMap.isEmpty()) {
                lin=findViewById(R.id.myLinear);
                for (String str : paramMap.keySet())
                    keySet.add(str);
                for (String str : paramMap.values())
                    values.add(str);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);
                final int N =values.size();
                final EditText[] editViews = new EditText[N];
                final TextView[] textViews = new TextView[N];
                for (int i = 0; i < N; i++) {
                    rowEditView = new EditText(JobDetail.this);
                    rowEditView.setLayoutParams(layoutParams);
                    TextView textView = new TextView(JobDetail.this);
                    rowEditView.setBackgroundResource(R.drawable.edittext_border);
                    rowEditView.setText(values.get(i));
                    textView.setText(JobDetail.this.keySet.get(i));
                    lin.addView(textView);
                    textView.setTextColor(JobDetail.this.getColor(R.color.black));
                    textView.setTextSize(16);
                    lin.addView(rowEditView);
                    editViews[i] = rowEditView;
                    textViews[i] = textView;
                }
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                build.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        simpleArcLoader.start();
                        simpleArcLoader.setVisibility(View.VISIBLE);
                        int i = N;
                        String[] textView = new String[i];
                        String[] editView = new String[i];
                        for (i = 0; i < N; i++) {
                            textView[i] = editViews[i].getText().toString();
                            editView[i] = textViews[i].getText().toString();
                        }
                        for (i = 0; i < textView.length; i++) {
                            System.out.println(editView[i]+"<--->"+ textView[i]);
                            jobData.put(editView[i], textView[i]);
                        }
                        build.setText("Build Started");
                        build.setBackgroundResource(R.color.green);
                        new BuildJob().execute(jenkinsurl,username,password,array_sort.get(position).getJob(),jobData);
                    }
                });
            } else if (paramMap.isEmpty()) {
                JobDetail.this.simpleArcLoader.stop();
                JobDetail.this.simpleArcLoader.setVisibility(View.GONE);
                JobDetail.this.build.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View param2View) {
                        simpleArcLoader.start();
                        simpleArcLoader.setVisibility(View.VISIBLE);
                        build.setText("Build Started");
                        build.setBackgroundResource(R.color.green);
                        new BuildWithoutParam().execute(jenkinsurl,username,password,array_sort.get(position).getJob());
                    }
                });
            } else {
                CFAlertDialog.Builder builder = (new CFAlertDialog.Builder(JobDetail.this)).setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT).setTitle("Please Connect VPN");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to fetch data from ");
                stringBuilder.append(JenkinServer.getDomainName(JobDetail.this.jenkinsurl));
                stringBuilder.append(" without VPN");
                builder.setMessage(stringBuilder.toString()).addButton("OK", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        Toast.makeText(JobDetail.this, "OK tapped", Toast.LENGTH_SHORT).show();
                        param2DialogInterface.dismiss();
                    }
                }).show();
                JobDetail.this.simpleArcLoader.stop();
                JobDetail.this.simpleArcLoader.setVisibility(View.GONE);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            JobDetail.this.simpleArcLoader.start();
            JobDetail.this.simpleArcLoader.setVisibility(View.VISIBLE);
        }
    }

    class BuildWithoutParam extends AsyncTask<String, String, String> {
        protected String doInBackground(String... strings) {
            String str = strings[0];
            JenkinServer.buildJobWithoutParam(strings[0], strings[1], strings[2], strings[3]);
            return str;
        }

        protected void onPostExecute(String jobName) {
            super.onPostExecute(jobName);
            Intent intent = new Intent(JobDetail.this, BuildStatus.class);
            intent.putExtra("jobname", jobName);
            intent.putExtra("jenkinsurl", JobDetail.this.jenkinsurl);
            intent.putExtra("username", JobDetail.this.username);
            intent.putExtra("password", JobDetail.this.password);
            startActivity(intent);
            simpleArcLoader.stop();
            simpleArcLoader.setVisibility(View.GONE);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            JobDetail.this.simpleArcLoader.start();
            JobDetail.this.simpleArcLoader.setVisibility(View.VISIBLE);
        }
    }
    class BuildJob extends AsyncTask<Object, String, String> {
        protected String doInBackground(Object... objects) {
            String str = (String)objects[3];
            JenkinServer.buildJob((String)objects[0], (String)objects[1], (String)objects[2], (String)objects[3], (Map)objects[4]);
            return str;
        }

        protected void onPostExecute(String jobName) {
            super.onPostExecute(jobName);
            Intent intent = new Intent(JobDetail.this, BuildStatus.class);
            intent.putExtra("jobname", jobName);
            intent.putExtra("jenkinsurl", JobDetail.this.jenkinsurl);
            intent.putExtra("username", JobDetail.this.username);
            intent.putExtra("password", JobDetail.this.password);
            startActivity(intent);
            simpleArcLoader.stop();
            simpleArcLoader.setVisibility(View.GONE);
        }

        protected void onPreExecute() {
            simpleArcLoader.start();
            simpleArcLoader.setVisibility(View.VISIBLE);
        }
    }
}