package com.viksingh.jenkins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.viksingh.jenkins.Utils.TextCompUncomp;

public class JobLog extends AppCompatActivity {

    private ScrollView mScrollView;
    private TextView mTextStatus;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_log);

        this.toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.toolbar.setTitle("Logs");
        this.toolbar.setNavigationIcon(R.drawable.back_to_home_button);
        this.mTextStatus = (TextView)findViewById(R.id.TEXT_STATUS_ID);
        this.mScrollView = (ScrollView)findViewById(R.id.SCROLLER_ID);
        getIntent();
        this.mTextStatus.setText(TextCompUncomp.decompress(OverView.consoleOutput, "UTF-8"));
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                JobLog.this.finish();
            }
        });
    }

    private void scrollToBottom() {
        this.mScrollView.post(new Runnable() {
            public void run() {
               mScrollView.smoothScrollTo(0, mTextStatus.getBottom());
            }
        });
    }
}