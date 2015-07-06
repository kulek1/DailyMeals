package com.mk.dailymeals;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mk.dailymeals.database.TodoDbAdapter;
import com.mk.dailymeals.model.TodoTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mirek on 05-07-2015.
 */
public class addMeal extends Activity {
    private EditText etNewTask;
    private ListView lvTodos;
    private TodoDbAdapter todoDbAdapter;
    private Cursor todoCursor;
    private List<TodoTask> tasks;
    private TodoTasksAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        initUiElements();
        todoDbAdapter = new TodoDbAdapter(getApplicationContext());
        listAdapter = new TodoTasksAdapter(this, tasks);

        Button save = (Button) findViewById(R.id.button_save);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeal();
            }
        });
    }

    private void initUiElements() {
        etNewTask = (EditText) findViewById(R.id.etNewTask);
        lvTodos = (ListView) findViewById(R.id.listView);
    }
    private void initListView() {
        fillListViewData();
    }
    private void fillListViewData() {
        todoDbAdapter = new TodoDbAdapter(getApplicationContext());
        todoDbAdapter.open();
        getAllTasks();
        listAdapter = new TodoTasksAdapter(this, tasks);
        lvTodos.setAdapter(listAdapter);
    }

    private void getAllTasks() {
        tasks = new ArrayList<TodoTask>();
        todoCursor = getAllEntriesFromDb();
        updateTaskList();
    }

    private Cursor getAllEntriesFromDb() {
        todoCursor = todoDbAdapter.getAllTodos();
        if (todoCursor != null) {
            startManagingCursor(todoCursor);
            todoCursor.moveToFirst();
        }
        return todoCursor;
    }

    private void updateTaskList() {
        if (todoCursor != null && todoCursor.moveToFirst()) {
            do {
                long id = todoCursor.getLong(TodoDbAdapter.ID_COLUMN);
                String description = todoCursor.getString(TodoDbAdapter.DESCRIPTION_COLUMN);
                boolean completed = todoCursor.getInt(TodoDbAdapter.COMPLETED_COLUMN) > 0 ? true : false;
                tasks.add(new TodoTask(id, description, completed));
            } while (todoCursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        if (todoDbAdapter != null)
            todoDbAdapter.close();
        super.onDestroy();
    }

    private void initListViewOnItemClick() {
        lvTodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                TodoTask task = tasks.get(position);
                if (task.isCompleted()) {
                    todoDbAdapter.updateTodo(task.getId(), task.getDescription(), false);
                } else {
                    todoDbAdapter.updateTodo(task.getId(), task.getDescription(), true);
                }
                updateListViewData();
            }
        });
    }

    private void updateListViewData() {
//do uzupelnienia
    }

    private void saveMeal() {
        String taskDescription = etNewTask.getText().toString();
        if(taskDescription.equals("")){
            etNewTask.setError("Your task description couldn't be empty string.");
        } else {
            Toast.makeText(getApplicationContext(),taskDescription,
                    Toast.LENGTH_SHORT).show();
            todoDbAdapter.insertTodo(taskDescription);
            etNewTask.setText("");
        }
        updateListViewData();
    }
}
