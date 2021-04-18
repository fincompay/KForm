package md.sancov.kform

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import md.sancov.kform.row.Row
import md.sancov.utils.State
import java.util.*

typealias RowsState = State<List<Row>>

class Form<Type : RowType> {
    private val reset = MutableStateFlow<Date?>(null)
    private val refresh = MutableStateFlow<Date?>(null)

    lateinit var source: FormAdapter<Type>

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: Flow<RowsState> = reset
        .filterNotNull()
        .transform {
            Log.i("FORM","ACTION: RESET")

            Log.i("FORM","STATE: EMIT LOADING")
            emit(State.Loading<List<Row>>())

            Log.i("FORM","ACTION: ON PREPARE")
            source.prepare.invoke()

            val store = source.store
            val binder = source.binder

            val rows = merge(refresh.map { }, source.triggers).map {
                val types = source.types()

                Log.i("FORM","STATE: EMIT SUCCESS types = $types")
//
                val rows = types.map { binder.resolve(it, store) }

                State.Success(rows)
            }

            emitAll(rows)
        }
        .catch {
            emit(State.Error(it))
        }

    fun setAdapter(source: FormAdapter<Type>) {
        this.source = source
        reset()
    }

    fun refresh() {
        refresh.value = Date()
    }

    fun reset() {
        reset.value = Date()
    }

    fun<Value> get(type: Type, default: Value): Value {
        return source.store.get(type, default)
    }

    operator fun<Value> get(type: Type): Value? {
        return source.store[type]
    }

    operator fun<Value> set(type: Type, data: Value?) {
        source.store[type] = data
    }


//    inline fun<reified Value> get(where: (Value) -> Boolean): Value? {
//        return source.store.get(where)
//    }
//
//    inline fun<reified Value> getAll(where: (Value) -> Boolean): List<Value> {
//        return source.store.getAll(where)
//    }
}