package md.sancov.kform.vm

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import md.sancov.kform.*
import md.sancov.kform.BinderAdapter

abstract class FormViewModel<Type: RowType>(state: SavedStateHandle): ViewModel() {
    private val form = Form<Type>(state)

    private val _rows = MutableStateFlow<RowsState?>(null)

    val rows = _rows.filterNotNull()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            form.rows.collect {
                _rows.value = it
            }
        }
    }

    fun<T: Adapter<Type>> set(adapter: T) {
        form.replaceAdapter(adapter)
    }

    fun refresh() {
        form.refresh()
    }

    fun reload(keepState: Boolean = true) {
        form.reload(keepState)
    }

    fun<Value: Parcelable> set(type: Type, value: Value?) {
        form.store[type] = value
    }

    fun<Value: Parcelable> set(type: Type, lambda: suspend () -> Value?) = viewModelScope.launch(Dispatchers.IO) {
        val value = try {
            lambda()
        } catch (_: Throwable) {
            null
        }

        form.also {
            it.store[type] = value
            it.refresh()
        }
    }

    fun<Value> value(type: Type): Value? {
        return form.store[type]
    }

    fun<Value> value(type: Type, default: Value): Value {
        return form.store[type] ?: default
    }

//    inline fun<reified Value: Enum<Value>> enum(type: Type): Value? {
//        return form.store.enum<Value>(type)
//    }
//    inline fun<reified Value: Enum<Value>> enum(type: Type, default: Value): Value {
//        return form.store.enum<Value>(type) ?: default
//    }
}