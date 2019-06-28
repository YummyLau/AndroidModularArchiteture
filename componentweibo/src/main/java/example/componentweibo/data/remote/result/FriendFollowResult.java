package example.componentweibo.data.remote.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import example.componentweibo.data.local.db.entity.UserEntity;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/16.
 */

public class FriendFollowResult extends ApiResult {

    @SerializedName("next_cursor")
    public int nextCursor;

    @SerializedName("total_number")
    public int totalNumber;

    @SerializedName("previous_cursor")
    public int previousCursor;

    @SerializedName("users")
    public List<UserEntity> userList;
}


