package com.example.esraoncu.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;


import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 20;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems=(ListView)findViewById(R.id.lvItems);
        readItems();

        itemsAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();

    }
    public void onAddItem(View v){
        EditText etNewItem=(EditText) findViewById(R.id.etNewItem);
        String itemText=etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener(){

        lvItems.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View item,int position, long id){
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView <?> parent,View item,int position,long id){
                String todoItem=itemsAdapter.getItem(position);
                Intent editIntent=new Intent(MainActivity.this,EditItemActivity.class);
                editIntent.putExtra("task",todoItem);
                startActivityForResult(editIntent,REQUEST_CODE);
                pos=position;
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String todoItem = data.getExtras().getString("newTask");
             updateList(pos,todoItem);

        }
    }

    public void updateList(int position, String newTask){
        items.set(position, newTask);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }


    private void readItems(){
        File fileDir=getFilesDir();
        File todoFile=new File(fileDir,"todo.txt");
        try{
           items=new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch (IOException  e){
            items=new ArrayList<String>();
            e.printStackTrace();
       }
    }
    private void writeItems(){
        File fileDir=getFilesDir();
        File todoFile=new File(getFilesDir(), "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }
        catch (IOException e){e.printStackTrace();}
    }

}
