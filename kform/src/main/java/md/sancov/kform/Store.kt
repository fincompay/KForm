package md.sancov.kform

import android.os.Bundle
import android.util.SparseArray
import androidx.core.os.bundleOf
import androidx.core.util.*
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.Pair
import kotlin.reflect.KClass

@OptIn(ExperimentalCoroutinesApi::class)
data class Store<Type: RowType>(private val state: SavedStateHandle) {
    companion object {
        private const val BUNDLE_VALUES = "STORE_VALUES"
    }

    val registry = Registry<Type>()
    val flows = SparseArray<MutableStateFlow<Any?>>()

    init {
        state.get<Bundle>(BUNDLE_VALUES)?.let { bundle ->
            bundle.keySet().forEach {
                val type = registry.getById(it.toInt()) ?: return@forEach
                set(type, bundle[it])
            }
        }

        state.setSavedStateProvider(BUNDLE_VALUES) {
            val pairs = mutableListOf<Pair<String, Any?>>()

            flows.forEach { key, value ->
                pairs.add(Pair(key.toString(), value.toString()))
            }

            bundleOf(*pairs.toTypedArray())
        }
    }

    operator fun<Value> set(type: Type, value: Value?) {
        flow<Value>(type).value = value
    }

    fun<Value> set(type: Type, block: (previous: Value?) -> Value?) {
        val data = block(get(type))
        set(type, data)
    }


    operator fun<Value> get(type: Type): Value? {
        return flow<Value>(type).value
    }

    fun<Value> get(type: Type, default: Value): Value {
        return get(type) ?: default
    }

    inline fun<reified Value> get(where: (Value) -> Boolean): Value? {
        return getAll(where).firstOrNull(where)
    }

    inline fun<reified Value> getAll(where: (Value) -> Boolean): List<Value> {
        val tmp = mutableListOf<Any>()

        flows.valueIterator().forEach { flow ->
            flow.value?.let { tmp.add(it) }
        }

        return tmp.filterIsInstance(Value::class.java).filter(where)
    }

    fun contains(type: Type): Boolean {
        return flow<Any>(type).value != null
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

    fun reset() {
        flows.keyIterator().forEach {
            flows[it].value = null
        }
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