package thanhdnh.ueh.edu.article_app;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserData {
  public static UserList data;
  private final Context context;
  private final GridView gridview;
  private final ExecutorService executor = Executors.newSingleThreadExecutor();

  public UserData(Context context, GridView gridview) {
    this.context = context;
    this.gridview = gridview;
  }

  public static UserProfile getUserFromId(int id) {
    if (data != null && data.getUsers() != null)
      for (UserProfile user : data.getUsers())
        if (user.getId() == id) return user;
    return null;
  }

  public void loadData(String url, Activity activity) {
    executor.execute(() -> {
      File file = DownloadWithProgress.downloadFile(url, context.getCacheDir());
      UserList result = null;
      try {
        if (file != null) result = new Gson().fromJson(readText(file), UserList.class);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (file != null) file.delete();
      }
      final UserList users = result;
      activity.runOnUiThread(() -> {
        if (activity.isFinishing() || activity.isDestroyed()) return;
        activity.findViewById(R.id.progress_users).setVisibility(View.GONE);
        if (users != null && users.getUsers() != null) {
          data = users;
          gridview.setAdapter(new UserAdapter(data.getUsers(), context));
        } else {
          TextView message = activity.findViewById(R.id.tv_message);
          message.setText("Không đọc được dữ liệu. Kiểm tra link JSON.");
          message.setVisibility(View.VISIBLE);
        }
      });
    });
  }

  public String readText(File file) throws Exception {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
      StringBuilder text = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) text.append(line).append("\n");
      return text.toString();
    }
  }

  public void close() {
    executor.shutdownNow();
  }
}
