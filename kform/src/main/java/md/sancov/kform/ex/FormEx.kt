package md.sancov.kform.ex

import md.sancov.kform.Form
import md.sancov.kform.RowType

fun<Type: RowType, Value> Form<Type>.get(type: Type, default: Value): Value {
    TODO()
//    return store.get(type, default)
}

operator fun<Type: RowType, Value> Form<Type>.get(type: Type): Value? {
//    return store[type]
    TODO()
}

operator fun<Type: RowType, Value> Form<Type>.set(type: Type, data: Value?) {
//    store[type] = data
}

inline fun<Type: RowType, reified Value> Form<Type>.get(where: (Value) -> Boolean): Value? {
    TODO()
//    return store.get(where)
}

inline fun<Type: RowType, reified Value> Form<Type>.getAll(where: (Value) -> Boolean): List<Value> {
    TODO()
//    return store.getAll(where)
}