package md.sancov.kform

class Registry<Type: RowType> {
    private val registrations = mutableSetOf<Type>()

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