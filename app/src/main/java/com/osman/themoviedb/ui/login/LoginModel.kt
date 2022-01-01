package com.osman.themoviedb.ui.login

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.osman.themoviedb.BR

class LoginModel : BaseObservable() {

    var email: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }

    var password: String? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }
}