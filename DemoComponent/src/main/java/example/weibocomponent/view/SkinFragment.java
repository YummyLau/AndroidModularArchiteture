package example.weibocomponent.view;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import example.basiclib.activity.BaseBindingActivity;
import example.basiclib.activity.BaseBindingFragment;
import example.basiclib.activity.BaseFragment;
import example.basiclib.net.resource.Resource;
import example.basiclib.widget.roundimage.RoundedImageView;
import example.componentlib.service.ServiceManager;
import example.componentlib.service.skin.ISkinService;
import example.componentlib.service.skin.Skin;
import example.weibocomponent.R;
import example.weibocomponent.databinding.DemoFragmentSkinLayoutBinding;

/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/1/24.
 */

public class SkinFragment extends BaseBindingFragment<DemoFragmentSkinLayoutBinding> {


    private ISkinService skinService;

    @Override
    public int getLayoutRes() {
        return R.layout.demo_fragment_skin_layout;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        skinService = ServiceManager.getService(ISkinService.class);
        initView();
        return dataBinding.getRoot();
    }

    private void initView() {
        if (skinService == null) {
            Toast.makeText(getContext(), R.string.demo_skin_no_registe, Toast.LENGTH_SHORT);
            return;
        }
        final Skin[] skins = skinService.getSkins();

        if (skins == null) {
            Toast.makeText(getContext(), R.string.demo_skin_no_content, Toast.LENGTH_SHORT);
        }

        for (int i = 0; i < skins.length; i++) {
            RoundedImageView roundedImageView = new RoundedImageView(getContext());
            roundedImageView.setBackgroundColor(skins[i].color);
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(100, 100);
            layoutParams.setMargins(20, 0, 20, 0);
            roundedImageView.setOval(true);
            final int index = i;
            roundedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    skinService.changeSkin(skins[index]);
                }
            });
            dataBinding.dynamicLayout.addView(roundedImageView, layoutParams);
        }
    }
}
