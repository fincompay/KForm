package md.sancov.kform

import androidx.lifecycle.SavedStateHandle

class Registry<Type: RowType>(state: SavedStateHandle) {
    companion object {
//        private const val BUNDLE_REGISTRATIONS = "BUNDLE_REGISTRY"
    }

    private val registrations = mutableSetOf<Type>()

    init {
//        state.get<Bundle>(BUNDLE_VALUES)?.let { bundle ->
//            bundle.keySet().forEach {
////                registry.getById(it.toInt())
////                set(it.toInt(), bundle[it])
//            }
//        }

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

    fun getById(id: Int): Type? {
        return registrations.firstOrNull { it.id == id }
    }

    fun register(vararg types: Type) {
        registrations.addAll(types)
    }

    fun clear() {
        registrations.clear()
    }
}