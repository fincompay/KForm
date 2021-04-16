package md.sancov.kform.binder

import md.sancov.kform.RowType
import md.sancov.kform.Store
import md.sancov.kform.row.Row

interface Binder<Type: RowType> {
    fun resolve(type: Type, store: Store<Type>): Row
}