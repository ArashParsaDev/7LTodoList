package com.arashparsa.a7ltodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.arashparsa.a7ltodolist.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddTaskDialog.AddNewTaskCallBack, TaskAdapter.TaskItemEventListener, EditTaskDialog.EditTaskCallBack {

    ActivityMainBinding binding;
    TaskAdapter taskAdapter;

    //private SQLiteOpenHelper sqLiteOpenHelper;
    private TaskDao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        taskDao = AppDatabase.getAppDatabase(this).getTaskDao();

        binding.etMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0){
                   // List<Task> tasks = sqLiteOpenHelper.searchTask(charSequence.toString());
                    List<Task> tasks = taskDao.search(charSequence.toString());
                    taskAdapter.searchTasks(tasks);
                }else {
                    //List<Task> tasks = sqLiteOpenHelper.getTasks();
                    List<Task> tasks = taskDao.getAll();
                    taskAdapter.searchTasks(tasks);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.rvMainTask.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
       // sqLiteOpenHelper = new SQLiteOpenHelper(this);
        taskAdapter = new TaskAdapter(this);
       // List<Task> tasks = sqLiteOpenHelper.getTasks();
        List<Task> tasks = taskDao.getAll();
        taskAdapter.addItems(tasks);

        binding.rvMainTask.setAdapter(taskAdapter);

        binding.ivDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sqLiteOpenHelper.deleteTasks();
                taskDao.deleteAll();
                taskAdapter.clearItems();
            }
        });

        //add task manually to DB
       /* Task task = new Task();
        task.setNameTask("hasan");
        task.setCompleted(true);
        long result = sqLiteOpenHelper.addTask(task);
        Log.i("MainActivity", "onCreate: " + result);*/

        binding.btnFabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskDialog addTaskDialog = new AddTaskDialog();
                addTaskDialog.show(getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public void onNewTask(Task task) {
       // long result = sqLiteOpenHelper.addTask(task);
        long result = taskDao.add(task);
        if (result > -1) {
            task.setId(result);
            taskAdapter.addItem(task);
        } else {
            Toast.makeText(MainActivity.this, "Item did not inserted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteButtonClick(Task task) {
       // int result = sqLiteOpenHelper.deleteTask(task);
        int result = taskDao.delete(task);
        if (result > 0) {
            taskAdapter.deleteItem(task);
        }
    }

    @Override
    public void onUpdateButtonClick(Task task) {
        EditTaskDialog editTaskDialog = new EditTaskDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("task", task);
        editTaskDialog.setArguments(bundle);
        editTaskDialog.show(getSupportFragmentManager(),null);
    }

    @Override
    public void onItemCheckedChange(Task task) {
        //sqLiteOpenHelper.updateTask(task);
        taskDao.update(task);

    }

    @Override
    public void onEditTask(Task task) {
        //int result = sqLiteOpenHelper.updateTask(task);
        int result = taskDao.update(task);
        if (result > 0) {
            taskAdapter.updateItem(task);
        }
    }
}