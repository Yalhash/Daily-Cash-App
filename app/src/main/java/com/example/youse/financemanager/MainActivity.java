package com.example.youse.financemanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast; //for debugging

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    FloatingActionButton addButt;

    ListView cashList;
    financeModule[] cashArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButt = findViewById(R.id.AddButton);


        //load array into list on startup
        TinyDB cashListDB = new TinyDB(MainActivity.this);
        ArrayList<financeModule> cashListItems = new ArrayList<financeModule>();
        //loading elements of the finance module
        ArrayList<Object> objList = cashListDB.getListObject("finances", financeModule.class);
        cashArray = new financeModule[objList.size()];
        for (int i = 0; i < objList.size(); i++){
            cashArray[i] = (financeModule) objList.get(i);
        }

        /*-------------------------------------Setting up List---------------------------------------------------*/
        final financeAdapter cashListAdapter = new financeAdapter(MainActivity.this, cashArray);
        cashList = (ListView) findViewById(R.id.cashFlowList);
        cashList.setAdapter(cashListAdapter);
        registerForContextMenu(cashList);
        updateSum();
        //on click go to view activity
        cashList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //send finance
                Intent intent = new Intent(MainActivity.this, viewActivity.class);
                intent.putExtra("finance", cashArray[position]);
                startActivity(intent);
            }
        });
        /*-----------------------------------Add button listener------------------------------------------------*/
        addButt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("isEdit", false);
                startActivityForResult(intent, 1);
            }
        });
    }

    /*--------------------------------------------Context Menu Handlers-------------------------------------------*/
    //((AdapterView.AdapterContextMenuInfo)menuInfo).position
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId()==R.id.cashFlowList) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_menu, menu);
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //info.position for index
        switch(item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("position", info.position);
                intent.putExtra("finance", cashArray[info.position]);
                startActivityForResult(intent, 2);
                return true;
            case R.id.delete:
                AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                //adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + cashArray[info.position].getFinanceName() +  "?");
                final int positionToRemove = info.position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeList(positionToRemove);
                    }});
                adb.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    /*------------------------------------Receiving data from edit activity----------------------*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    financeModule newFinance = (financeModule) data.getSerializableExtra("editFinance");
                    appendList(newFinance);
                }
                break;
            }
            case(2) : {
                if (resultCode == Activity.RESULT_OK){
                    financeModule editFinance = (financeModule) data.getSerializableExtra("editFinance");
                    int position = data.getIntExtra("position", 0);
                    cashArray[position] = editFinance;
                    //update list
                    financeAdapter cashListAdapter = new financeAdapter(MainActivity.this, cashArray);
                    cashList.setAdapter(cashListAdapter);
                    updateSum();
                }
            }
        }
    }

    /*----------------------------------Saving data when the app closes--------------------------------*/
    @Override
    protected void onStop() {
        super.onStop();
        TinyDB saveDB = new TinyDB(MainActivity.this);
        ArrayList<Object> saveArray = new ArrayList<Object>(Arrays.asList(cashArray));
        saveDB.putListObject("finances", saveArray);
    }

    /*--------------------------------updating the sum at the top--------------------------------------*/
    public void updateSum(){
        Integer financeSum = 0;
        for(financeModule a : cashArray){
            if(a.getPosNeg()) {
                financeSum += a.getValue();
            }else{
                financeSum -= a.getValue();
            }
        }
        ImageView posNegImage = findViewById(R.id.posNegImage);
        if(financeSum < 0){
            financeSum *= -1;
            posNegImage.setImageResource(R.drawable.ic_down);
        }else if(financeSum > 0){
            posNegImage.setImageResource(R.drawable.ic_up);
        }else{
            posNegImage.setImageResource(R.drawable.ic_zero);
        }
        String sumString = "$" + financeSum.toString();
        TextView sumText = findViewById(R.id.TotalText);
        sumText.setText(sumString);
    }

    /*-------------------------------------List Handlers---------------------------------------------*/

    public void appendList(financeModule newListElement){
        cashArray = Arrays.copyOf(cashArray, cashArray.length+1);
        cashArray[cashArray.length-1] = newListElement;
        financeAdapter cashListAdapter = new financeAdapter(MainActivity.this, cashArray);
        cashList.setAdapter(cashListAdapter);
        updateSum();
    }

    public void removeList(Integer index){
        //bad index handler
        if (index >= cashArray.length || index < 0){
            return;
        }
        financeModule[] tempArr = new financeModule[cashArray.length-1];
        Integer j = 0;
        for(int i = 0; i < cashArray.length; i++){
            if(i != index){
                tempArr[j] = cashArray[i];
                j++;
            }
        }
        cashArray = tempArr;
        financeAdapter cashListAdapter = new financeAdapter(MainActivity.this, cashArray);
        cashList.setAdapter(cashListAdapter);
        updateSum();
    }


}
