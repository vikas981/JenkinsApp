package com.viksingh.jenkins;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.viksingh.jenkins.Utils.TextCompUncomp;

public class BuildLog extends AppCompatActivity {

    private ScrollView mScrollView;
    private TextView mTextStatus;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_log);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Logs");
        toolbar.setNavigationIcon(R.drawable.back_to_home_button);
        mTextStatus = (TextView)findViewById(R.id.TEXT_STATUS_ID);
        mScrollView = (ScrollView)findViewById(R.id.SCROLLER_ID);
        getIntent();
        mTextStatus.setText(TextCompUncomp.decompress(BuildStatus.console, "UTF-8"));
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
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