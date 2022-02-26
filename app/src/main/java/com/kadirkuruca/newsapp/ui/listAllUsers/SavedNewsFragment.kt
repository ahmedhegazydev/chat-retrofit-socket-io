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
import com.kadirkuruca.newsapp.data.model.AllUsers
import com.kadirkuruca.newsapp.data.model.SocketUser
import com.kadirkuruca.newsapp.databinding.FragmentSavedNewsBinding
import com.kadirkuruca.newsapp.network.SocketManager
import com.kadirkuruca.newsapp.ui.login.BreakingNewsViewModel
import com.kadirkuruca.newsapp.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Type

private const val TAG = "SavedNewsFragment"

@AndroidEntryPoint
class SavedNewsFragment : Fragment(R.layout.fragment_saved_news)
//    ArticlesAdapter.OnItemClickListener
{

//    private val viewModel: SavedNewsViewModel by viewModels()
    private val viewModel: BreakingNewsViewModel by viewModels()
    private val viewMode by lazy { ViewModelProvider(requireActivity())[UsersViewModel::class.java] }

    var isLoading = false
    private var mSocket: Socket? = null
    private lateinit var socketUser: SocketUser
    private var userString: String = ""

    private val userAdapter = UserAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSavedNewsBinding.bind(view)

        mSocket = SocketManager.getInstance(requireContext())!!.getSocket()
        mSocket!!.on(GET_ALL_USER, onUserList)
        userString = ConfigUser.getInstance(requireContext())!!.getPreferences()!!
            .getString(DATA_USER_NAME, "")!!
        socketUser = Gson().fromJson(userString, SocketUser::class.java)


//        val articleAdapter = ArticlesAdapter(this)
//        binding.apply {
//            rvSavedNews.apply {
//                adapter = articleAdapter
//                setHasFixedSize(true)
//            }
//
//            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
//                0,
//                ItemTouchHelper.LEFT
//            ) {
//                override fun onMove(
//                    recyclerView: RecyclerView,
//                    viewHolder: RecyclerView.ViewHolder,
//                    target: RecyclerView.ViewHolder
//                ): Boolean {
//                    return false
//                }
//
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    val register = articleAdapter.currentList[viewHolder.adapterPosition]
//                    viewModel.onArticleSwiped(register)
//                }
//            }).attachToRecyclerView(rvSavedNews)
//        }

//        viewModel.getAllArticles().observe(viewLifecycleOwner) {
//            articleAdapter.submitList(it)
//        }

//        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
//            viewModel.savedArticleEvent.collect { event ->
//                when (event) {
//                    is SavedNewsViewModel.SavedArticleEvent.ShowUndoDeleteArticleMessage -> {
//                        Snackbar.make(requireView(), "Article Deleted!",Snackbar.LENGTH_LONG)
//                            .setAction("UNDO"){
//                                viewModel.onUndoDeleteClick(event.register)
//                            }.show()
//                    }
//                }
//            }
//        }

        binding.rcDataUser.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }


//        binding.addGroup.hide()
        viewMode.getAllUser().observe(viewLifecycleOwner) {
            userAdapter.apply {
                dataList = it
//                addGroup!!.data = it
            }
        }


        userAdapter.setItemClickListener { user, _ ->
            findNavController()
                .navigate(
                    R.id.chatFragment,
                    Bundle().apply {
//                        this.putString(USER_ID, (user as SocketUser).id)
                        this.putString(USER_ID, (user as AllUsers.Data).id.toString())
                        this.putString(TYPE_CHAT, USER_FRAGMENT)
                        this.putString(USER_NAME, user.fullname)
                    }
                )
        }

        viewModel.allUsers.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { newsResponse ->
//                        articleAdapter.submitList(newsResponse.articles.toList())
//                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
//                        isLastPage = viewModel.breakingNewsPage == totalPages
//                        if(isLastPage)
//                            rvBreakingNews.setPadding(0,0,0,0)

//                        findNavController().navigate(
//                            R.id.savedNewsFragment
//                        )
                        userAdapter.apply {
                            dataList = newsResponse.data
//                            addGroup!!.data = it
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

//    override fun onItemClick(register: Article) {
//        val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(register)
//        findNavController().navigate(action)
//    }

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