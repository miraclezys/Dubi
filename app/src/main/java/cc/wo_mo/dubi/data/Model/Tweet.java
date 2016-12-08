package cc.wo_mo.dubi.data.Model;

/**
 * Created by womo on 2016/12/8.
 */

public class Tweet extends BaseResponse {
    public User user;
    public String description;
    public String image_url;
    public Integer tweet_id;

    public Tweet(String description, String image_url) {
        this.description = description;
        this.image_url = image_url;
    }
}
