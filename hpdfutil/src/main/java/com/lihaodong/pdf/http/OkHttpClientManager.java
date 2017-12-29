package com.lihaodong.pdf.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpClientManager
{
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;



    private OkHttpClientManager()
    {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClientManager getInstance()
    {
        if (mInstance == null)
        {
            synchronized (OkHttpClientManager.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }


    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private void _downloadAsyn(final String url, final String fileName, final String destFileDir, final ResultCallback callback)
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(final Request request, final IOException e)
            {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response)
            {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int sum = 0;
                int len = 0;
                FileOutputStream fos = null;
                try
                {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    String savePath = isExistDir(destFileDir);
                    File file = new File(savePath, getFileName(url,fileName));
                    fos = new FileOutputStream(file);
                    int a = 0;
                    while ((len = is.read(buf)) != -1)
                    {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        if (a!=progress) {
                            a= progress;
                            setDownloadingResultCallback(progress, callback);
                        }
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file, callback);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    sendFailedStringCallback(response.request(), e, callback);
                } finally
                {
                    try
                    {
                        if (is != null) is.close();
                    } catch (IOException e)
                    {
                        sendFailedStringCallback(response.request(), e, callback);
                    }
                    try
                    {
                        if (fos != null) fos.close();
                    } catch (IOException e)
                    {
                        sendFailedStringCallback(response.request(), e, callback);
                    }
                }

            }
        });
    }
    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    private String getFileName(String path,String fileName)
    {
        if(TextUtils.isEmpty(fileName)){
            int separatorIndex = path.lastIndexOf("/");
            return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
        }else{
            return fileName;
        }

    }

    public static void downloadAsyn(String url, String fileName,String destDir, ResultCallback callback)
    {
        getInstance()._downloadAsyn(url, fileName,destDir, callback);
    }

    //****************************

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback)
    {
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final File file, final ResultCallback callback)
    {
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (callback != null)
                {
                    callback.onResponse(file);
                }
            }
        });
    }
    private void setDownloadingResultCallback(final int object, final ResultCallback callback)
    {
        mDelivery.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (callback != null)
                {
                    callback.onDownloading(object);
                }
            }
        });
    }

    public static abstract class ResultCallback
    {

        public ResultCallback()
        {
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(File file);
        public abstract void onDownloading(int progress);
    }


}

