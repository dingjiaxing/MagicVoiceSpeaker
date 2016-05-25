package biz.home.api.myComponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import biz.home.R;
import biz.home.application.myUtils.DensityUtil;

/**
 * Created by admin on 2016/1/27.
 */
public class Topbar extends RelativeLayout {
    private TextView tvTitle;
    private String title;
    private returnClickListener listener;
    private ImageView iv_return;
    private LayoutParams leftParams,titleParams;
    public interface returnClickListener{
        public void leftClick();
    }
    public void setOnLeftClickListener(returnClickListener listener){
        this.listener=listener;
    }
    public Topbar(Context context) {
        super(context);
    }
    public void setText(String text){
        tvTitle.setText(text);
    }
    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.Topbar);
        title=ta.getString(R.styleable.Topbar_title2);
        ta.recycle();

        setBackgroundColor(0xff1a8dc3);
        tvTitle=new TextView(context);
        tvTitle.setText(title);
        tvTitle.setTextColor(getResources().getColor(R.color.white));
        tvTitle.setTextSize(DensityUtil.px2dip(context,40));

        iv_return=new ImageView(context);
        iv_return.setImageResource(R.drawable.left_or_back);
        iv_return.setBackgroundColor(0xff1a8dc3);
        iv_return.setPadding(DensityUtil.dip2px(context,15),DensityUtil.dip2px(context,15),DensityUtil.dip2px(context,15),DensityUtil.dip2px(context,15));
        leftParams=new LayoutParams(DensityUtil.dip2px(context,50),DensityUtil.dip2px(context,50));
        leftParams.addRule(ALIGN_PARENT_LEFT,TRUE);
        addView(iv_return,leftParams);

        titleParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(CENTER_IN_PARENT,TRUE);
        addView(tvTitle,titleParams);

        iv_return.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });
    }
}
