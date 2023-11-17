package md.fincompay.kform.row

interface ValueRow<Params, Model>: Row {
    val params: Params
    val model: Model?
}