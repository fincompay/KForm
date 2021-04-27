package md.sancov.kform

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import md.sancov.kform.model.EnumModel
import md.sancov.kform.model.toEnum
import kotlin.collections.set
import kotlin.reflect.KClass

@OptIn(ExperimentalCoroutinesApi::class)
data class Store<Type : RowType>(private val state: SavedStateHandle) {
    companion object {
        private const val KEY_BUNDLE = "STORE_BUNDLE"
        private const val KEY_PAIRS = "KEY_PAIRS"

        private const val KEY_TYPE = "BUNDLE_KEY_TYPE"
        private const val KEY_VALUES = "BUNDLE_KEY_VALUES"
    }

    val types get(): List<Type> {
        return flows.keys.toList()
    }

    val flows = mutableMapOf<Type, MutableStateFlow<Any?>>()

    init {
        state.get<Bundle>(KEY_BUNDLE)?.let { bundle ->
            val values: List<Bundle> = bundle.getParcelableArrayList(KEY_PAIRS) ?: return@let

            values.forEach {
                val type = it.getParcelable<Type>(KEY_TYPE) as Type
                flowByType<Any>(type).value = it.get(KEY_VALUES)
            }
        }

        state.setSavedStateProvider(KEY_BUNDLE) {
            val bundles = flows.map {
                bundleOf(
                    KEY_TYPE to it.key,
                    KEY_VALUES to it.value.value
                )
            }

            bundleOf(KEY_PAIRS to bundles)
        }
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <Value> flowByType(type: Type): MutableStateFlow<Value?> {
        if (!flows.containsKey(type)) {
            flows[type] = MutableStateFlow(null)
        }

        return flows[type] as MutableStateFlow<Value?>
    }

    operator fun <Value> set(type: Type, value: Value?) {
        flowByType<Value>(type).value = value
    }

    operator fun <Value> get(type: Type): Value? {
        return flowByType<Value>(type).value
    }

    fun <Value> set(type: Type, block: (previous: Value?) -> Value?) {
        val data = block(get(type))
        set(type, data)
    }

    fun <Value> setIfNull(type: Type, value: () -> Value?) {
        val flow = flowByType<Value>(type)
        if (flow.value == null) {
            flow.value = value()
        }
    }

    fun <Value> get(type: Type, default: Value): Value {
        return get(type) ?: default
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

    fun collectByClazz(clazz: KClass<out Type>): Flow<Type> {
        val types = flows.keys.filterIsInstance(clazz.java)

        val flows = types
            .map { type ->
                flowByType<Any>(type).map { type }
            }
            .toTypedArray()

        return merge(*flows)
    }

    fun clear() {
        flows.forEach {
            it.value.value = null
        }
    }

    inline fun <reified Value> get(where: (Value) -> Boolean): Value? {
        return getAll(where).firstOrNull()
    }

    inline fun <reified Value> getAll(where: (Value) -> Boolean): List<Value> {
        val tmp = mutableListOf<Any>()

        flows.values.forEach { flow ->
            flow.value?.let { tmp.add(it) }
        }

        return tmp.filterIsInstance(Value::class.java).filter(where)
    }

    inline fun <reified Value> getType(where: (Value) -> Boolean): Type? {
        return getAllTypes(where).firstOrNull()
    }

    inline fun <reified Value> getAllTypes(where: (Value) -> Boolean): List<Type> {
        val types = mutableListOf<Type>()

        flows.forEach { entry ->
            val value = entry.value.value

            if (value is Value && where(value)) {
                types.add(entry.key)
            }
        }

        return types
    }

    inline fun <reified Value : Enum<Value>> getEnum(type: Type): Value? {
        return get<EnumModel>(type)?.toEnum<Value>()
    }

    inline fun <reified Value : Enum<Value>> getEnum(type: Type, default: Value): Value {
        return getEnum<Value>(type) ?: default
    }
}