package cc.wo_mo.dubi.data.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by womo on 2016/12/8.
 */

public class User extends BaseResponse {
    public String username;
    public String photo_url;
    public String introduction;
    public String birth;
    public String gender;
    public String region;
    public Integer user_id;
    @SerializedName("is_friend")
    public Boolean isFriend;
    @SerializedName("is_fan")
    public Boolean isFan;
    @SerializedName("friend_count")
    public int friendCount;
    @SerializedName("fans_count")
    public int fansCount;

    public User(){}

    public User(String photo_url, String introduction, String birth, String region, String gender) {
        this.photo_url = photo_url;
        this.introduction = introduction;
        this.birth = birth;
        this.region = region;
        this.gender = gender;
    }

}
