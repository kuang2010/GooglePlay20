package com.kuang2010.googleplay20.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

/**
 * author: kuangzeyu2019
 * date: 2020/6/5
 * time: 16:40
 * 1. 内存获取数据 2. 本地磁盘获取数据 cache 3. 网络获取数据
 *
 * desc: 仿照glide用三级缓存实现显示网络图片，并使用线程池优化多线程访问网络
 *
 */
public class BitmapUtil {

    private Context mContext;
    private LruCache<String,Bitmap> mLruCache ;
    private final ExecutorService mThreadPool;

    public BitmapUtil(Context context){
        mContext = context;

        //LruCache内部使用了软引用对象SoftReference，使用方式同map集合
        mLruCache = new LruCache<String,Bitmap>((int) (Runtime.getRuntime().freeMemory()/2)){//可用内存的一半做为缓存容器大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();//容器中每张图片的字节大小
            }
        };

        //线程池，创建了6条线程。线程池里的线程执行完任务后在不调用threadPool.shutdown()时是不会销毁的，
        //当要开始的任务的个数大于线程池里线程的个数时，里面的线程是可以复用的，如里面的6个线程执行完6个任务后，会复用其中的某些线程去执行剩余的任务。当任务的个数小于线程的个数时，多出的线程不执行任务，会造成一点点资源的浪费
        mThreadPool = Executors.newFixedThreadPool(6);
    }


    private Map<ImageView,String> iv_url = new HashMap<>();//将imageView与url绑定在一起，解决多线程中网速慢滑动快时由于listview缓存复用ImageView造成图片显示错位的问题

    /**
     * 显示网络图片，该方法在listview等等中会被调用多次，多条线程可能同时存在，多线程
     * @param iv
     * @param url
     */
    public void display(ImageView iv,String url){

        //从内存中取
        Bitmap bitmap = mLruCache.get(Md5Util.md5Encode(url));
        if (bitmap!=null){
            displayBitMap(bitmap,iv);
//            Log.d("tagtag","从内存中获取图片");
            return;
        }

        bitmap  = BitmapFactory.decodeFile(getLocalPath(url).getAbsolutePath());
        if (bitmap!=null){
            displayBitMap(bitmap,iv);
//            Log.d("tagtag","从本地磁盘中获取图片");
            saveBitmap2Memery(url,bitmap);
            return;
        }

        //从网络中获取图片
        iv_url.put(iv,url);

//        new Thread(new DisPlayRunnable(iv,url),url.substring(url.lastIndexOf("/")+1)).start();
        mThreadPool.submit(new DisPlayTask(iv,url));
    }


    private class DisPlayTask implements Runnable {

        private ImageView mIv;
        private String mUrl;

        public DisPlayTask(ImageView iv, String url) {
            mIv = iv;
            mUrl = url;
        }

        @Override
        public void run() {
            try {
                String threadName = Thread.currentThread().getName();
//                Log.d("BitmapUtil","线程"+threadName+"正在下载图片");
                URL url = new URL(mUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                int code = connection.getResponseCode();
                if (code == 200){
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    String ivUrl = iv_url.get(mIv);
                    if (mUrl.equals(ivUrl)){
                        displayBitMap(bitmap,mIv);
//                        Log.d("tagtag","从网络中获取图片");
                    } else {
                        //网速慢造成的图片错位 不显示图片
                    }
                    saveBitmap2Memery(mUrl,bitmap);

                    saveBitmap2Local(mUrl,bitmap);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存图片到本地
     * @param url
     * @param bitmap
     */
    private void saveBitmap2Local(String url, Bitmap bitmap) throws FileNotFoundException {
        File localPath = getLocalPath(url);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(localPath));
    }

    private File getLocalPath(String url) {
        File cacheDir = mContext.getCacheDir();
        File file = new File(cacheDir,Md5Util.md5Encode(url)+"."+url.substring(url.lastIndexOf(".")+1));
        return file;
    }

    /**
     * 保存图片到内存
     * @param url
     * @param bitmap
     */
    private void saveBitmap2Memery(String url, Bitmap bitmap) {
        String md5Url = Md5Util.md5Encode(url);
        mLruCache.put(md5Url,bitmap);
    }

    private synchronized void displayBitMap(Bitmap bitmap, ImageView iv) {
        if (Looper.myLooper() == Looper.getMainLooper()){
            //主线程
            iv.setImageBitmap(bitmap);
        }else {
            Message message = mHandler.obtainMessage();
            Ob ob = new Ob();
            ob.bitmap = bitmap;
            ob.iv = iv;
            message.obj = ob;
            mHandler.sendMessage(message);
        }

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Ob ob = (Ob) msg.obj;
            Bitmap bitmap = ob.bitmap;
            ImageView iv = ob.iv;
            iv.setImageBitmap(bitmap);
        }
    };

    class Ob{
        public Bitmap bitmap;
        public ImageView iv;
    }
}
