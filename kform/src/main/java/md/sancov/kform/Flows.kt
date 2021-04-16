package md.sancov.kform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach

data class Flows<Type: RowType>(private val store: Store<Type>) {
    private val flows = mutableListOf<Flow<Unit>>()

    fun subscribe(vararg types: Type, onEach: suspend (store: Store<Type>) -> Unit = {}) {
        val flow = store.collect(*types).onEach {
            onEach(store)
        }
        flows.add(flow)
    }

    internal fun combine() = combine(flows) {}
}
