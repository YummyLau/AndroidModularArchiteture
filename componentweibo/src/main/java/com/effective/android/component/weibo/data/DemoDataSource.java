package com.effective.android.component.weibo.data;


import androidx.lifecycle.LiveData;

import java.util.List;

import example.basiclib.net.resource.Resource;
import com.effective.android.component.weibo.data.local.db.entity.StatusEntity;
import com.effective.android.component.weibo.data.local.db.entity.UserEntity;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */
public interface DemoDataSource {

    LiveData<Resource<List<StatusEntity>>> getHomeStatus();

    LiveData<Resource<UserEntity>> getUserInfo(long uid);
}
