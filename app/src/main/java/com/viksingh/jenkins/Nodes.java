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
import com.viksingh.jenkins.Adapter.NodeAdapter;
import com.viksingh.jenkins.Model.NodeModel;
import com.viksingh.jenkins.Utils.JenkinServer;

import java.util.ArrayList;
import java.util.prefs.NodeChangeListener;

public class Nodes extends AppCompatActivity implements NodeAdapter.OnItemClickListner {

    public static ArrayList<String> computerName;
    private static ArrayList<NodeModel> data;
    private NodeAdapter adapter;
    private String jenkinsUrl;
    private RecyclerView.LayoutManager layoutManager;
    private String password;
    private RecyclerView recyclerView;
    private SimpleArcLoader simpleArcLoader;
    private Toolbar toolbar;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nodes);
        computerName = new ArrayList<>();
        data=new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Nodes");
        toolbar.setNavigationIcon(R.drawable.back_to_home_button);
        simpleArcLoader = (SimpleArcLoader)findViewById(R.id.loader);
        layoutManager = (RecyclerView.LayoutManager)new LinearLayoutManager(this);
        Intent intent = getIntent();
        jenkinsUrl = intent.getStringExtra("jenkinsurl");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        new LoadNodes().execute(jenkinsUrl, username, password);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                finish();
            }
        });

    }

    public void onItemClick(int position) {
        Intent intent = new Intent(this, NodeDashBoard.class);
        intent.putExtra("name", data.get(position).getName());
        intent.putExtra("jenkinsurl", this.jenkinsUrl);
        intent.putExtra("username", this.username);
        intent.putExtra("password", this.password);
        startActivity(intent);
    }
    class LoadNodes extends AsyncTask<String, String, ArrayList<NodeModel>> {
        protected ArrayList<NodeModel> doInBackground(String... strings) {
            computerName = JenkinServer.getComputerDeatail(strings[0], strings[1], strings[2]);
            for (int i = 0; i < Nodes.computerName.size(); i++)
                data.add(new NodeModel(Nodes.computerName.get(i)));
            return data;
        }

        protected void onPostExecute(ArrayList<NodeModel> nodeModelList) {
            super.onPostExecute(nodeModelList);
            Nodes nodes = Nodes.this;
            adapter=new NodeAdapter(nodes, nodeModelList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListner(Nodes.this);
            adapter.notifyDataSetChanged();
            simpleArcLoader.stop();
            simpleArcLoader.setVisibility(View.GONE);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Nodes.this.simpleArcLoader.start();
            Nodes.this.simpleArcLoader.setVisibility(View.VISIBLE);
        }
    }
}