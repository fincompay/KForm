package md.sancov.kform

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.parcelize.Parcelize
import md.sancov.kform.model.EnumModel
import md.sancov.kform.model.toEnum
import kotlin.collections.set
import kotlin.reflect.KClass

@OptIn(ExperimentalCoroutinesApi::class)
data class Store<Type : RowType>(private val state: SavedStateHandle) {
    companion object {
        private const val KEY_BUNDLE = "STORE_BUNDLE"
        private const val KEY_PAIRS = "KEY_PAIRS"
    }

    val types get(): List<Type> {
        return flows.keys.toList()
    }

    val flows = mutableMapOf<Type, MutableStateFlow<Parcelable?>>()

    init {
        state.get<Bundle>(KEY_BUNDLE)?.let { bundle ->
            val values: List<StorePair<Type, Parcelable>> = bundle.getParcelable(KEY_PAIRS) ?: return@let

            Log.v("STORE", "RESTORE $values")

            values.forEach {
                flowByType<Parcelable>(it.type).value = it.value
            }
        }

        state.setSavedStateProvider(KEY_BUNDLE) {
            val pairs = flows.map {
                StorePair(it.key, it.value.value)
            }

            Log.v("STORE", "SAVING PAIRS $pairs")

            bundleOf(KEY_PAIRS to pairs)
        }
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <Value: Parcelable> flowByType(type: Type): MutableStateFlow<Value?> {
        if (!flows.containsKey(type)) {
            flows[type] = MutableStateFlow(null)
        }

        return flows[type] as MutableStateFlow<Value?>
    }

    operator fun <Value: Parcelable> set(type: Type, value: Value?) {
        flowByType<Value>(type).value = value
    }

    operator fun <Value: Parcelable> get(type: Type): Value? {
        return flowByType<Value>(type).value
    }

    fun <Value: Parcelable> set(type: Type, block: (previous: Value?) -> Value?) {
        val data = block(get(type))
        set(type, data)
    }

    fun <Value: Parcelable> setIfNull(type: Type, value: Value?) {
        val flow = flowByType<Value>(type)
        if (flow.value == null) {
            flow.value = value
        }
    }

    fun <Value: Parcelable> get(type: Type, default: Value): Value {
        return get(type) ?: default
    }

    fun contains(type: Type): Boolean {
        return flowByType<Parcelable>(type).value != null
    }

    fun collect(vararg types: Type): Flow<Type> {
        val flows = types
            .map { type ->
                flowByType<Parcelable>(type).map { type }
            }
            .toTypedArray()

        return merge(*flows)
    }

    fun collect(clazz: KClass<out Type>): Flow<Type> {
        val types = flows.keys.filterIsInstance(clazz.java)

        val flows = types
            .map { type ->
                flowByType<Parcelable>(type).map { type }
            }
            .toTypedArray()

        return merge(*flows)
    }

    fun clear() {
        flows.forEach {
            it.value.value = null
        }
    }

    inline fun <reified Value: Parcelable> get(where: (Value) -> Boolean): Value? {
        return getAll(where).firstOrNull()
    }

    inline fun <reified Value: Parcelable> getAll(where: (Value) -> Boolean): List<Value> {
        val tmp = mutableListOf<Any>()

        flows.values.forEach { flow ->
            flow.value?.let { tmp.add(it) }
        }

        return tmp.filterIsInstance(Value::class.java).filter(where)
    }

    inline fun <reified Value: Parcelable> getType(where: (Value) -> Boolean): Type? {
        return getAllTypes(where).firstOrNull()
    }

    inline fun <reified Value: Parcelable> getAllTypes(where: (Value) -> Boolean): List<Type> {
        val types = mutableListOf<Type>()

        flows.forEach { entry ->
            val value = entry.value.value

            if (value is Value && where(value)) {
                types.add(entry.key)
            }
        }

        return types
    }

    inline fun <reified Value : Enum<Value>> enum(type: Type): Value? {
        return get<EnumModel>(type)?.toEnum<Value>()
    }

    inline fun <reified Value : Enum<Value>> enum(type: Type, default: Value): Value {
        return enum<Value>(type) ?: default
    }

    @Parcelize
    private data class StorePair<Type: RowType, Value: Parcelable>(val type: Type, val value: Value?): Parcelable
}