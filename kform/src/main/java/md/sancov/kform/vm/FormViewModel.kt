package md.sancov.kform.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import md.sancov.kform.Form
import md.sancov.kform.FormAdapter
import md.sancov.kform.RowType
import md.sancov.kform.RowsState
import md.sancov.kform.model.EnumModel
import md.sancov.kform.model.toEnum

abstract class FormViewModel<Type: RowType>: ViewModel() {
    private val form = Form<Type>()

    private val _rows = MutableStateFlow<RowsState?>(null)

    val rows = _rows

    init {
        viewModelScope.launch(Dispatchers.IO) {
            form.rows.collect {
                _rows.value = it
            }
        }
    }

    fun<T: FormAdapter<Type>> set(adapter: T, lambda: T.() -> Unit = {}) {
        form.replaceAdapter(adapter.apply(lambda))
    }

    fun refresh() {
        form.refresh()
    }

    fun reload(keepState: Boolean = true) {
        form.reload(keepState)
    }

    fun<Value> set(type: Type, value: Value?) {
        form[type] = value
    }

    fun<Value> set(type: Type, lambda: suspend () -> Value?) = viewModelScope.launch(Dispatchers.IO) {
        val value = try {
            lambda()
        } catch (_: Throwable) {
            null
        }

        form.also {
            it[type] = value
            it.refresh()
        }
    }

    fun<Value> value(type: Type): Value? {
        return form[type]
    }

    fun<Value> value(type: Type, default: Value): Value {
        return form[type] ?: default
    }

    inline fun<reified Value: Enum<Value>> enum(type: Type): Value? {
        return value<EnumModel>(type)?.toEnum<Value>()
    }
    inline fun<reified Value: Enum<Value>> enum(type: Type, default: Value): Value {
        return enum<Value>(type) ?: default
    }
}