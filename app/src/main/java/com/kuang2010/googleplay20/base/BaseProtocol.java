package com.kuang2010.googleplay20.base;

import android.text.TextUtils;
import android.util.Log;

import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.factory.GlobalFactory;
import com.kuang2010.googleplay20.util.FileUtil;
import com.kuang2010.googleplay20.util.IOUtils;
import com.kuang2010.googleplay20.util.Md5Util;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * author: kuangzeyu2019
 * date: 2020/6/13
 * time: 13:30
 * desc: MainActivity的接口协议
 */
public abstract class BaseProtocol {

    /**有更多数据的回调监听,用于判断是否要加载更多数据*/
    protected OnHasMoreDataListener mOnHasMoreDataListener;
    /**保存的数据对应的key*/
    private String mSavekey;

    public interface OnHasMoreDataListener {
        void setHasMoreData(boolean hasMoreData);
    }

    /**
     * 加载数据
     * @param index 分页加载的下标参数，即已加载完的数据个数
     */
    protected void loadData(int index) {// final BaseRvAdapter.ILoadMoreDataCallBack callBack
        String url = Constant.URlS.BASEURL + getInterceKey();//"home"

        mSavekey = generateSavekey(url, index);
        String result = getDataFromMemory();
        if (!TextUtils.isEmpty(result)){
            Log.d("BaseProtocol","###从内存加载数据--->"+mSavekey);
            BaseProtocol.this.onSuccess(result);
            return;
        }

        result = getDataFromLocal();
        if (!TextUtils.isEmpty(result)){
            Log.d("BaseProtocol","###从本地加载数据--->"+mSavekey);
            BaseProtocol.this.onSuccess(result);
            return;
        }

        RequestParams params = new RequestParams();
        params.setUri(url);

        params.addQueryStringParameter("index", index + "");
        params.setConnectTimeout(3000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseProtocol.this.onSuccess(result);
                /**保存一份到内存*/
                saveData2Memory(result);
                /**保存一份到本地*/
                saveData2Local(result);

               /* Gson gson = new Gson();
                HomeBean homeBean = gson.fromJson(result, HomeBean.class);
                if (homeBean==null){
//                    mHasMoreData = false;
                    setHasMoreData(false);
                    callBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_GONE);
                    return;
                }
                List<AppInfoBean> appInfoBeans = homeBean.list;
                if (appInfoBeans==null || appInfoBeans.size()==0){
//                    mHasMoreData = false;
                    setHasMoreData(false);
                    callBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_GONE);
                    return;
                }
//                mHasMoreData = true;
                setHasMoreData(true);
                callBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_LOAD_ERROR);
                callBack.setMoreDatas(appInfoBeans);*/
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //mHasMoreData保持上一次的状态就行
//                callBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_LOAD_ERROR);
                BaseProtocol.this.onError(ex,isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //mHasMoreData保持上一次的状态就行
//                callBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_LOAD_ERROR);
                BaseProtocol.this.onCancelled(cex);
            }

            @Override
            public void onFinished() {

            }
        });
    }



    private String generateSavekey(String url, int params) {
        return Md5Util.md5Encode(url)+getInterceKey()+"."+params;
    }

    private File getCachFile(){
        String cacheFileDir = FileUtil.getDir("json");
        String fileName = mSavekey;
        File cacheFile = new File(cacheFileDir,fileName);
        return cacheFile;
    }

    /**
     * 从本地获取数据
     * @return jsonString
     */
    private String getDataFromLocal() {
        /**
         if(文件存在){
             //读取插入时间
             //判断是否过期
             if(未过期){
                 //读取缓存内容
                 //Json解析解析内容
                 if(不为null){
                 //返回并结束
                 }
             }
         }
         */

        /**
         1.file
         2.sdcard/Android/data/包目录/cache/json/
         3.savekey+"."+index
         4.
         第一行:数据的生成时间(插入时间)
         第二行:协议的真正内容
         */
        File cachFile = getCachFile();
        if (cachFile.exists()){
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cachFile));
                //读取第一行 时间
                long saveTime = Long.parseLong(reader.readLine());
                if (System.currentTimeMillis() - saveTime < Constant.PROTOCOLTIMEOUT){
                    //未过期,有效
                    //读取后面几行... 协议内容
                    String jsonString = reader.readLine();
                    StringBuffer sb = new StringBuffer();

                    while (!TextUtils.isEmpty(jsonString)){
                        sb.append(jsonString);
                        jsonString = reader.readLine();
                        //sb.append(jsonString);//错,要判空null,否则末尾多出个null
                    }

                    //保存一份到内存
                    saveData2Memory(sb.toString());

                    return sb.toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
                IOUtils.close(reader);
            }
        }
        return null;
    }

    /**
     * 保存数据到本地，缓存数据要保证索引的唯一性
     * @param result jsonString
     */
    private void saveData2Local(String result) {
        File cachFile = getCachFile();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(cachFile));
            //写第一行 时间
            writer.write(""+System.currentTimeMillis());
            //换行
            writer.newLine();//"\r\n"
            //写数据,读取时注意result里本身可能有换行，所以要循环读取
            writer.write(result);

            writer.flush();
            Log.d("BaseProtocol","保存数据到本地成功："+cachFile.getAbsolutePath());

        } catch (IOException e) {
            IOUtils.close(writer);
            e.printStackTrace();
        }
    }


    /**
     * 从内存获取数据
     * @return jsonString
     */
    private String getDataFromMemory() {
        return GlobalFactory.getStrHashMap().get(mSavekey);
    }

    /**
     * 保存数据到内存,缓存数据要保证索引的唯一性
     * @param result jsonString
     */
    private void saveData2Memory(String result) {
        GlobalFactory.getStrHashMap().put(mSavekey,result);
    }

    /**
     * 子类调用，设置是否还可以加载更多数据
     * @param hasMoreData 是否还有更多数据
     */
    protected void setHasMoreData(boolean hasMoreData) {
        if (mOnHasMoreDataListener !=null){
            mOnHasMoreDataListener.setHasMoreData(hasMoreData);
        }
    }


    /**
     * 获取区分协议接口url的关键字
     * @return 区分协议接口url的关键字
     */
    protected abstract String getInterceKey();

    protected abstract void onCancelled(Callback.CancelledException cex);

    protected abstract void onError(Throwable ex, boolean isOnCallback);

    protected abstract void onSuccess(String result);
}
