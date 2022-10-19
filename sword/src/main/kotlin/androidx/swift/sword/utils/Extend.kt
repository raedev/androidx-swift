/**
 * 扩展方法
 * @author RAE
 * @date 2022/08/22
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
package androidx.swift.sword.utils

import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * 绑定到UI生命周期
 */
fun <T : Any> Observable<T>.uiThread(owner: LifecycleOwner): Observable<T> {
    return this.compose(UiObservableTransformer(owner))
}

/**
 * 绑定到UI生命周期
 */
fun <T : Any> Observable<T>.mainThread(): Observable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}