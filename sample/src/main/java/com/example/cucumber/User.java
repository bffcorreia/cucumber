package com.example.cucumber;

import android.provider.BaseColumns;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import io.bffcorreia.cucumber.Cucumber;
import java.sql.SQLException;

@DatabaseTable(tableName = "user") public class User extends Cucumber<User> {

  @DatabaseField(columnName = BaseColumns._ID, id = true) private int id;
  @DatabaseField private String name;

  @Override protected Dao<User, Integer> getDao() throws SQLException {
    return AndroidApplication.databaseHelper.getDao(User.class);
  }

  @Override public int getId() {
    return id;
  }

  @Override public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
