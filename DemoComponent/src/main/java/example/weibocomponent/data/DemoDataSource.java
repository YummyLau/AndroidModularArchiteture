package example.weibocomponent.data;


import java.util.List;

import androidx.lifecycle.LiveData;
import example.basiclib.net.resource.Resource;
import example.weibocomponent.data.local.db.entity.StatusEntity;
import example.weibocomponent.data.local.db.entity.UserEntity;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public interface DemoDataSource {

    LiveData<Resource<List<StatusEntity>>> getHomeStatus();

    LiveData<Resource<UserEntity>> getUserInfo(long uid);
}
