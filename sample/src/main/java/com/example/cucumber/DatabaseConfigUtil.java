package com.example.cucumber;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
  private static final Class<?>[] classes = new Class[] {
      User.class,
  };

  public static void main(String[] args) throws Exception {
    writeConfigFile("new_ormlite_config.txt", classes);
  }
}
