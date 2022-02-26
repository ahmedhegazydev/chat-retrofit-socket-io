package com.task.newsapp.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.task.newsapp.R
import com.task.newsapp.data.model.SocketUser
import com.task.newsapp.databinding.FragmentUserLoginBinding
import com.task.newsapp.network.SocketManager
import com.task.newsapp.ui.viewmodel.UserAuthViewModel
import com.task.newsapp.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

private const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_user_login)
{

    private val viewModel: UserAuthViewModel by viewModels()
    var isLoading = false
    private var mSocket: Socket? = null
    private var id = ""

    private val configUser by lazy {
        ConfigUser.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentUserLoginBinding.bind(view)
        requireActivity().title = REGISTRATION


        mSocket = SocketManager.getInstance(requireContext())!!.getSocket()
        mSocket!!.on(SING_IN, onSingIn)
        val dataShared = configUser!!.getPreferences()
        id = dataShared!!.getString(ID_DEVi, "")!!


        viewModel.loginUser.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { newsResponse ->
                        Log.e(TAG, "onViewCreated: " + newsResponse.token)
                        findNavController().navigate(
                            R.id.listAllUsersFragment
                        )
                    }
                }
                is Resource.Error -> {
                    paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = true
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Error: $message")
                    }
                }
                is Resource.Loading -> {
                    paginationProgressBar.visibility = View.VISIBLE
                }
            }
        }

        binding.txtPhone.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.btnSave.isEnabled = charSequence.toString().trim { it <= ' ' }
                    .isNotEmpty()
            }

            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) = Unit

            override fun afterTextChanged(editable: Editable) = Unit
        })

        binding.txtPhone.setText("201222546343")
        binding.txtPassword.setText("123456")

        binding.btnSave.setOnClickListener {

            var phone = binding.txtPhone.text.toString()
            var password = binding.txtPassword.text.toString()

            when {
                TextUtils.isEmpty(phone) -> {
                    binding.txtPhone.error = getString(R.string.errorRequired)
                    binding.txtPhone.requestFocus()
                    return@setOnClickListener
                }
                !Patterns.PHONE.matcher(phone).matches() -> {
                    binding.txtPhone.error = getString(R.string.phone)
                    binding.txtPhone.requestFocus()
                    return@setOnClickListener
                }
                TextUtils.isEmpty(password) -> {
                    binding.txtPassword.error = getString(R.string.errorRequired)
                    binding.txtPassword.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    val userId = JSONObject()
                    userId.put(SocketUser.PHONE, phone)
                    userId.put(SocketUser.PASSWORD, password)
                    mSocket!!.emit(SING_IN, id, userId)

//                    map["phone"] = phone
//                    map["password"] = password
//                    viewModel.getLoginUser(map)

                }
            }
        }

        binding.txtSingUp.setOnClickListener {
            findNavController().navigate(
//                R.id.singUpAuthFragment
                R.id.registerFragment
            )
        }

    }


    private var onSingIn = Emitter.Listener { args ->
        GlobalScope.launch(Dispatchers.Main) {
            val length = args.size
            if (length == 0) {
                return@launch
            }
            try {
                if (id == args[0].toString()) {
                    configUser!!.getEditor()!!.apply {
                        putString(DATA_USER_NAME, args[1].toString())
                        putBoolean(IS_LOGIN, true)
                        apply()
                    }
                    val user = Gson().fromJson(args[1].toString(), SocketUser::class.java)
                    findNavController().navigate(R.id.action_breakingNewsFragment_to_savedNewsFragment)
                        .also {
                            mSocket!!.emit(GET_ALL_USER, true)
                            mSocket!!.emit(UPDATE_DATA, JSONObject().apply {
                                put(SocketUser.ID, user.id)
                                put(SocketUser.IS_ONLINE, true)
                            })
                        }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}