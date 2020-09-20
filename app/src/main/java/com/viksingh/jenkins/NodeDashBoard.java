package com.viksingh.jenkins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.leo.simplearcloader.SimpleArcLoader;
import com.viksingh.jenkins.Utils.JenkinServer;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;

public class NodeDashBoard extends AppCompatActivity {
    private TextView avaliableRAM;
    private TextView avaliableSWAP;
    private TextView diskPath;
    private TextView diskSpace;
    private TextView executors;
    private BarChart mBarChart;
    private TextView nodeName;
    private TextView osName;
    private ScrollView scrollView;
    private SimpleArcLoader simpleArcLoader;
    private TextView status;
    private TextView tempPath;
    private TextView tempSpace;
    private Toolbar toolbar;
    private TextView totalRAM;
    private TextView totalSwap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_dash_board);
        nodeName = (TextView)findViewById(R.id.nodeName);
        osName = (TextView)findViewById(R.id.osName);
        status = (TextView)findViewById(R.id.status);
        executors = (TextView)findViewById(R.id.executors);
        avaliableRAM = (TextView)findViewById(R.id.availableRam);
        totalRAM = (TextView)findViewById(R.id.totalRam);
        avaliableSWAP = (TextView)findViewById(R.id.availableSwap);
        totalSwap = (TextView)findViewById(R.id.taoalSwap);
        tempSpace = (TextView)findViewById(R.id.temporarySpace);
        tempPath = (TextView)findViewById(R.id.temporaryPath);
        diskSpace = (TextView)findViewById(R.id.diskSpace);
        diskPath = (TextView)findViewById(R.id.diskPath);
        simpleArcLoader = (SimpleArcLoader)findViewById(R.id.loader);
        scrollView = (ScrollView)findViewById(R.id.scrollStats);
        mBarChart = (BarChart)findViewById(R.id.mBarChart);
        Intent intent = getIntent();
        nodeName.setText(intent.getStringExtra("name"));
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra("name"));
        toolbar.setNavigationIcon(R.drawable.back_to_home_button);
        new LoadData().execute(intent.getStringExtra("jenkinsurl"), intent.getStringExtra("username"), intent.getStringExtra("password"),intent.getStringExtra("name"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
                finish();
            }
        });
    }
    class LoadData extends AsyncTask<String, String, Map<String, String>> {
        protected Map<String, String> doInBackground(String... strings) {
            String jsonData = null;
            String executors= null;
            HashMap<String, String> hashMap = new HashMap<>();
            jsonData = JenkinServer.computerAllDetails(strings[0], strings[1], strings[2], strings[3]);
            executors=JenkinServer.getExecutors(strings[0], strings[1], strings[2], strings[3]);
            if (jsonData == null) {
                return hashMap;
            } else {
                try {
                    String availablePhysicalMemory;
                    String availableSwapSpace;
                    String totalSwapSpace;
                    String totalPhysicalMemory;
                    JSONObject object = new JSONObject(jsonData);
                    JSONObject jsonObject = object.getJSONObject("hudson.node_monitors.SwapSpaceMonitor");

                    if (jsonObject.isNull("availablePhysicalMemory")) {
                        availablePhysicalMemory = "NA";
                    } else {
                        availablePhysicalMemory = NodeDashBoard.humanReadableByteCountBin(Long.parseLong(jsonObject.getString("availablePhysicalMemory")));
                    }
                    if (jsonObject.isNull("totalSwapSpace")) {
                        totalSwapSpace = "NA";
                    } else {
                        totalSwapSpace = NodeDashBoard.humanReadableByteCountBin(Long.parseLong(jsonObject.getString("totalSwapSpace")));
                    }
                    if (jsonObject.isNull("totalPhysicalMemory")) {
                        totalPhysicalMemory = "NA";
                    } else {
                        totalPhysicalMemory = NodeDashBoard.humanReadableByteCountBin(Long.parseLong(jsonObject.getString("totalPhysicalMemory")));
                    }
                    if (jsonObject.isNull("availableSwapSpace")) {
                        availableSwapSpace = "NA";
                    } else {
                        availableSwapSpace = NodeDashBoard.humanReadableByteCountBin(Long.parseLong(jsonObject.getString("availableSwapSpace")));
                    }
                    JSONObject json = new JSONObject(jsonData);
                    try {
                        String OS = json.get("hudson.node_monitors.ArchitectureMonitor").toString();
                        String path = json.getJSONObject("hudson.node_monitors.TemporarySpaceMonitor").getString("path");
                        String diskpath = json.getJSONObject("hudson.node_monitors.DiskSpaceMonitor").getString("path");
                        String diskspace = NodeDashBoard.humanReadableByteCountBin(Long.parseLong(json.getJSONObject("hudson.node_monitors.DiskSpaceMonitor").getString("size")));
                        String tempsize = NodeDashBoard.humanReadableByteCountBin(Long.parseLong(json.getJSONObject("hudson.node_monitors.TemporarySpaceMonitor").getString("size")));
                        hashMap.put("availablePhysicalMemory", availablePhysicalMemory);
                        hashMap.put("availableSwapSpace", availableSwapSpace);
                        hashMap.put("totalPhysicalMemory", totalPhysicalMemory);
                        hashMap.put("totalSwapSpace", totalSwapSpace);
                        hashMap.put("OS", OS);
                        hashMap.put("path", path);
                        hashMap.put("diskpath", diskpath);
                        hashMap.put("diskspace", diskspace);
                        hashMap.put("tempsize", tempsize);
                        hashMap.put("executors",executors);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return hashMap;
        }

        protected void onPostExecute(Map<String, String> map) {
            super.onPostExecute(map);
            if (!map.isEmpty()) {
               osName.setText(map.get("OS"));
               avaliableRAM.setText(map.get("availableSwapSpace"));
               totalRAM.setText(map.get("totalPhysicalMemory"));
               avaliableRAM.setText(map.get("availablePhysicalMemory"));
               avaliableSWAP.setText(map.get("availableSwapSpace"));
               totalSwap.setText(map.get("totalSwapSpace"));
               diskSpace.setText(map.get("diskspace"));
               tempPath.setText(map.get("path"));
               diskPath.setText(map.get("diskpath"));
               tempSpace.setText(map.get("tempsize"));
               status.setText("Online");
               executors.setText(map.get("executors"));

                String[] arrayOfString2 = avaliableRAM.getText().toString().split("\\.");
                String[] arrayOfString3 = totalRAM.getText().toString().split("\\.");
                String[] arrayOfString1 = avaliableSWAP.getText().toString().split("\\.");
                String[] arrayOfString4 = totalSwap.getText().toString().split("\\.");

                NodeDashBoard.this.mBarChart.addBar(new BarModel("Total RAM", Integer.parseInt(arrayOfString3[0]), Color.parseColor("#FFA500")));
                NodeDashBoard.this.mBarChart.addBar(new BarModel("Available RAM", Integer.parseInt(arrayOfString2[0]), Color.parseColor("#FF0000")));
                NodeDashBoard.this.mBarChart.addBar(new BarModel("Total SWAP", Integer.parseInt(arrayOfString4[0]), Color.parseColor("#008000")));
                NodeDashBoard.this.mBarChart.addBar(new BarModel("Available SWAP", Integer.parseInt(arrayOfString1[0]), Color.parseColor("#00BFFF")));
                NodeDashBoard.this.simpleArcLoader.stop();
                NodeDashBoard.this.simpleArcLoader.setVisibility(View.GONE);
            } else {
                mBarChart.addBar(new BarModel("Total RAM", 0.0F, Color.parseColor("#FFA500")));
                mBarChart.addBar(new BarModel("Available RAM", 0.0F, Color.parseColor("#FF0000")));
                mBarChart.addBar(new BarModel("Total SWAP", 0.0F, Color.parseColor("#008000")));
                mBarChart.addBar(new BarModel("Available SWAP", 0.0F, Color.parseColor("#00BFFF")));
                status.setText("Offline");
                osName.setText("NA");
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            NodeDashBoard.this.simpleArcLoader.start();
            NodeDashBoard.this.simpleArcLoader.setVisibility(View.VISIBLE);
        }
    }
    public static String humanReadableByteCountBin(long paramLong) {
        long l1;
        if (paramLong == Long.MIN_VALUE) {
            l1 = Long.MAX_VALUE;
        } else {
            l1 = Math.abs(paramLong);
        }
        if (l1 < 1024L) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(paramLong);
            stringBuilder.append(" B");
            return stringBuilder.toString();
        }
        long l2 = l1;
        StringCharacterIterator stringCharacterIterator = new StringCharacterIterator("KMGTPE");
        for (byte b = 40; b >= 0 && l1 > 1152865209611504844L >> b; b -= 10) {
            l2 >>= 10L;
            stringCharacterIterator.next();
        }
        return String.format("%.1f %ciB", new Object[] { Double.valueOf((l2 * Long.signum(paramLong)) / 1024.0D), Character.valueOf(stringCharacterIterator.current()) });
    }
}