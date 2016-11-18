package com.example.cucumber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

  private TextView nameView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    nameView = (TextView) findViewById(R.id.name);
    User user = findOrCreateUser();
    nameView.setText(user.getName());
  }

  private User findOrCreateUser() {
    UserRepository userRepository = new UserRepository(AndroidApplication.databaseHelper);
    try {
      User user = userRepository.whereName("bffcorreia").whereId(1).first();
      if (user == null) {
        user = new User();
        user.setId(1);
        user.setName("bffcorreia");
        user.save();
      }
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
