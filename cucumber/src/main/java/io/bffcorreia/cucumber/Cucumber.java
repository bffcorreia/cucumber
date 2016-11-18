package io.bffcorreia.cucumber;

import com.j256.ormlite.stmt.UpdateBuilder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Cucumber<T extends Cucumber<T>> extends CucumberDbAccess<T> {

  private final List<ValidationError> validationErrors;

  public Cucumber() {
    this.validationErrors = new ArrayList<>();
  }

  public boolean create() {
    try {
      beforeSave();
      getDao().create((T) this);
      afterSave();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  public boolean createIfNotExist() {
    try {
      if (getDao().idExists(getId())) {
        return false;
      }
      return create();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean update() {
    try {
      beforeSave();
      getDao().update((T) this);
      afterSave();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  public boolean save() {
    if (isNew()) {
      return create();
    } else {
      return update();
    }
  }

  @Override public boolean delete() {
    try {
      int deletedRows = getDao().delete((T) this);
      return deletedRows == 1;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean updateColumnValue(String columnName, Object value) {
    try {
      UpdateBuilder<T, Integer> updateBuilder = getDao().updateBuilder();
      updateBuilder.where().idEq(getId());
      updateBuilder.updateColumnValue(columnName, value);
      int updatedRows = updateBuilder.update();
      return updatedRows == 1;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateId(int newId) throws SQLException {
    getDao().updateId((T) this, newId);
  }

  public void beforeSave() {
  }

  public void afterSave() {
  }

  public int refresh() {
    try {
      return getDao().refresh((T) this);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public abstract int getId();

  public abstract void setId(int id);

  public boolean isNew() {
    try {
      return !getDao().idExists(getId());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<ValidationError> getValidationErrors() {
    return validationErrors;
  }

  public void setValidationErrors(List<ValidationError> validationErrors) {
    this.validationErrors.clear();
    this.validationErrors.addAll(validationErrors);
  }

  public void setValidationError(int key) {
    validationErrors.add(new ValidationError(key));
  }
}
