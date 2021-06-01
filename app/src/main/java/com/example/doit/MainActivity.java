package com.example.doit;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.Adapter.ToDoAdapter;
import com.example.doit.Model.ToDoModel;
import com.example.doit.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private ToDoAdapter taskAdapter;
    private List<ToDoModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        //removing topmost action bar
        getSupportActionBar().hide();
        db = new DatabaseHandler(this);
        db.openDatabase();


        //defining recyler view
        RecyclerView taskRecyclerView = findViewById(R.id.tasksRecyclerView);

        //setting the recycler view as linear layout
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //setting adapter to recycling view
        taskAdapter = new ToDoAdapter(db, MainActivity.this);
        taskRecyclerView.setAdapter(taskAdapter);

       taskList = db.getAllTasks();


        taskAdapter.setTasks(taskList);
        Collections.reverse(taskList);
        fab.setOnClickListener(v -> AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        /*now we define a recycler view adapter and a modal class
        for the recycler view to work upon
        model contains the class which will define the structure of individual
        task
         */

    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}