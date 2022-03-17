package androidx.swift.mvp;

import androidx.lifecycle.LifecycleOwner;
import androidx.swift.rxjava3.paging.IPageableObservable;

import java.util.List;

/**
 * 分页请求
 * @author rae
 * @date 2021/06/14
 */
public interface IPageView<T> extends LifecycleOwner {

    /**
     * 加载数据
     * @param IPageable 分页
     * @param data 数据
     */
    void onLoadData(IPageableObservable<T> IPageable, List<T> data);

    /**
     * 加载数据错误
     * @param pageable 分页
     * @param message 错误消息
     */
    void onLoadDataError(IPageableObservable<T> pageable, String message);

    /**
     * 没有更多数据
     * @param pageable 分页
     */
    void onNoMoreData(IPageableObservable<T> pageable);

}
