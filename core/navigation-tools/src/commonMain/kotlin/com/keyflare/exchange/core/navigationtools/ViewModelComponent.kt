package com.keyflare.exchange.core.navigationtools

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.keyflare.common.utils.cast
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

open class ViewModelComponent<VM : ViewModel<*, *>>(
    componentContext: ComponentContext,
    viewModelFactory: (ComponentContext) -> VM,
    viewModelClass: KClass<out ViewModel<*, *>>,
): ComponentContext by componentContext {

    val viewModel: VM = componentContext.instanceKeeper
        .getOrCreate(viewModelClass) { viewModelFactory(this) }

    init {
        viewModel.viewModelScope.launch {
            viewModel.cast<ViewModel<*, *>>().updateContext(this@ViewModelComponent)
        }
    }

    companion object {
        inline fun <reified T : ViewModel<*, *>> with(
            componentContext: ComponentContext,
            noinline viewModelFactory: (ComponentContext) -> T,
        ): ViewModelComponent<T> {
            return ViewModelComponent(componentContext, viewModelFactory, T::class)
        }
    }
}
