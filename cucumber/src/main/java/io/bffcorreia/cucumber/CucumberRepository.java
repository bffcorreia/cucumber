package io.bffcorreia.cucumber;

import android.provider.BaseColumns;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public abstract class CucumberRepository<T extends Cucumber<T>> extends CucumberDbAccess<T> {

  public boolean save(final Collection<T> list) {
    try {
      return getDao().callBatchTasks(new Callable<Boolean>() {
        @Override public Boolean call() throws Exception {
          for (T model : list) {
            model.save();
          }
          return true;
        }
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public boolean create(final Collection<T> list) {
    try {
      return getDao().callBatchTasks(new Callable<Boolean>() {
        @Override public Boolean call() throws Exception {
          for (T model : list) {
            model.create();
          }
          return true;
        }
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public boolean createIfNotExist(final Collection<T> list) {
    try {
      return getDao().callBatchTasks(new Callable<Boolean>() {
        @Override public Boolean call() throws Exception {
          for (T model : list) {
            model.createIfNotExist();
          }
          return true;
        }
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public boolean update(final Collection<T> list) {
    try {
      return getDao().callBatchTasks(new Callable<Boolean>() {
        @Override public Boolean call() throws Exception {
          for (T model : list) {
            model.update();
          }
          return true;
        }
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public int updateColumnValue(String columnName, Object value) throws SQLException {
    UpdateBuilder<T, Integer> updateBuilder = getDao().updateBuilder();
    updateBuilder.where()
        .in(BaseColumns._ID, getModelIdsAsList(all()));
    updateBuilder.updateColumnValue(columnName, value);
    return updateBuilder.update();
  }

  public List<Integer> getModelIdsAsList(Collection<T> models) {
    List<Integer> ids = new ArrayList<>(models.size());

    for (T model : models) {
      ids.add(model.getId());
    }

    return ids;
  }

  // WARNING: This method is not compliant with our CucumberDbAccess and, therefore,
  // it didn't take into account the where clauses
  public List<Integer> checkWhichIdsExist(Set<Integer> ids) throws SQLException {
    List<Integer> idsInDb = new ArrayList<>();
    Dao<T, Integer> dao = getDao();

    for (Integer id : ids) {
      if (dao.idExists(id)) {
        idsInDb.add(id);
      }
    }

    return idsInDb;
  }

  public CucumberRepository<T> whereId(int id) throws SQLException {
    addWhereClause(where().eq(BaseColumns._ID, id));
    return this;
  }

  public CucumberRepository<T> whereIds(List<Integer> ids) throws SQLException {
    addWhereClause(where().in(BaseColumns._ID, ids));
    return this;
  }

  public CucumberRepository<T> join(QueryBuilder queryBuilder) throws SQLException {
    addJoinClause(queryBuilder);
    return this;
  }

  public CucumberRepository<T> leftJoin(QueryBuilder queryBuilder) throws SQLException {
    addLeftJoinClause(queryBuilder);
    return this;
  }
}
