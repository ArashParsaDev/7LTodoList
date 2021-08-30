package com.arashparsa.a7ltodolist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditTaskDialog extends DialogFragment {

    private EditTaskCallBack callBack;
    private Task task;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callBack = (EditTaskCallBack) context;
        task = getArguments().getParcelable("task");
        if ((task == null)){
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_dialog_task,null,false);
        TextInputEditText etName = view.findViewById(R.id.et_editDialog_taskName);
        etName.setText(task.getNameTask());
        TextInputLayout inputLayout =view.findViewById(R.id.etl_editDialog_taskName_error);
        MaterialButton saveBtn = view.findViewById(R.id.btn_editDialog_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.length() > 0 ){
                    task.setNameTask(etName.getText().toString());
                    //because task just created
                    callBack.onEditTask(task);
                    dismiss();
                }else {
                    inputLayout.setError("عنوان نباید خالی باشد");
                }
            }
        });
        builder.setView(view);
        return builder.create();
    }

    public  interface EditTaskCallBack{
        void onEditTask(Task task);
    }
}
