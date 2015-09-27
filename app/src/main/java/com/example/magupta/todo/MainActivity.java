package com.example.magupta.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import helpers.DBHelper;
import models.Task;

public class MainActivity extends Activity {
    ArrayList<Task> items;
    ArrayAdapter<Task> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext());

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = dbHelper.getAllTasks();
        itemsAdapter = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos,
                                                   long id) {
                        Task task = items.get(pos);
                        dbHelper.deleteTask(task);
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                        intent.putExtra("text", ((TextView) view).getText());
                        intent.putExtra("position", position);
                        intent.putExtra("task_id", items.get(position).getId());
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        EditText etNewItem  = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (!itemText.isEmpty()) {
            Task task = new Task(itemText);
            int id = dbHelper.createTask(task);
            task.setId(id);
            itemsAdapter.add(task);
        }
        etNewItem.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if ( resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String name = data.getExtras().getString("itemValue");
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

            int position = data.getExtras().getInt("position");
            items.get(position).setName(name);
            itemsAdapter.notifyDataSetChanged();
//            writeItems();
        }
    }
}
