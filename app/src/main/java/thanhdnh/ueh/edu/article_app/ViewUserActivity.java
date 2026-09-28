package thanhdnh.ueh.edu.article_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import okhttp3.Call;

public class ViewUserActivity extends AppCompatActivity {
  private Call downloadCall;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_user);
    if (getSupportActionBar() != null) getSupportActionBar().hide();
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_root), (v, insets) -> {
      Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
      return insets;
    });

    ImageView avatar = findViewById(R.id.iv_detail_avatar);
    TextView username = findViewById(R.id.tv_detail_username);
    TextView email = findViewById(R.id.tv_detail_email);
    TextView userId = findViewById(R.id.tv_detail_id);
    TextView desc = findViewById(R.id.tv_detail_desc);
    TextView hobby = findViewById(R.id.tv_detail_hobby);
    ProgressBar progress = findViewById(R.id.progress_avatar);

    int id = (int) getIntent().getLongExtra("id", -1);
    UserProfile user = UserData.getUserFromId(id);

    if (user == null) {
      Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }
    username.setText(user.getUsername());
    email.setText("Email: " + user.getEmail());
    userId.setText("ID: " + user.getId());
    desc.setText(user.getDesc());
    hobby.setText(user.getHobby());
    avatar.setContentDescription("Ảnh đại diện của " + user.getUsername());
    findViewById(R.id.btn_back).setOnClickListener(v -> finish());

    String url = user.getAvatarUrl();
    if (url == null || url.trim().isEmpty()) {
      progress.setVisibility(View.GONE);
    } else {
      downloadCall = DownloadWithProgress.downloadWithProgress(url.trim(),
          new Handler(Looper.getMainLooper()), this, getCacheDir(), progress, avatar);
    }
  }

  @Override
  protected void onDestroy() {
    if (downloadCall != null) downloadCall.cancel();
    super.onDestroy();
  }
}
