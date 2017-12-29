package com.lihaodong.pdf.http;


/**
 * Created by yaodetao on 2016/12/29.
 */

public interface HttpListener{

    void onFailed( Exception e);

    void onLoading(int progress);
}
