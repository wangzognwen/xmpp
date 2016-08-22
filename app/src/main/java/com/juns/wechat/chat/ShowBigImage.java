package com.juns.wechat.chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.PhotoUtil;
import com.juns.wechat.widget.scalemageview.PhotoView;
import android.app.AlertDialog;

/**
 * 下载显示大图
 * 
 */
@Content(R.layout.activity_show_big_image)
public class ShowBigImage extends ToolbarActivity {

	private ProgressDialog pd;
	private PhotoView scaleImageView;
	private int default_res = R.drawable.default_image;
	private String localFilePath;
	private Bitmap bitmap;
	private boolean isDownloaded;
	private ProgressBar loadLocalPb;
    private String imageName;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int UPDATE_FXID = 4;// 结果
    private static final int UPDATE_NICK = 5;// 结果


    @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setToolbarTitle("我的头像");
        setToolbarRight(2, R.drawable.icon_more);
        scaleImageView = (PhotoView) findViewById(R.id.image);
		loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
		default_res = getIntent().getIntExtra("default_image", R.drawable.default_useravatar);
		Uri uri = getIntent().getParcelableExtra("uri");
		String remotepath = getIntent().getExtras().getString("remotepath");
		String secret = getIntent().getExtras().getString("secret");
		System.err.println("show big image uri:" + uri + " remotepath:"
				+ remotepath);

        String fileName = AccountManager.getInstance().getHeadUrl();
        LogUtil.i("fileName: " + fileName);
        ImageLoader.loadAvatar(scaleImageView, fileName);

        scaleImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

        findViewById(R.id.ivRightBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog();
            }
        });
	}

    private void showPhotoDialog() {
         final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.alertdialog);

        // 为确认按钮添加事件,执行退出应用操作
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("拍照");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {

                imageName = getNowTime() + ".png";
                PhotoUtil.takePhoto(ShowBigImage.this, PHOTO_REQUEST_TAKEPHOTO, imageName);
                dlg.cancel();
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("相册");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imageName = getNowTime() + ".png";
                PhotoUtil.openAlbum(ShowBigImage.this, PHOTO_REQUEST_GALLERY);
                dlg.cancel();
            }
        });

    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                 /*   startPhotoZoom(
                            Uri.fromFile(PhotoUtil.getFile(imageName)),
                            480);*/
                    Uri uri = Uri.fromFile(PhotoUtil.getFile(imageName));
                    PhotoUtil.cropView(uri, 480, ShowBigImage.this, PHOTO_REQUEST_CUT, imageName);
                    break;

                case PHOTO_REQUEST_GALLERY:
                    if (data != null)
                        PhotoUtil.cropView(data.getData(), 480, ShowBigImage.this, PHOTO_REQUEST_CUT, imageName);
                    break;

                case PHOTO_REQUEST_CUT:
                    // BitmapFactory.Options options = new BitmapFactory.Options();
                    //
                    // /**
                    // * 最关键在此，把options.inJustDecodeBounds = true;
                    // * 这里再decodeFile()，返回的bitmap为空
                    // * ，但此时调用options.outHeight时，已经包含了图片的高了
                    // */
                    // options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(PhotoUtil.PHOTO_PATH + "/"
                            + imageName);
                    scaleImageView.setImageBitmap(bitmap);
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }


	/**
	 * 通过远程URL，确定下本地下载后的localurl
	 * 
	 * @param remoteUrl
	 * @return
	 */
	public String getLocalFilePath(String remoteUrl) {
		String localPath = null;
		/*if (remoteUrl.contains("/")) {
			localPath = PathUtil.getDbManager().getImagePath().getAbsolutePath()
					+ "/" + remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
		} else {
			localPath = PathUtil.getDbManager().getImagePath().getAbsolutePath()
					+ "/" + remoteUrl;
		}*/
		return localPath;
	}

	/**
	 * 下载图片
	 * 
	 * @param remoteFilePath
	 */
	private void downloadImage(final String remoteFilePath,
			final Map<String, String> headers) {
		String str1 = getResources().getString(R.string.Download_the_pictures);
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage(str1);
		pd.show();
		localFilePath = getLocalFilePath(remoteFilePath);
	}

	@Override
	public void onBackPressed() {
		if (isDownloaded)
			setResult(RESULT_OK);
		finish();
	}
}
