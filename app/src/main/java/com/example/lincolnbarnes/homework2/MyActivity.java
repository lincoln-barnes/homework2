package com.example.lincolnbarnes.homework2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MyActivity extends Activity {

    ArrayList<String> tags;
    EditText tv;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        tags = new ArrayList<String>();
        tv = (EditText) findViewById(R.id.textView);
        final ListView listView = (ListView) findViewById(R.id.listView1);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, tags);

        listView.setAdapter(adapter);

    }

    /*public void onSaveClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a state")
                .setItems(statesArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!states.contains(statesArray[which])) {
                            states.add(statesArray[which]);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        builder.create().show();

    }*/
}
