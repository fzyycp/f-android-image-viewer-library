package cn.faury.android.library.viewer.image.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import cn.faury.android.library.viewer.image.ShowImageActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add("https://www.wassk.cn/ssk-ssqr-web-v2/resource/images/getImage?imageId=d4c373f0-1af5-4eaf-aef5-0958c79c965a");
                list.add("https://www.wassk.cn/ssk-ssqr-web-v2/resource/images/getImage?imageId=977540d7-6f66-4901-9b43-f593e6df63bc");
                list.add("https://www.wassk.cn/ssk-ssqr-web-v2/resource/images/getImage?imageId=a550aa09-3348-4428-a24d-926000f1acf9");

                Intent intent = new Intent(MainActivity.this, ShowImageActivity.class);
                intent.putStringArrayListExtra(ShowImageActivity.INTENT_EXTRA_NAME_URL, list);
                intent.putExtra(ShowImageActivity.INTENT_EXTRA_NAME_INDEX, 0);
                intent.putExtra(ShowImageActivity.INTENT_EXTRA_NAME_SCALABLE, false);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
