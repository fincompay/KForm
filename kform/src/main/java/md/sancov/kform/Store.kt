package md.sancov.kform

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import androidx.core.os.bundleOf
import androidx.core.util.*
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import md.sancov.kform.model.EnumModel
import md.sancov.kform.model.toEnum
import kotlin.Pair
import kotlin.reflect.KClass

@OptIn(ExperimentalCoroutinesApi::class)
data class Store<Type: RowType>(private val state: SavedStateHandle) {
    companion object {
        private const val BUNDLE_VALUES = "STORE_VALUES"
    }

    val flows = SparseArray<MutableStateFlow<Any?>>()

    init {
        state.get<Bundle>(BUNDLE_VALUES)?.let { bundle ->
            bundle.keySet().forEach {
                val id = it.toIntOrNull() ?: return@forEach

                Log.d("STORE", "RESTORE = $it with: ${bundle[it]}")

                flowById<Any>(id).value = bundle[it]
            }
        }

        state.setSavedStateProvider(BUNDLE_VALUES) {
            val pairs = mutableListOf<Pair<String, Any?>>()

            flows.forEach { key, flow ->
                val value = flow.value

                Log.d("STORE", "SAVE = $key value = $value")

                pairs.add(Pair(key.toString(), value.toString()))
            }

            bundleOf(*pairs.toTypedArray())
        }
    }

    operator fun<Value> set(type: Type, value: Value?) {
        flowByType<Value>(type).value = value
    }

    fun<Value> set(type: Type, block: (previous: Value?) -> Value?) {
        val data = block(get(type))
        set(type, data)
    }


    operator fun<Value> get(type: Type): Value? {
        return flowByType<Value>(type).value
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

    inline fun<reified Value: Enum<Value>> enum(type: Type): Value? {
        return get<EnumModel>(type)?.toEnum<Value>()
    }
    inline fun<reified Value: Enum<Value>> enum(type: Type, default: Value): Value {
        return enum<Value>(type) ?: default
    }

    fun contains(type: Type): Boolean {
        return flowByType<Any>(type).value != null
    }

    fun collect(vararg types: Type): Flow<Type> {
        val flows = types
            .map { type ->
                flowByType<Any>(type).map { type }
            }
            .toTypedArray()

        return merge(*flows)
    }

    fun collect(clazz: KClass<out Type>): Flow<Type> {
        TODO()
//        val types = types.filterIsInstance(clazz.java)
//
//        val flows = types
//            .map { type ->
//                flow<Any>(type).map { type  }
//            }
//            .toTypedArray()
//
//        return merge(*flows)
    }

    fun clear() {
        flows.keyIterator().forEach {
            flows[it].value = null
        }
    }

    fun<Value> flowByType(type: Type): MutableStateFlow<Value?> {
        return flowById(type.id)
    }

    @Suppress("UNCHECKED_CAST")
    fun<Value> flowById(id: Int): MutableStateFlow<Value?> {
        if (!flows.containsKey(id)) {
            flows[id] = MutableStateFlow(null)
        }

        return flows[id] as MutableStateFlow<Value?>
    }
}