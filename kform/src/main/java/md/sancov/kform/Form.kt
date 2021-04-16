package md.sancov.kform

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.*
import md.sancov.kform.row.Row
import md.sancov.utils.State
import java.util.*

typealias RowsState = State<List<Row>>

class Form<T : RowType> {
    private val reset = MutableStateFlow<Date?>(null)
    private val refresh = MutableStateFlow<Date?>(null)

    private var source: DataSource<T>? = null


    // on reset -> clear
    // |
    // v

    val items: Flow<RowsState> = reset
        .filterNotNull()
        .mapNotNull { source }
        .transform { source ->
            Log.i("FORM","ACTION: RESET")

            Log.i("FORM","STATE: EMIT LOADING")

            emit(State.Loading<List<Row>>())

            Log.i("FORM","ACTION: ON SETUP")
//            source.onSetup(this@Form)

            Log.i("FORM","ACTION: BINDINGS")

//            source.binder.resolve()

            // source setup

                // val binder = source.binder

                // val listeners = source.listeners

                // val triggers = source.triggers

                //val rows = flows.filterNotNull().merge().map {
                // val types = source.types
//                     val rows = types.invoke(store).map { binder.resolve(it, store) }
//                     State.Success(rows)
//               }



                Log.i("FORM","STATE: EMIT SUCCESS")
                emit(State.Success(emptyList()))

//            emit(State.Loading())
//            store.reset()

//            source.de

                // listeners = Unit
                // triggerables = refresh / triggers = emit(State.Success(rows))
            }
            .catch {
//                emit(State.Error(it))
            }

    fun setDataSource(source: DataSource<T>) {
        this.source = source

//        reset()
    }

    fun refresh() {
        refresh.value = Date()
    }

    fun reset() {
        reset.value = Date()
    }
}