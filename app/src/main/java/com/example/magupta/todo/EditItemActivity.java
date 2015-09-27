package com.example.magupta.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import helpers.DBHelper;
import models.Task;

public class EditItemActivity extends Activity {
    private int position;
    DBHelper dbHelper;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        dbHelper = new DBHelper(getApplicationContext());
        position = getIntent().getIntExtra("position", 0);
        int task_id = getIntent().getIntExtra("task_id",-1);

        task = dbHelper.getData(task_id);
        String itemValue = getIntent().getStringExtra("text");
        EditText item = (EditText) findViewById(R.id.editTask);

        item.setText(itemValue, TextView.BufferType.EDITABLE);
        item.setSelection(itemValue.length());

        if(item.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onSubmitSave(View view) {
        EditText etName = (EditText) findViewById(R.id.editTask);
        Intent data = new Intent();

        task.setName(etName.getText().toString());
        dbHelper.updateTask(task);

        data.putExtra("itemValue", etName.getText().toString());
        data.putExtra("position", this.position);
        setResult(RESULT_OK, data);
        finish();
    }
}
