package com.viksingh.jenkins;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
    private TextView address;
    private TextView backToHome;
    private TextView dob;
    private TextView email;
    private TextView emailInfo;
    private TextView name;
    private TextView nameTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.name = (TextView)findViewById(R.id.name);
        this.email = (TextView)findViewById(R.id.email);
        this.backToHome = (TextView)findViewById(R.id.backToHome);
        this.emailInfo = (TextView)findViewById(R.id.emailInfo);
        this.dob = (TextView)findViewById(R.id.dob);
        this.nameTextView = (TextView)findViewById(R.id.nameTextView);
        this.address = (TextView)findViewById(R.id.address);
        String username = getIntent().getStringExtra("username").toString();
        this.name.setText(username);
        TextView mail = this.email;
        StringBuilder builder = new StringBuilder();
        builder.append(username);
        builder.append("@altametrics.com");
        mail.setText(builder.toString());
        this.nameTextView.setText(username);
        TextView textView2 = this.emailInfo;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(username);
        stringBuilder.append("@altametrics.com");
        textView2.setText(stringBuilder.toString());
        this.backToHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}