package example.componentlib.service.skin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import example.componentlib.service.IService;

/**
 * skin service
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2017/12/11.
 */

public interface ISkinService extends IService {

    void changeSkin(@NonNull Skin skin);

    @Nullable
    Skin[] getSkins();
}
