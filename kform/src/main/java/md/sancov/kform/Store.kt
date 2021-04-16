package md.sancov.kform

import android.os.Bundle
import android.util.SparseArray
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

data class Store<Type: RowType>(private val state: SavedStateHandle) {
    companion object {
        private const val BUNDLE_VALUES = "STORE_VALUES"
    }

    val registry = Registry<Type>(state)
    val flows = SparseArray<MutableStateFlow<Any?>>()

    init {
        state.get<Bundle>(BUNDLE_VALUES)?.let { bundle ->
            bundle.keySet().forEach {
//                registry.getById(it.toInt())
//                set(it.toInt(), bundle[it])
            }
        }

//        state.setSavedStateProvider(BUNDLE_VALUES) {
////            val pairs = mutableListOf<Pair<String, Any?>>()
////
////            flows.forEach { key, flow ->
////                pairs.add(Pair(key.toString(), flow.value))
////            }
////
////            bundleOf(*pairs.toTypedArray())
//        }
    }

    fun collect(vararg types: Type): Flow<Unit> {
        val tmp = types
            .map { flow<Any>(it) }
            .toTypedArray()

        return combine(*tmp) {}
    }

    @Suppress("UNCHECKED_CAST")
    fun<Value> flow(type: Type): MutableStateFlow<Value?> {
        val key = type.id

        if (!flows.containsKey(key)) {
            flows[key] = MutableStateFlow(null)
        }

        return flows[key] as MutableStateFlow<Value?>
    }
}