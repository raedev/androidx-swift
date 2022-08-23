package androidx.swift.contract

import android.content.Context
import androidx.lifecycle.LifecycleOwner

/**
 * 回调视图
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
interface PresenterView : LifecycleOwner {

    fun getContext(): Context?

}