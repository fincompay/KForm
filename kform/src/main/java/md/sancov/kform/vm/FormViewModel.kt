package md.sancov.kform.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import md.sancov.kform.DataSource
import md.sancov.kform.Form
import md.sancov.kform.RowType
import md.sancov.kform.RowsState

abstract class FormViewModel<Type: RowType>: ViewModel() {
    private val form = Form<Type>()

    private val _rows = MutableStateFlow<RowsState?>(null)

    val rows = _rows
        .filterNotNull()
        .asLiveData(Dispatchers.IO)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            form.items.collect {
                _rows.value = it
            }
        }
    }

    fun setDataSource(dataSource: DataSource<Type>) {
        form.setDataSource(dataSource)
    }


//    fun<V> set(type: T, value: V?) {
//        form[type] = value
//    }
//
    fun<V> set(type: Type, lambda: suspend () -> V?) = viewModelScope.launch(Dispatchers.IO) {
//        val value = try {
//            lambda()
//        } catch (_: Throwable) {
//            null
//        }
//
//        form.also {
//            it[row] = value
//            it.refresh()
//        }
    }
//
//    fun<V> value(type: T): V? {
//        return form[type]
//    }
//
//    fun<V> value(type: T, default: V): V {
//        return form[type] ?: default
//    }
//
//    inline fun<reified V: Enum<V>> enum(type: T): V? {
//        return value<EnumModel>(type)?.toEnum<V>()
//    }
//
//    inline fun<reified V: Enum<V>> enum(type: T, default: V): V {
//        return enum<V>(type) ?: default
//    }


    fun refresh() {
        form.refresh()
    }

    fun reset() {
        form.reset()
    }
}