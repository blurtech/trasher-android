package tech.blur.trasher.common.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.observe(
        owner: LifecycleOwner,
        crossinline observer: (T?) -> Unit
) {
    observe(owner, Observer<T> { v -> observer(v) })
}

inline fun <T> LiveData<T>.observeNonNull(
        owner: LifecycleOwner,
        crossinline observer: (T) -> Unit
) {
    this.observe(owner, Observer {
        if (it != null) {
            observer(it)
        }
    })
}

inline fun <T> LiveData<T>.observeOnce(
        crossinline observer: (T?) -> Unit
) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T?) {
            observer(t)
            removeObserver(this)
        }
    })
}