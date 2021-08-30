package com.arashparsa.a7ltodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.arashparsa.a7ltodolist.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddTaskDialog.AddNewTaskCallBack, TaskAdapter.TaskItemEventListener, EditTaskDialog.EditTaskCallBack {

    ActivityMainBinding binding;
    TaskAdapter taskAdapter;

    private SQLiteOpenHelper sqLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.rvMainTask.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        /*taskList.add(new Task(1,"arash",true));
        taskList.add(new Task(2,"arash",true));
        taskList.add(new Task(3,"arash",true));
        taskList.add(new Task(4,"arash",true));
        taskList.add(new Task(5,"arash",true));
        taskList.add(new Task(6,"arash",true));

*/
        sqLiteOpenHelper = new SQLiteOpenHelper(this);
        taskAdapter = new TaskAdapter(this);
        List<Task> tasks = sqLiteOpenHelper.getTasks();
        taskAdapter.addItems(tasks);

        binding.rvMainTask.setAdapter(taskAdapter);

        binding.ivDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteOpenHelper.deleteTasks();
                taskAdapter.clearItems();
            }
        });








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
        long result = sqLiteOpenHelper.addTask(task);
        if (result > -1) {
            task.setId(result);
            taskAdapter.addItem(task);
        } else {
            Toast.makeText(MainActivity.this, "Item did not inserted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteButtonClick(Task task) {
        int result = sqLiteOpenHelper.deleteTask(task);
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
    public void onEditTask(Task task) {
        int result = sqLiteOpenHelper.updateTask(task);
        if (result > 0) {
            taskAdapter.updateItem(task);
        }
    }
}