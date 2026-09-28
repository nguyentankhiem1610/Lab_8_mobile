package thanhdnh.ueh.edu.article_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
  private final ArrayList<UserProfile> users;
  private final LayoutInflater inflater;

  public UserAdapter(ArrayList<UserProfile> users, Context context) {
    this.users = users;
    inflater = LayoutInflater.from(context);
  }

  @Override public int getCount() { return users.size(); }
  @Override public UserProfile getItem(int position) { return users.get(position); }
  @Override public long getItemId(int position) { return getItem(position).getId(); }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    MyView holder;
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.user_disp_tpl, parent, false);
      holder = new MyView();
      holder.avatar = convertView.findViewById(R.id.iv_avatar);
      holder.username = convertView.findViewById(R.id.tv_username);
      convertView.setTag(holder);
    } else {
      holder = (MyView) convertView.getTag();
    }

    UserProfile user = getItem(position);
    holder.username.setText(user.getUsername());
    String url = user.getAvatarUrl();
    Picasso.get().load(url == null || url.trim().isEmpty() ? null : url.trim())
        .resize(300, 300).centerCrop()
        .placeholder(android.R.drawable.ic_menu_myplaces)
        .error(android.R.drawable.ic_menu_report_image)
        .into(holder.avatar);
    holder.avatar.setContentDescription("Ảnh đại diện của " + user.getUsername());
    return convertView;
  }

  private static class MyView {
    ImageView avatar;
    TextView username;
  }
}
