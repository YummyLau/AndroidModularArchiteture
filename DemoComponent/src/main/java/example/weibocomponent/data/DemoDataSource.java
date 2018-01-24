package example.weibocomponent.data;


import android.arch.lifecycle.LiveData;
import java.util.List;

import example.basiclib.net.resource.Resource;
import example.weibocomponent.data.local.db.entity.StatusEntity;
import example.weibocomponent.data.local.db.entity.UserEntity;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */
public interface DemoDataSource {

    LiveData<Resource<List<StatusEntity>>> getHomeStatus();

    LiveData<Resource<UserEntity>> getUserInfo(long uid);
}
