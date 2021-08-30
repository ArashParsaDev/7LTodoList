package com.arashparsa.a7ltodolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    Context context;
    List<Task> taskList =new ArrayList<>();
    private TaskItemEventListener eventListener;

    public TaskAdapter(TaskItemEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        holder.bindTask(taskList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBoxTask;
        private  View deleteBtn;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxTask = itemView.findViewById(R.id.checkboxTask);
            deleteBtn = itemView.findViewById(R.id.ivDelete);
        }

        public void bindTask(Task task) {
            checkBoxTask.setChecked(task.isCompleted());
            checkBoxTask.setText(task.getNameTask());
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //notify activity by eventLisener
                    eventListener.onDeleteButtonClick(task);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    eventListener.onUpdateButtonClick(task);
                    return false;
                }
            });
        }
    }

    public  void addItem(Task task){
        taskList.add(0,task);
        notifyItemInserted(0);
    }


    public  void  addItems(List<Task> tasks){
        this.taskList.addAll(tasks);
        notifyDataSetChanged();
    }

    public  void deleteItem(Task task){
        for (int i = 0; i <taskList.size() ; i++) {
            if ((taskList.get(i).getId() == task.getId())){
                taskList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public  void  updateItem(Task task){
        for (int i = 0; i <taskList.size() ; i++) {
            if ((taskList.get(i).getId() == task.getId())) {
                taskList.set(i, task);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void clearItems() {
        this.taskList.clear();
        notifyDataSetChanged();
    }


    public interface TaskItemEventListener{
        void onDeleteButtonClick(Task task);
        void onUpdateButtonClick(Task task);
   }
}