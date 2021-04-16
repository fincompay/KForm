package md.sancov.kform.row

import md.sancov.kform.RowType

interface Row {
    val type: RowType

    override fun equals(other: Any?): Boolean
}

interface RowClickable: Row