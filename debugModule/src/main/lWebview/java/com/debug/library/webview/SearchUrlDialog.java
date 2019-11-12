package com.debug.library.webview;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.debug.R;


/**
 * 输入url对话框
 * Created by yummyLau on 18-09-17
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class SearchUrlDialog extends AppCompatDialog {

    private EditText mEdittext;
    private Button mActionBtn;
    private UrlAction mUrlAction;

    public SearchUrlDialog(@NonNull final Context context) {
        super(context, R.style.search_dialog_style);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_search, null, false);
        setContentView(view);
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
        setCanceledOnTouchOutside(true);
        mEdittext = view.findViewById(R.id.url_content);
        mActionBtn = view.findViewById(R.id.action);
        mActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mEdittext.getText().toString();
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(context, "输入url不能为空 ！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mUrlAction != null) {
                    mUrlAction.onAction(url);
                    mEdittext.setText("");
                }
            }
        });
    }

    public interface UrlAction {
        void onAction(String url);
    }

    public void setUrlAction(UrlAction urlAction) {
        this.mUrlAction = urlAction;
    }
}
