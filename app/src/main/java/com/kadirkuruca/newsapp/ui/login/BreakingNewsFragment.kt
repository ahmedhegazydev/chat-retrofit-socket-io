package com.kadirkuruca.newsapp.ui.login

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
import com.kadirkuruca.newsapp.R
import com.kadirkuruca.newsapp.data.model.SocketUser
import com.kadirkuruca.newsapp.databinding.FragmentBreakingNewsBinding
import com.kadirkuruca.newsapp.network.SocketManager
import com.kadirkuruca.newsapp.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

private const val TAG = "BreakingNewsFragment"

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news)
//    , ArticlesAdapter.OnItemClickListener
{

    private val viewModel: BreakingNewsViewModel by viewModels()
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private var mSocket: Socket? = null
    private var id = ""

    private val configUser by lazy {
        ConfigUser.getInstance(requireContext())
    }
    private val map = HashMap<String, String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBreakingNewsBinding.bind(view)
//        val articleAdapter = ArticlesAdapter(this)
        requireActivity().title = REGISTRATION



//        binding.apply {
//            rvBreakingNews.apply {
//                adapter = articleAdapter
//                setHasFixedSize(true)
//                addOnScrollListener(this@BreakingNewsFragment.scrollListener)
//            }
//        }

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
//                        articleAdapter.submitList(newsResponse.articles.toList())
//                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
//                        isLastPage = viewModel.breakingNewsPage == totalPages
//                        if(isLastPage)
//                            rvBreakingNews.setPadding(0,0,0,0)
                        Log.e(TAG, "onViewCreated: " + newsResponse.token)


//                        configUser!!.getEditor()!!
//                            .putString(ID_DEVi, UUID.randomUUID().toString()).apply()
//                       var id = configUser!!.getPreferences()!!.getString(ID_DEVi, "")!!
//                        configUser!!.getEditor()!!.apply {
////                            putString(
////                                DATA_USER_NAME, "{\n" +
////                                        "\t\"id\":\"${id}\",\n" +
////                                        "\t\"name\":\"Ahmed Hegzo\",\n" +
////                                        "\t\"phone\":\"201221546343\",\n" +
////                                        "\t\"isOnline\":\"true\",\n" +
////                                        "\t\"password\":\"123456\"\n" +
////                                        "}"
////                            )
//                            putBoolean(IS_LOGIN, true)
//                            apply()
//                        }

//                        var phone = binding.txtPhone.text.toString()
//                        var password = binding.txtPassword.text.toString()
//
//                        val userId = JSONObject()
//                        userId.put(SocketUser.PHONE, phone)
//                        userId.put(SocketUser.PASSWORD, password)
//                        mSocket!!.emit(SING_IN, id, userId)

                        findNavController().navigate(
                            R.id.savedNewsFragment
                        )
//
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

        binding.txtPhone.setText("201221546343")
        binding.txtPassword.setText("123456")

        binding.btnSave.setOnClickListener {

            //            val username = mBinding.txtUsername.text.toString()
            var phone = binding.txtPhone.text.toString()
            var password = binding.txtPassword.text.toString()

            when {
//                TextUtils.isEmpty(username) -> {
//                    mBinding.txtUsername.error = getString(R.string.errorRequired)
//                    mBinding.txtUsername.requestFocus()
//                    return@setOnClickListener
//                }
//                !Patterns.EMAIL_ADDRESS.matcher(username).matches() -> {
//                    mBinding.txtUsername.error = getString(R.string.email)
//                    mBinding.txtUsername.requestFocus()
//                    return@setOnClickListener
//                }
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
//                password.length < 8 -> {
//                    mBinding.txtPassword.error = getString(R.string.errorPasswordShort)
//                    mBinding.txtPassword.requestFocus()
//                    return@setOnClickListener
//                }
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
                R.id.articleFragment
            )
        }

    }

//    private val scrollListener = object : RecyclerView.OnScrollListener() {
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){ //State is scrolling
//                isScrolling = true
//            }
//        }
//
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//
//            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//            val totalVisibleItemCount = layoutManager.childCount
//            val totalItemCount = layoutManager.itemCount
//
//            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
//            val isAtLastItem = firstVisibleItemPosition + totalVisibleItemCount >= totalItemCount
//            val isNotAtBeginning = firstVisibleItemPosition >= 0
//            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
//            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
//
//            if(shouldPaginate){
//                viewModel.getBreakingNews("tr")
//                isScrolling = false
//            }
//        }
//    }

//    override fun onItemClick(register: Article) {
//        val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(register)
//        findNavController().navigate(action)
//    }

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
                    findNavController().navigate(R.id.action_breakingNewsFragment_to_chatFragment)
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