package thanhdnh.ueh.edu.article_app;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;

public class DownloadWithProgress {
  private static final OkHttpClient client = new OkHttpClient();

  public static File downloadFile(String url, File cached) {
    File file = null;
    try {
      Request request = new Request.Builder().url(url).build();
      try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful() || response.body() == null) return null;
        file = File.createTempFile("users_", ".json", cached);
        try (BufferedSink sink = Okio.buffer(Okio.sink(file))) {
          sink.writeAll(response.body().source());
        }
        return file;
      }
    } catch (Exception e) {
      if (file != null) file.delete();
      e.printStackTrace();
      return null;
    }
  }

  public static Call downloadWithProgress(String url, Handler mainHandler,
      Context context, File where2store, ProgressBar progressBar, ImageView imageView) {
    Request request = new Request.Builder().url(url).build();
    Call call = client.newCall(request);
    progressBar.setVisibility(View.VISIBLE);
    progressBar.setIndeterminate(true);
    call.enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        mainHandler.post(() -> {
          if (call.isCanceled()) return;
          progressBar.setVisibility(View.GONE);
          Toast.makeText(context, "Không tải được ảnh", Toast.LENGTH_SHORT).show();
        });
      }

      @Override
      public void onResponse(Call call, Response response) {
        File file = null;
        try (Response result = response) {
          ResponseBody body = result.body();
          if (!result.isSuccessful() || body == null) throw new IOException("Lỗi tải ảnh");
          file = File.createTempFile("avatar_", ".img", where2store);
          long total = body.contentLength();
          try (InputStream input = body.byteStream();
               FileOutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[8192];
            long downloaded = 0;
            int count;
            while ((count = input.read(buffer)) != -1) {
              output.write(buffer, 0, count);
              downloaded += count;
              if (total > 0) {
                int percent = (int) (downloaded * 100 / total);
                mainHandler.post(() -> {
                  if (call.isCanceled()) return;
                  progressBar.setIndeterminate(false);
                  progressBar.setProgress(percent);
                });
              }
            }
          }
          File image = file;
          mainHandler.post(() -> {
            if (!call.isCanceled()) {
              imageView.setImageURI(Uri.fromFile(image));
              progressBar.setVisibility(View.GONE);
            }
            image.delete();
          });
        } catch (IOException e) {
          if (file != null) file.delete();
          onFailure(call, e);
        }
      }
    });
    return call;
  }
}
