package thanhdnh.ueh.edu.article_app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfile {
  @Expose
  @SerializedName("id") private int id;
  @Expose
  @SerializedName("username") private String username;
  @Expose
  @SerializedName("email") private String email;
  @Expose
  @SerializedName("desc") private String desc;
  @Expose
  @SerializedName("avatar_url") private String avatar_url;
  @Expose
  @SerializedName("hobby") private String hobby;

  public UserProfile(int id, String username, String email, String desc,
                     String avatar_url, String hobby) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.desc = desc;
    this.avatar_url = avatar_url;
    this.hobby = hobby;
  }

  public int getId() {
    return id; }
  public String getUsername() {
    return username; }
  public String getEmail() {
    return email; }
  public String getDesc() {
    return desc; }
  public String getAvatarUrl() {
    return avatar_url; }
  public String getHobby() {
    return hobby; }

  public void setId(int id) {
    this.id = id;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  public void setAvatarUrl(String avatar_url) {
    this.avatar_url = avatar_url;
  }
  public void setHobby(String hobby) {
    this.hobby = hobby;
  }
}
