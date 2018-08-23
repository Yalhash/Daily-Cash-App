package com.example.youse.financemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class viewActivity extends AppCompatActivity {
    TextView nameText;
    TextView amountText;
    TextView notesBodyText;
    ImageView posNegImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        //set up all of the textViews
        nameText = findViewById(R.id.nameText);
        amountText = findViewById(R.id.amountText);
        notesBodyText = findViewById(R.id.notesBodyText);
        posNegImage = findViewById(R.id.posNegImage);
        //handle the finance thing, set the text of all of the textViews
        Bundle bundle = getIntent().getExtras();
        financeModule viewFinance = (financeModule) bundle.getSerializable("finance");
        String nameString = "Name: " + viewFinance.getFinanceName();
        String amountString = "Value: " + viewFinance.getValueText();
        String notesString = viewFinance.getNotes();
        nameText.setText(nameString);
        amountText.setText(amountString);
        notesBodyText.setText(notesString);
        if (viewFinance.getValue() == 0){
            posNegImage.setImageResource(R.drawable.ic_zero);
        }else if(viewFinance.getPosNeg()){
            posNegImage.setImageResource(R.drawable.ic_up);
        }else{
            posNegImage.setImageResource(R.drawable.ic_down);
        }

    }

    public void okClick(View view){
        finish();
    }

}

