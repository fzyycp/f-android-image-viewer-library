package cn.faury.android.library.viewer.image;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowImageActivity extends Activity implements View.OnClickListener{

    private Context mContext;

    private ViewPager mVpImageView;

    private ImageView mIvBack;

    private ViewPagerAdapter mVpAdapter;

    private ImageView mIvShowImage;

    private TextView mTvImageCountNow;//当前下标

    private TextView mTvImageCountTotal;//总数

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private List<String> mUrls = new ArrayList<String>();//图片路径集合

    private List<View> mViews = new ArrayList<View>();

    private int mShowIndex = 0;//初始显示第几张图

    private boolean mScalable = true;//是否可缩放

    //传值标签名称
    public static final String INTENT_EXTRA_NAME_URL = "urls";//图片路径list

    public static final String INTENT_EXTRA_NAME_INDEX = "index";//显示图片的下标

    public static final String INTENT_EXTRA_NAME_SCALABLE = "scalable";//是否可缩放

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_show_image);
        setImageLoad();

        mVpImageView = (ViewPager) findViewById(R.id.vp_show_image);
        mIvBack = (ImageView) findViewById(R.id.iv_show_image_back);
        mTvImageCountNow = (TextView) findViewById(R.id.tv_show_image_count_now);
        mTvImageCountTotal = (TextView) findViewById(R.id.tv_show_image_count_total);

        mIvBack.setOnClickListener(this);

        //获取图片地址集合
        if(getIntent().getExtras() != null)
        {
            mUrls = getIntent().getExtras().getStringArrayList(INTENT_EXTRA_NAME_URL);
            mShowIndex = getIntent().getExtras().getInt(INTENT_EXTRA_NAME_INDEX , 0);
            mScalable = getIntent().getExtras().getBoolean(INTENT_EXTRA_NAME_SCALABLE , true);
        }

        for (int i = 0;i<mUrls.size();i++)
        {
            View view = getLayoutInflater().inflate(R.layout.part_showimage_viewpager, null);
            if(mScalable)
            {
                view.findViewById(R.id.iv_show_iamge_zoom).setVisibility(View.VISIBLE);
                view.findViewById(R.id.iv_show_iamge_normal).setVisibility(View.GONE);
                mIvShowImage = (ImageView) view.findViewById(R.id.iv_show_iamge_zoom);
                ImageLoader.getInstance().displayImage(mUrls.get(i).trim(),mIvShowImage , mOptions);
            }
            else
            {
                view.findViewById(R.id.iv_show_iamge_zoom).setVisibility(View.GONE);
                view.findViewById(R.id.iv_show_iamge_normal).setVisibility(View.VISIBLE);
                mIvShowImage = (ImageView) view.findViewById(R.id.iv_show_iamge_normal);
                ImageLoader.getInstance().displayImage(mUrls.get(i).trim(),mIvShowImage , mOptions);
            }
            mViews.add(view);
        }

        mVpAdapter = new ViewPagerAdapter(mViews);
        mVpImageView.setAdapter(mVpAdapter);

        if(mShowIndex<mUrls.size())
        {
            mVpImageView.setCurrentItem(mShowIndex);
        }
        else
        {
            mVpImageView.setCurrentItem(0);
        }

        mTvImageCountNow.setText(String.format(getResources().getString(R.string.image_count) , mShowIndex+1 ));
        mTvImageCountTotal.setText(mUrls.size()+"");

        mVpImageView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTvImageCountNow.setText(String.format(getResources().getString(R.string.image_count) , position+1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setImageLoad()
    {

        File file = new File(Environment.getExternalStorageDirectory(), "temp/Image");
        if (!file.exists())
        {
            file.mkdirs();
        }
        mImageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config2 = null;
        try {
            config2 = new ImageLoaderConfiguration.Builder(this)
                    .threadPoolSize(3)
                    .memoryCache(new MyLruMemoryCache(2 * 1024 * 1024))
                    .memoryCacheSizePercentage(13)
                    .diskCache(new LruDiskCache(file, new HashCodeFileNameGenerator() // 磁盘缓存
                    {
                        @Override
                        public String generate(String imageUri)
                        {
                            String[] string = imageUri.split("\\?");
                            imageUri = string.length > 0 ? string[0] : imageUri;
                            return super.generate(imageUri);
                        }
                    }, 50 * 1024 * 1024))
                    .diskCacheFileCount(100).writeDebugLogs()
                    .build();
            mImageLoader.init(config2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.mipmap.image_on_loading)           //加载图片时的图片
                .showImageOnFail(R.mipmap.image_on_error)              //加载失败时的图片
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_show_image_back) {
            finish();
        }
    }
}
