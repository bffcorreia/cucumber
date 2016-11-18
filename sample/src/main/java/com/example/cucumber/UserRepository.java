package com.example.cucumber;

import com.j256.ormlite.dao.Dao;
import io.bffcorreia.cucumber.CucumberRepository;
import java.sql.SQLException;

public class UserRepository extends CucumberRepository<User> {

  private final DatabaseHelper databaseHelper;

  public UserRepository(DatabaseHelper databaseHelper) {
    this.databaseHelper = databaseHelper;
  }

  @Override protected Dao<User, Integer> getDao() throws SQLException {
    return databaseHelper.getDao(User.class);
  }

  public UserRepository whereName(String name) throws SQLException {
    addWhereClause(where().eq("name", name));
    return this;
  }
}
