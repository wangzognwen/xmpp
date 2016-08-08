package com.juns.wechat.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.juns.wechat.R;
import com.juns.wechat.config.ConfigUtil;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;

/**
 * Created by 王宗文 on 2016/7/16.
 */
public class ImageUtil {
    private static final String REMOTE_PATH = ConfigUtil.REAL_SERVER + "/upload/";

    private static final ImageOptions OPTIONS = new ImageOptions.Builder()
            .setFailureDrawableId(R.drawable.default_useravatar)
            .setLoadingDrawableId(R.drawable.default_useravatar).build();

    public static void loadImage(ImageView imageView, String fileName){
        x.image().bind(imageView, REMOTE_PATH  + fileName, OPTIONS);
    }
}
