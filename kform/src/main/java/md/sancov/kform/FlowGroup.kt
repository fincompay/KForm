package md.sancov.kform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.reflect.KClass

data class FlowGroup<Type: RowType>(private val store: Store<Type>) {
    private val flows = mutableListOf<Flow<Unit>>()

    fun subscribe(vararg types: Type, onEach: suspend (type: Type, store: Store<Type>) -> Unit = { _, _ -> }) {
        val flow = store.collect(*types)
            .onEach { onEach(it, store) }
            .map {  }

        flows.add(flow)
    }

    fun subscribe(clazz: KClass<out Type>, onEach: suspend (type: Type, store: Store<Type>) -> Unit = { _, _ -> }) {
        val flow = store.collect(clazz)
            .onEach { onEach(it, store) }
            .map {  }

        flows.add(flow)
    }

    internal fun combine() = combine(flows) {}
}
