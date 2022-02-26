package com.kadirkuruca.newsapp.ui.listAllUsers

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kadirkuruca.newsapp.R
import com.kadirkuruca.newsapp.adapter.UserAdapter
import com.kadirkuruca.newsapp.data.model.SocketUser
import com.kadirkuruca.newsapp.databinding.FragmentListAllUsersBinding
import com.kadirkuruca.newsapp.network.SocketManager
import com.kadirkuruca.newsapp.ui.viewmodel.UserAuthViewModel
import com.kadirkuruca.newsapp.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list_all_users.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Type

private const val TAG = "ListAllUsersFragment"

@AndroidEntryPoint
class ListAllUsersFragment : Fragment(R.layout.fragment_list_all_users) {

    private val viewModel: UserAuthViewModel by viewModels()
    private val viewMode by lazy { ViewModelProvider(requireActivity())[UsersViewModel::class.java] }
    var isLoading = false
    private var mSocket: Socket? = null
    private lateinit var socketUser: SocketUser
    private var userString: String = ""

    private val userAdapter = UserAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentListAllUsersBinding.bind(view)

        mSocket = SocketManager.getInstance(requireContext())!!.getSocket()
        mSocket!!.on(GET_ALL_USER, onUserList)
        userString = ConfigUser.getInstance(requireContext())!!.getPreferences()!!
            .getString(DATA_USER_NAME, "")!!
        socketUser = Gson().fromJson(userString, SocketUser::class.java)

        binding.rcDataUser.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }


        viewMode.getAllUser().observe(viewLifecycleOwner) {
            userAdapter.apply {
                dataList = it
            }
        }

        userAdapter.setItemClickListener { user, _ ->
            findNavController()
                .navigate(
                    R.id.chatFragment,
                    Bundle().apply {
                        this.putString(USER_ID, (user as SocketUser).id)
//                        this.putString(USER_ID, (user as AllUsers.Data).id.toString())

                        this.putString(TYPE_CHAT, USER_FRAGMENT)

//                        this.putString(USER_NAME, user.fullname)
                        this.putString(USER_NAME, user.name)
                    }
                )
        }

        viewModel.allUsers.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { newsResponse ->
                        userAdapter.apply {
                            dataList = newsResponse.data
                        }
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
//        viewModel.getAllUsers()

    }

    private var onUserList = Emitter.Listener { args ->
        GlobalScope.launch(Dispatchers.Main) {
            val length = args.size
            if (length == 0) {
                return@launch
            }
            val userListToken: Type = object : TypeToken<ArrayList<SocketUser>>() {}.type
            val userList =
                Gson().fromJson<ArrayList<SocketUser>>(
                    args[0].toString(),
                    userListToken
                )
            socketUser.isOnline = true
            userList.remove(socketUser)
            viewMode.addListUsers(userList)
        }
    }
}