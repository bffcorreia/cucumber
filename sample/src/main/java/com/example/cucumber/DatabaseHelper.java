package com.example.cucumber;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

  private static final String DATABASE_NAME = "cucumber_db";
  private static final int DATABASE_VERSION = 1;

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
  }

  @Override public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    try {
      TableUtils.createTableIfNotExists(connectionSource, User.class);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion,
      int newVersion) {
    try {
      TableUtils.dropTable(connectionSource, User.class, true);
      onCreate(database, connectionSource);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
