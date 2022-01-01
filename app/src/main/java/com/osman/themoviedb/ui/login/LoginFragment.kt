package com.osman.themoviedb.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.navigation.fragment.findNavController
import com.osman.themoviedb.BR
import com.osman.themoviedb.base.DefaultBaseFragment
import com.osman.themoviedb.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : DefaultBaseFragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val loginModel by lazy { LoginModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.model = loginModel
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionToMoviesFragment())
        }

        loginModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (propertyId == BR.email || propertyId == BR.password) {
                    binding.btnLogin.isEnabled = loginModel.email.isValidEmailAddress() &&
                            loginModel.password.isValidPassword()
                }
            }
        })
    }

    fun String?.isValidEmailAddress(): Boolean {
        return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun String?.isValidPassword(): Boolean {
        return !isNullOrEmpty() && length in 8..15
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}