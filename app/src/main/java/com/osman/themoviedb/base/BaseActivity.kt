package com.osman.themoviedb.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
open class BaseActivity<VM : ViewModel> : DefaultBaseActivity {

    lateinit var viewModel: VM
    private val viewModelClass: Class<VM>

    constructor() : super() {
        this.viewModelClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    }

    constructor(viewModelClass: Class<VM>) : super() {
        this.viewModelClass = viewModelClass
    }

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId) {
        this.viewModelClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    }

    constructor(
        viewModelClass: Class<VM>,
        @LayoutRes contentLayoutId: Int,
    ) : super(contentLayoutId) {
        this.viewModelClass = viewModelClass
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[viewModelClass]

    }


}