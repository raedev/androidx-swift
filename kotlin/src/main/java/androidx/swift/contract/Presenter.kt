package androidx.swift.contract

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Presenter 业务处理
 * @author RAE
 * @date 2022/08/04
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
abstract class Presenter<V : PresenterView>(protected val view: V) {

    val context: Context
        get() {
            return checkNotNull(view.getContext())
        }

    init {
        view.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> onCreated()
                    Lifecycle.Event.ON_START -> onStart()
                    Lifecycle.Event.ON_RESUME -> onResume()
                    Lifecycle.Event.ON_PAUSE -> onPause()
                    Lifecycle.Event.ON_STOP -> onStop()
                    Lifecycle.Event.ON_DESTROY -> {
                        onDestroy()
                        view.lifecycle.removeObserver(this)
                    }
                    else -> return
                }
            }
        })
    }

    protected open fun onCreated() = Unit
    protected open fun onStart() = Unit
    protected open fun onResume() = Unit
    protected open fun onPause() = Unit
    protected open fun onStop() = Unit
    protected open fun onDestroy() = Unit

}