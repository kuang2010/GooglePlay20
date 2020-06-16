package com.kuang2010.googleplay20.viewhold;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.bean.CategoryInfoBean;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.util.BitmapUtilFactory;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import androidx.annotation.NonNull;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 15:09
 * desc:
 */
public class CategoryItemViewHold extends BaseViewHold<CategoryInfoBean> {

    @ViewInject(R.id.item_category_item_1)
    LinearLayout	mContainerItem1;

    @ViewInject(R.id.item_category_item_2)
    LinearLayout	mContainerItem2;

    @ViewInject(R.id.item_category_item_3)
    LinearLayout mContainerItem3;

    @ViewInject(R.id.item_category_icon_1)
    ImageView mIvIcon1;

    @ViewInject(R.id.item_category_icon_2)
    ImageView		mIvIcon2;

    @ViewInject(R.id.item_category_icon_3)
    ImageView		mIvIcon3;

    @ViewInject(R.id.item_category_name_1)
    TextView mTvName1;

    @ViewInject(R.id.item_category_name_2)
    TextView		mTvName2;

    @ViewInject(R.id.item_category_name_3)
    TextView		mTvName3;

    private Context mContext;
    public CategoryItemViewHold(Context context, @NonNull View itemView) {
        super(itemView);
        mContext = context;
        x.view().inject(this,itemView);
    }

    @Override
    public void setData(CategoryInfoBean t) {
        setData(mTvName1,mIvIcon1,t.name1,t.url1);
        setData(mTvName2,mIvIcon2,t.name2,t.url2);
        setData(mTvName3,mIvIcon3,t.name3,t.url3);
    }

    public void setData(TextView tv, ImageView iv, final String name, String url) {
        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(url)){
            tv.setText(name);

            iv.setImageResource(R.drawable.ic_default);
            BitmapUtilFactory.getBitmapUtils(mContext).display(iv, Constant.URlS.IMAGEBASEURL + url);
//            x.image().bind(iv, Constant.URlS.IMAGEBASEURL + url);

            ViewGroup parent = (ViewGroup) tv.getParent();
            parent.setVisibility(View.VISIBLE);

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, name, 0).show();
                }
            });

        }else{
            ViewGroup parent = (ViewGroup) tv.getParent();
            parent.setVisibility(View.INVISIBLE);
        }
    }
}
