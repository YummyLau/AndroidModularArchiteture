package example.componentweibo;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class WeiboActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_main);
    }
}
