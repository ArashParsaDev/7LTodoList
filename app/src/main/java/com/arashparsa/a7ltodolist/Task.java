package com.arashparsa.a7ltodolist;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {

    private long id;
    private String nameTask;
    private boolean isCompleted;

    public Task() {
    }

    public Task(long id, String nameTask, boolean isCompleted) {
        this.nameTask = nameTask;
        this.id = id;
        this.isCompleted = isCompleted;
    }


    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }








    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.nameTask);
        dest.writeByte(this.isCompleted ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.nameTask = source.readString();
        this.isCompleted = source.readByte() != 0;
    }

    protected Task(Parcel in) {
        this.id = in.readLong();
        this.nameTask = in.readString();
        this.isCompleted = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
