package md.sancov.kform

import android.os.Bundle
import android.util.SparseArray
import androidx.core.util.containsKey
import androidx.core.util.set
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.reflect.KClass

// default params

@OptIn(ExperimentalCoroutinesApi::class)
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

    fun collect(vararg types: Type): Flow<Type> {
        val flows = types
            .map { type ->
                flow<Any>(type).map { type }
            }
            .toTypedArray()

        return merge(*flows)
    }

    fun collect(clazz: KClass<out Type>): Flow<Type> {
        val types = registry.types.filterIsInstance(clazz.java)

        val flows = types
            .map { type ->
                flow<Any>(type).map { type  }
            }
            .toTypedArray()

        return merge(*flows)
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