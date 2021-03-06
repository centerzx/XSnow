package com.vise.netexpand.request;

import android.content.Context;

import com.vise.netexpand.func.ApiResultFunc;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheResult;
import com.vise.xsnow.http.subscriber.ApiCallbackSubscriber;

import java.lang.reflect.Type;

import io.reactivex.Observable;

/**
 * @Description: 返回APIResult的GET请求类
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 17/5/13 14:31.
 */
public class ApiGetRequest extends ApiBaseRequest {
    @Override
    protected <T> Observable<T> execute(Type type) {
        return apiService.get(suffixUrl, params).map(new ApiResultFunc<T>(type)).compose(this.<T>apiTransformer());
    }

    @Override
    protected <T> Observable<CacheResult<T>> cacheExecute(Type type) {
        return this.<T>execute(type).compose(ViseHttp.getInstance().getApiCache().<T>transformer(cacheMode, type));
    }

    @Override
    protected <T> void execute(Context context, ACallback<T> callback) {
        if (isLocalCache) {
            this.cacheExecute(getSubType(callback)).subscribe(new ApiCallbackSubscriber(context, callback));
        }
        this.execute(getType(callback)).subscribe(new ApiCallbackSubscriber(context, callback));
    }
}
