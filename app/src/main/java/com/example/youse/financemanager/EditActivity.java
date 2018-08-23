package com.example.youse.financemanager;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    EditText nameEdit;
    EditText amountEdit;
    EditText notesEdit;
    ImageButton posNegBut;
    financeModule editFinance;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        //Declare stuff
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        amountEdit = (EditText) findViewById(R.id.amountEdit);
        notesEdit = (EditText) findViewById(R.id.notesEdit);
        posNegBut = (ImageButton) findViewById(R.id.posNegButton);

        //Handle values passed in
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //Did we come from an edit or a new CashFlow
            if(bundle.getBoolean("isEdit")){
                //import the other values and fill the boxes w/ them
                editFinance = (financeModule) bundle.getSerializable("finance");
                nameEdit.setText(editFinance.getFinanceName());
                amountEdit.setText(editFinance.getValue().toString());
                notesEdit.setText(editFinance.getNotes());
                if(!editFinance.getPosNeg()){
                    posNegBut.setImageResource(R.drawable.ic_down);
                }
            }else{
                //When coming from the + FAB
                editFinance = new financeModule("", true, 0, "");
            }
            position = bundle.getInt("position", -1);
        }
    }

    public void posNegClick(View v){
        if (editFinance.getPosNeg()){
            editFinance.setPosNeg(false);
            posNegBut.setImageResource(R.drawable.ic_down);
        }else{
            editFinance.setPosNeg(true);
            posNegBut.setImageResource(R.drawable.ic_up);
        }
    }


    public void cancelClick(View v){
        finish();
    }

    public void saveClick(View v){
        if(nameEdit.getText().toString().length()< 1 || amountEdit.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Missing Required Field(s)", Toast.LENGTH_SHORT).show();
        }else {
            editFinance.setFinanceName(nameEdit.getText().toString());
            editFinance.setValue(Integer.parseInt(amountEdit.getText().toString()));
            editFinance.setNotes(notesEdit.getText().toString());
            Intent resultIntent = new Intent();
            resultIntent.putExtra("editFinance", editFinance);
            if(position != -1) {
                resultIntent.putExtra("position", position);
            }
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
