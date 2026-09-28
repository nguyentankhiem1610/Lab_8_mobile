package thanhdnh.ueh.edu.article_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
  private static final String USERS_URL =
      "https://raw.githubusercontent.com/nguyentankhiem1610/Lab_8_mobile/refs/heads/main/users.json";
  public GridView gridview;
  private UserData userData;

  private final AdapterView.OnItemClickListener onitemclick =
      new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          Intent intent = new Intent(MainActivity.this, ViewUserActivity.class);
          intent.putExtra("id", gridview.getAdapter().getItemId(position));
          startActivity(intent);
        }
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (getSupportActionBar() != null) getSupportActionBar().hide();
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_root), (v, insets) -> {
      Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
      return insets;
    });
    gridview = findViewById(R.id.gridview);
    userData = new UserData(this, gridview);
    userData.loadData(USERS_URL, this);
    gridview.setOnItemClickListener(onitemclick);
  }

  @Override
  protected void onDestroy() {
    if (userData != null) userData.close();
    super.onDestroy();
  }
}
