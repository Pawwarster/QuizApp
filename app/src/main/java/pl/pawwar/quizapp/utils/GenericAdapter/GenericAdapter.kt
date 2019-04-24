package pl.pawwar.quizapp.utils.GenericAdapter

open class GenericAdapter(private vararg val binders: GenericViewBinder<*>) : GenericTypedAdapter<Any>(*binders)