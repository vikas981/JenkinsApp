package com.viksingh.jenkins;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButton {
    private CardView cardView;
    private ConstraintLayout constraintLayout;
    Animation fade_in;
    private ProgressBar progressBar;
    private TextView textView;

    ProgressButton(Context paramContext, View paramView) {
        this.fade_in = AnimationUtils.loadAnimation(paramContext, R.anim.fade_in);
        this.cardView = (CardView)paramView.findViewById(R.id.cp_cardview);
        this.constraintLayout = (ConstraintLayout)paramView.findViewById(R.id.constraintlayout);
        this.progressBar = (ProgressBar)paramView.findViewById(R.id.progressBar);
        this.textView = (TextView)paramView.findViewById(R.id.textView);
    }

    void buttonActivated() {
        this.progressBar.setAnimation(this.fade_in);
        this.progressBar.setVisibility(View.VISIBLE);
        this.textView.setAnimation(this.fade_in);
        this.textView.setText("Please Wait");
    }

    void buttonFinised() {
        this.constraintLayout.setBackgroundColor(this.cardView.getResources().getColor(R.color.green));
        this.textView.setText("Validating User..");
        this.progressBar.setVisibility(View.GONE);
    }

    void userNotValidated() {
        this.constraintLayout.setBackgroundColor(this.cardView.getResources().getColor(R.color.red));
        this.textView.setText("Try Again");
        this.progressBar.setVisibility(View.GONE);
    }
}
