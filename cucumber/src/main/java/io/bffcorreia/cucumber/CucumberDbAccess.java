package io.bffcorreia.cucumber;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CucumberDbAccess<T extends Cucumber<T>> {

  private transient List<Where<T, Integer>> whereClauses;
  private transient List<QueryBuilder<T, Integer>> joinClauses;
  private transient List<QueryBuilder<T, Integer>> leftJoinClauses;
  private transient QueryBuilder<T, Integer> queryBuilder;
  private transient Where<T, Integer> where;

  // OrmLite needs a default constructor
  public CucumberDbAccess() {
    init();
  }

  private void init() {
    this.whereClauses = new ArrayList<>();
    this.joinClauses = new ArrayList<>();
    this.leftJoinClauses = new ArrayList<>();
  }

  protected abstract Dao<T, Integer> getDao() throws SQLException;

  public QueryBuilder<T, Integer> createQueryBuilder() {
    try {
      return getDao().queryBuilder();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  protected void mergeWhereClauses() throws SQLException {
    int numberOfClauses = whereClauses.size();

    switch (numberOfClauses) {
      case 0:
        break;
      case 1:
        getQueryBuilderInstance().setWhere(where());
        break;
      default:
        where().and(whereClauses.size());
        break;
    }
  }

  private void clearWhereClauses() {
    whereClauses.clear();
    if (where() != null) {
      where = null;
    }
  }

  private void mergeJoinClauses() throws SQLException {
    if (joinClauses.size() > 0) {
      for (QueryBuilder joinClause : joinClauses) {
        getQueryBuilderInstance().join(joinClause);
      }
    }

    if (leftJoinClauses.size() > 0) {
      for (QueryBuilder leftJoinClause : leftJoinClauses) {
        getQueryBuilderInstance().leftJoin(leftJoinClause);
      }
    }
  }

  private void clearJoinClauses() {
    joinClauses.clear();
    leftJoinClauses.clear();
  }

  private void mergeClauses() throws SQLException {
    mergeWhereClauses();
    mergeJoinClauses();
  }

  private void clearClauses() {
    clearJoinClauses();
    clearWhereClauses();
    // TODO CHANGE ME FROM THIS METHOD
    queryBuilder = createQueryBuilder();
  }

  protected void addJoinClause(QueryBuilder<T, Integer> queryBuilder) {
    joinClauses.add(queryBuilder);
  }

  protected void addLeftJoinClause(QueryBuilder<T, Integer> queryBuilder) {
    leftJoinClauses.add(queryBuilder);
  }

  protected void addWhereClause(Where<T, Integer> where) {
    whereClauses.add(where);
  }

  public boolean delete() throws SQLException {
    DeleteBuilder<T, Integer> deleteBuilder = getDao().deleteBuilder();
    mergeClauses();
    deleteBuilder.setWhere(where);
    int deletedRows = deleteBuilder.delete();
    clearClauses();
    return deletedRows > 0;
  }

  public Collection<T> all() throws SQLException {
    mergeClauses();
    Collection<T> collection = getQueryBuilderInstance().query();
    clearClauses();
    return collection;
  }

  public T first() throws SQLException {
    mergeClauses();
    T model = getQueryBuilderInstance().queryForFirst();
    clearClauses();
    return model;
  }

  public QueryBuilder<T, Integer> buildQueryBuilder() throws SQLException {
    QueryBuilder<T, Integer> newQb = getQueryBuilderInstance();
    mergeClauses();
    // TODO: we might not need this
    if (whereClauses.size() > 0) {
      newQb.setWhere(where());
    }
    clearClauses();
    return newQb;
  }

  public long count() throws SQLException {
    long count;

    mergeClauses();
    count = getQueryBuilderInstance().countOf();
    clearClauses();

    return count;
  }

  public CucumberDbAccess<T> distinct() {
    getQueryBuilderInstance().distinct();
    return this;
  }

  public CucumberDbAccess<T> limit(Long limit) {
    getQueryBuilderInstance().limit(limit);
    return this;
  }

  public CucumberDbAccess<T> orderByRaw(String orderBy) {
    getQueryBuilderInstance().orderByRaw(orderBy);
    return this;
  }

  public CucumberDbAccess<T> orderBy(String columnName, boolean ascending) {
    getQueryBuilderInstance().orderBy(columnName, ascending);
    return this;
  }

  protected Where<T, Integer> where() {
    if (where != null) {
      return where;
    } else {
      return where = getQueryBuilderInstance().where();
    }
  }

  private QueryBuilder<T, Integer> getQueryBuilderInstance() {
    if (queryBuilder == null) {
      queryBuilder = createQueryBuilder();
    }
    return queryBuilder;
  }
}
