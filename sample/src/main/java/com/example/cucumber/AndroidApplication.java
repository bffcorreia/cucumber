package com.example.cucumber;

import android.app.Application;

public class AndroidApplication extends Application {

  public static DatabaseHelper databaseHelper;

  @Override public void onCreate() {
    super.onCreate();
    databaseHelper = new DatabaseHelper(this);
  }
}
