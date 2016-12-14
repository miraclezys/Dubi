package cc.wo_mo.dubi.data.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by womo on 2016/12/8.
 */

public class User extends BaseResponse {
    public String username;
    public String photo_url;
    public Integer user_id;
    @SerializedName("is_friend")
    public Boolean isFriend;
    @SerializedName("is_fan")
    public Boolean isFan;
    @Expose private Integer photo;
}
