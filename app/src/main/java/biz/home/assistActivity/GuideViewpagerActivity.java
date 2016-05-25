package biz.home.assistActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.adapter.GuideViewPagerAdapter;

/**
 * Created by admin on 2015/11/24.
 */
public class GuideViewpagerActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private GuideViewPagerAdapter vpAdapter;
    private List<View> views;
    private ImageView imageTeach;
    private Boolean teachFlag=false;

    //引导图片资源
    private static final int[] pics = { R.drawable.guide1_bg,
            R.drawable.guide2_bg, R.drawable.guide3_bg };

    //底部小店图片
    private ImageView[] dots ;

    //记录当前选中位置
    private int currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_viewpager_activity);
        imageTeach= (ImageView) findViewById(R.id.guideTeachImage);
        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //初始化引导图片列�?
        for(int i=0; i<pics.length; i++) {
            System.out.println("添加引导图片i:" + i);
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.guideViewpager);
        //初始化Adapter
        vpAdapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        //绑定回调
        vp.setOnPageChangeListener(this);
//        button = (Button) findViewById(R.id.button);
        //初始化底部小�?
        initDots();
        imageTeach.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent();
                intent.setClass(GuideViewpagerActivity.this, SpeakToitActivity.class);
                GuideViewpagerActivity.this.startActivity(intent);
                finish();
                /*
                if(!teachFlag){
                    imageTeach.setImageResource(R.drawable.guide_teach_telephone02);
                    teachFlag=true;
                }else {
                    Intent intent=new Intent();
                    intent.setClass(GuideViewpagerActivity.this, SpeakToitActivity.class);
                    GuideViewpagerActivity.this.startActivity(intent);
                    finish();
                }
                */

            }
        });


    }
    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰�?
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即�?中状�?
    }
    /**
     *设置当前的引导页
     */
    private void setCurView(int position)
    {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }
    /**
     *这只当前引导小点的�?�?
     */
    private void setCurDot(int positon)
    {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状�?
        setCurDot(arg0);
        if(arg0 == 2){
            imageTeach.setVisibility(View.VISIBLE);

        }else{
            imageTeach.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
