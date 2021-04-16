package md.sancov.kform

import android.os.Bundle
import android.util.SparseArray
import androidx.core.os.bundleOf
import androidx.core.util.containsKey
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow

data class Store<Type: RowType>(val state: SavedStateHandle) {
    companion object {
        private const val BUNDLE_VALUES = "STORE_VALUES"
    }

//    val registry = Registry<T>()
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


    fun<Value> flow(type: Type): MutableStateFlow<Value?> {
        TODO()
//        if (!flows.containsKey(key)) {
//            flows[key] = MutableStateFlow(null)
//        }
//        return flows[key] as MutableStateFlow<T?>
    }
}