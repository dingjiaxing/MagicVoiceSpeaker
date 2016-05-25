package biz.home.assistActivity.resource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import biz.home.R;
import biz.home.SettingInformationsActivity;
import biz.home.api.myComponent.Topbar;
import biz.home.bean.MagicInfo;
import biz.home.model.MagicInfoApiEnum;
import biz.home.util.HttpHelp;

/**
 * Created by admin on 2016/1/27.
 */
public class MyInformationTopMenu extends Activity implements View.OnClickListener{
    private Topbar topbar;
    private LinearLayout line1,line2,line3,line4,line5,line6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information_top_menu);
        topbar= (Topbar) findViewById(R.id.my_information_topbar);
        line1= (LinearLayout) findViewById(R.id.my_information_linear1);
        line2= (LinearLayout) findViewById(R.id.my_information_linear2);
        line3= (LinearLayout) findViewById(R.id.my_information_linear3);
        line4= (LinearLayout) findViewById(R.id.my_information_linear4);
        line5= (LinearLayout) findViewById(R.id.my_information_linear5);
        line6= (LinearLayout) findViewById(R.id.my_information_linear6);
        line1.setOnClickListener(this);
        line2.setOnClickListener(this);
        line3.setOnClickListener(this);
        line4.setOnClickListener(this);
        line5.setOnClickListener(this);
        line6.setOnClickListener(this);

        topbar.setOnLeftClickListener(new Topbar.returnClickListener() {
            @Override
            public void leftClick() {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_information_linear1:
                startActivity(new Intent(MyInformationTopMenu.this, SettingInformationsActivity.class));
                break;
            case R.id.my_information_linear2:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MagicInfo info=new MagicInfo();
                        info.setUid("B0FD0E37DB8E42EC9C1115F49C5C78AF");
                        info.setPostId("1B9A5B61FEF44BCBA61432BEF39F2A20");
                        info.setTitle("凑");
                        info.setPublisher("李测试机");
                        info.setApi(MagicInfoApiEnum.ADDRESOURCECONTACT);
                        String s= HttpHelp.send(info);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        info=new MagicInfo();
                        info.setUid("2D4EA4A79DCA43DF971AFE492479042B");
                        info.setPostId("1B9A5B61FEF44BCBA61432BEF39F2A20");
                        info.setTitle("凑");
                        info.setPublisher("李测试机");
                        info.setApi(MagicInfoApiEnum.ADDRESOURCECONTACT);
                         s= HttpHelp.send(info);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                         info=new MagicInfo();
                        info.setUid("B0FD0E37DB8E42EC9C1115F49C5C78AF");
                        info.setPostId("00cea936c158435abd5cdc7ba26fad2c");
                        info.setTitle("凑");
                        info.setPublisher("李测试机");
                        info.setApi(MagicInfoApiEnum.ADDRESOURCECONTACT);
                         s= HttpHelp.send(info);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        info=new MagicInfo();
                        info.setUid("88F8043BEBAB4BE8B647AB30C57CB8E1");
                        info.setPostId("1B9A5B61FEF44BCBA61432BEF39F2A20");
                        info.setTitle("继续");
                        info.setPublisher("李测试机");
                        info.setApi(MagicInfoApiEnum.ADDRESOURCECONTACT);
                        s= HttpHelp.send(info);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        info=new MagicInfo();
                        info.setUid("2D4EA4A79DCA43DF971AFE492479042B");
                        info.setPostId("a0c27aff29644636acbb12c8933e664b");
                        info.setTitle("继续");
                        info.setPublisher("李测试机");
                        info.setApi(MagicInfoApiEnum.ADDRESOURCECONTACT);
                        s= HttpHelp.send(info);
                    }
                }).start();

                break;
            case R.id.my_information_linear3:
                startActivity(new Intent(MyInformationTopMenu.this, ContactedResourceList.class));
                break;
            case R.id.my_information_linear4:
                startActivity(new Intent(MyInformationTopMenu.this, MyReleasedResourceList.class));
                break;
            case R.id.my_information_linear5:
                startActivity(new Intent(MyInformationTopMenu.this, MyCollectionResourceList.class));
                break;
            case R.id.my_information_linear6:
                startActivity(new Intent(MyInformationTopMenu.this, MagicRecommendResourceList.class));
                break;
        }
    }
}
