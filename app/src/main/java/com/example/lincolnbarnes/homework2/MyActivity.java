package com.example.lincolnbarnes.homework2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MyActivity extends Activity
{

    ArrayList<String> tags = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    SharedPreferences sharePreferences;
    SharedPreferences.Editor editor;
    final Context context = this;
    EditText tagBox;
    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        tagBox = (EditText) findViewById(R.id.tagBox);
        searchBox = (EditText) findViewById(R.id.searchBox);

        final ListView listView = (ListView) findViewById(R.id.listView1);

        sharePreferences = getPreferences(MODE_PRIVATE);
        editor = sharePreferences.edit();
        editor.apply();

        tags = new ArrayList<String>(sharePreferences.getAll().keySet());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                loadPreferences(tags.get(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id)
            {
                final CharSequence[] items = {"Share", "Edit", "Delete"};
                final String tag = (tags.get(position));
                String msg = String.format("Share, Edit or Delete the search tagged as \"%s\"", tag);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(msg);

                builder.setItems(items, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String selected = items[which].toString();

                        if(selected.equals("Share"))
                        {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            startActivity(Intent.createChooser(shareIntent, "Share"));
                        }
                        else if(selected.equals("Edit"))
                        {
                            searchBox.setText(sharePreferences.getString(tag, ""));
                            tagBox.setText(tag);
                            tags.remove(tag);
                            adapter.notifyDataSetChanged();
                            editor.remove(tag);
                            editor.apply();
                        }
                        else if(selected.equals("Delete"))
                        {
                            String msg2 = String.format("Are you sure you want to delete \"%s\"", tag);
                            AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(context);

                            deleteBuilder.setTitle(msg2);
                            deleteBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id) {
                                    tags.remove(tag);
                                    adapter.notifyDataSetChanged();
                                    editor.remove(tag);
                                    editor.apply();
                                }
                            });
                            deleteBuilder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                            AlertDialog alertDialog = deleteBuilder.create();
                            alertDialog.show();
                        }
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id){}
                });

                builder.create();
                builder.show();

                return true;
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags);

        listView.setAdapter(adapter);
    }

    public void onSaveClick(View v)
    {
        String tag;
        String search;

        //tagBox = (EditText) findViewById(R.id.tagBox);
        //searchBox = (EditText) findViewById(R.id.searchBox);

        tag = tagBox.getText().toString();
        search = searchBox.getText().toString();

        editor.putString(tag, search);
        editor.commit();


        if(!tags.contains(tag))
        {
            tags.add(tagBox.getText().toString());
            //editor.putString(tag, search);
            //editor.commit();
            tagBox.setText("");
            searchBox.setText("");
        }
        else
        {
            tagBox.setText("");
            searchBox.setText("");
            searchBox.requestFocus();
        }

        //Hides Keyboard
        ((InputMethodManager) getSystemService (
            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
            searchBox.getWindowToken(), 0);

        adapter.notifyDataSetChanged();
    };

    private void loadPreferences(String key)
    {
        String url = "http://www.uta.edu/search/?q=";
        String search;
        SharedPreferences sharePreferences = getPreferences(MODE_PRIVATE);
        String keyWord = sharePreferences.getString(key, "");

        search = String.format("%s%s", url, keyWord);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(search));

        startActivity(i);
    };
}