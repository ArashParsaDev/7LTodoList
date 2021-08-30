package com.arashparsa.a7ltodolist;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
            checkBoxTask.setOnCheckedChangeListener(null);
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

            checkBoxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    task.setCompleted(isChecked);
                    eventListener.onItemCheckedChange(task);
                    if(isChecked == true){
                        SpannableString spannableString = new SpannableString(task.getNameTask());
                        spannableString.setSpan(new StrikethroughSpan(),0,task.getNameTask().length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                        checkBoxTask.setText(spannableString);
                    }else
                        checkBoxTask.setText(task.getNameTask());
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

    public void searchTasks(List<Task> tasks){
        this.taskList =tasks;
        notifyDataSetChanged();
    }


    public interface TaskItemEventListener{
        void onDeleteButtonClick(Task task);
        void onUpdateButtonClick(Task task);
        void onItemCheckedChange(Task task);
   }
}
