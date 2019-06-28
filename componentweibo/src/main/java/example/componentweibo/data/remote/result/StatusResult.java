package example.componentweibo.data.remote.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import example.componentweibo.data.local.db.entity.StatusEntity;


/**
 * 获取关注微博列表
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/14.
 */

public class StatusResult extends ApiResult {

    @SerializedName("total_number")
    public int totalNum;

    @SerializedName("statuses")
    public List<StatusEntity> statusList;
}
