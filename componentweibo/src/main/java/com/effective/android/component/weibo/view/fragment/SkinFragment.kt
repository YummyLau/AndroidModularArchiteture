package com.effective.android.component.weibo.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.effective.android.base.fragment.BaseFragment
import com.effective.android.component.weibo.R
import com.effective.android.component.weibo.databinding.DemoFragmentSkinLayoutBinding


/**
 * Email yummyl.lau@gmail.com
 * Created by yummylau on 2018/01/25.
 */

class SkinFragment : BaseFragment() {


    private val dataBinding: DemoFragmentSkinLayoutBinding? = null


    override fun getLayoutRes(): Int {
        return R.layout.demo_fragment_skin_layout
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        //        skinService = ServiceManager.getService(ISkinService.class);
        initView()
        return dataBinding!!.root
    }

    private fun initView() {
        //        if (skinService == null) {
        //            Toast.makeText(getContext(), R.string.demo_skin_no_registe, Toast.LENGTH_SHORT).show();
        //            return;
        //        }
        //        final Skin[] skins = skinService.getSkins();
        //
        //        if (skins == null) {
        //            Toast.makeText(getContext(), R.string.demo_skin_no_content, Toast.LENGTH_SHORT).show();
        //        }
        //
        //        for (int i = 0; i < skins.length; i++) {
        //            RoundedImageView roundedImageView = new RoundedImageView(getContext());
        //            roundedImageView.setBackgroundColor(skins[i].color);
        //            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(100, 100);
        //            layoutParams.setMargins(20, 0, 20, 0);
        //            roundedImageView.setOval(true);
        //            final int index = i;
        //            roundedImageView.setOnClickListener(new View.OnClickListener() {
        //                @Override
        //                public void onClick(View view) {
        //                    skinService.changeSkin(skins[index]);
        //                }
        //            });
        //            dataBinding.dynamicLayout.addView(roundedImageView, layoutParams);
        //        }
    }
}
