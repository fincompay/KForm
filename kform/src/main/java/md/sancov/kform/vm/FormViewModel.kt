package md.sancov.kform.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import md.sancov.kform.Adapter
import md.sancov.kform.Form
import md.sancov.kform.RowType
import md.sancov.kform.RowsState

abstract class FormViewModel<Type: RowType>(state: SavedStateHandle): ViewModel() {
    val form = Form<Type>(state)

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

    fun<Value> set(type: Type, value: Value?) {
        form.also {
            it.store[type] = value
            it.refresh()
        }
    }

    fun<Value> set(type: Type, lambda: suspend () -> Value?) = viewModelScope.launch(Dispatchers.IO) {
        val value = try {
            lambda()
        } catch (_: Throwable) {
            null
        }

        set(type, value)
    }

    fun<Value> get(type: Type): Value? {
        return form.store[type]
    }

    fun<Value> get(type: Type, default: Value): Value {
        return form.store[type] ?: default
    }

    inline fun<reified Value> get(where: (Value) -> Boolean): Value? {
        return form.store.get(where)
    }

    inline fun<reified Value> getAll(where: (Value) -> Boolean): List<Value> {
        return form.store.getAll(where)
    }

    inline fun<reified Value: Enum<Value>> getEnum(type: Type): Value? {
        return form.store.getEnum<Value>(type)
    }

    inline fun<reified Value: Enum<Value>> getEnum(type: Type, default: Value): Value {
        return form.store.getEnum<Value>(type) ?: default
    }
}