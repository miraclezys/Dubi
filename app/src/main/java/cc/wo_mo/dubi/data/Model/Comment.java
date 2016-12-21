package cc.wo_mo.dubi.data.Model;

/**
 * Created by womo on 2016/12/8.
 */

public class Comment extends BaseResponse {
    public String content;
    public String time;
    public Integer from_user_id;
    public Integer to_user_id;
    public User from_user;
    public String to_username;
    public Integer tweet_id;

    public Comment(Integer from_user_id, String content, Integer to_user_id) {
        this.from_user_id = from_user_id;
        this.content = content;
        this.to_user_id = to_user_id;
    }
}
