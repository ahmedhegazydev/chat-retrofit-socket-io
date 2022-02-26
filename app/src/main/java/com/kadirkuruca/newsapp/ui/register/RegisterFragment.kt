package com.kadirkuruca.newsapp.ui.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.android.material.snackbar.Snackbar
import com.kadirkuruca.newsapp.R
import com.kadirkuruca.newsapp.data.model.SocketUser
import com.kadirkuruca.newsapp.databinding.FragmentArticleBinding
import com.kadirkuruca.newsapp.network.SocketManager
import com.kadirkuruca.newsapp.ui.login.BreakingNewsViewModel
import com.kadirkuruca.newsapp.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

private const val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_article) {

//    private val viewModel: ArticleViewModel by viewModels()
    private val viewModel: BreakingNewsViewModel by viewModels()
    private var imageUser: String? = null
    private lateinit var binding: FragmentArticleBinding
    var isLoading = false
    private var mSocket: Socket? = null
    private var id = ""
    private val configUser by lazy {
        ConfigUser.getInstance(requireContext())!!
    }
    private val map = HashMap<String, String>()
//    private val map = HashMap<String, RequestBody>()

    private val args by navArgs<ArticleFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         binding = FragmentArticleBinding.bind(view)


//        binding.apply {
//            val register = args.register
//            webView.apply {
//                webViewClient = WebViewClient()
//                register.url?.let {
//                    loadUrl(register.url.toString())
//                }
//            }
//
//            fab.setOnClickListener {
//                viewModel.saveArticle(register)
//            }
//        }

//        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
//            viewModel.articleEvent.collect { event ->
//                when (event) {
//                    is ArticleViewModel.ArticleEvent.ShowArticleSavedMessage -> {
//                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }

        mSocket = SocketManager.getInstance(requireContext())!!.getSocket()
        mSocket!!.on(SING_UP, onSingUp)

        binding.txtLogin.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.txtName.setText("Ahmed Hegzo")
        binding.txtPhone.setText("201222546343")
        binding.txtPassword.setText("123456")
        binding.txtPassword2.setText("123456")

        binding.btnSave.setOnClickListener {
            var name = binding.txtName.text.toString()
            var phone = binding.txtPhone.text.toString()
            var password = binding.txtPassword.text.toString()
            var password2 = binding.txtPassword2.text.toString()

//            name = "Ahmed Mohamed"
//            phone = "201222546345"
//            password = "01156749640"
//            password2 = "01156749640"


            when {
                TextUtils.isEmpty(name) -> {
                    binding.txtName.error = getString(R.string.errorRequired)
                    binding.txtName.requestFocus()
                    return@setOnClickListener
                }
                name.length < 3 -> {
                    binding.txtName.error = getString(R.string.errorNameShort)
                    binding.txtName.requestFocus()
                    return@setOnClickListener
                }
//                TextUtils.isEmpty(username) -> {
//                    binding.txtUsername.error = getString(R.string.errorRequired)
//                    binding.txtUsername.requestFocus()
//                    return@setOnClickListener
//                }
//                !Patterns.EMAIL_ADDRESS.matcher(username).matches() -> {
//                    binding.txtUsername.error = getString(R.string.email)
//                    binding.txtUsername.requestFocus()
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
//                    binding.txtPassword.error = getString(R.string.errorPasswordShort)
//                    binding.txtPassword.requestFocus()
//                    return@setOnClickListener
//                }
                TextUtils.isEmpty(password2) -> {
                    binding.txtPassword2.error = getString(R.string.errorRequired)
                    binding.txtPassword2.requestFocus()
                    return@setOnClickListener
                }
                password != password2 -> {
                    binding.txtPassword2.error = getString(R.string.errorPassword)
                    binding.txtPassword2.requestFocus()
                    return@setOnClickListener
                }
//                TextUtils.isEmpty(imageUser) -> {
//                    Snackbar.make(
//                        requireView(),
//                        getString(R.string.errorImage),
//                        Snackbar.LENGTH_LONG
//                    ).show()
//                    return@setOnClickListener
//                }
                else -> {

                    var name = binding.txtName.text.toString()
                    var phone = binding.txtPhone.text.toString()
                    var password = binding.txtPassword.text.toString()

                    val user = JSONObject()
                    user.put(SocketUser.NAME, name)
                    user.put(SocketUser.PHONE, phone)
                    user.put(SocketUser.PASSWORD, password)
                    user.put(SocketUser.ID, UUID.randomUUID().toString())
                    user.put(SocketUser.IMAGE, imageUser)
                    user.put(SocketUser.IS_ONLINE, false)
//                        mSocket!!.emit(SING_UP, id, user)

                    viewModel.getRegisterUser(name, phone, password)

                }
            }
        }

        binding.cardView.setOnClickListener {
            permission(
                requireContext(),
                arrayListOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                selectImage()
            }
        }


        viewModel.registerUser.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { newsResponse ->

                        configUser.getEditor()!!
                            .putString(ID_DEVi, UUID.randomUUID().toString()).apply()
                        id = configUser.getPreferences()!!.getString(ID_DEVi, "")!!

                        var name = binding.txtName.text.toString()
                        var phone = binding.txtPhone.text.toString()
                        var password = binding.txtPassword.text.toString()

                        val user = JSONObject()
                        user.put(SocketUser.NAME, name)
                        user.put(SocketUser.PHONE, phone)
                        user.put(SocketUser.PASSWORD, password)
                        user.put(SocketUser.ID, UUID.randomUUID().toString())
                        user.put(SocketUser.IMAGE, imageUser)
                        user.put(SocketUser.IS_ONLINE, false)
                        mSocket!!.emit(SING_UP, id, user)


                        Log.e(TAG, "onViewCreated: " + newsResponse.token, )
//                        findNavController().navigate(
//                            R.id.breakingNewsFragment
//                        )
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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CODE &&
            resultCode == Activity.RESULT_OK
        ) {

//            if (!TextUtils.isEmpty(binding.txtUsername.text)) {
//                binding.btnSave.isEnabled = true
//            }
            if (!TextUtils.isEmpty(binding.txtPhone.text)) {
                binding.btnSave.isEnabled = true
            }

            val imageUri = data!!.data
            binding.image.setImageURI(imageUri)

            val bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
            imageUser = imageToBase64(bitmap)

        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = TYPE_INTENT_IMAGE
        startActivityForResult(intent, REQUEST_IMAGE_CODE)
    }

    private var onSingUp = Emitter.Listener { args ->
        GlobalScope.launch(Dispatchers.Main) {
            val length = args.size
            if (length == 0) {
                return@launch
            }
            try {
                if (id == args[0].toString()
                ) {
                    if (args[1].toString().toBoolean())
                        findNavController().navigateUp()
                    else {
//                        mBinding.txtUsername.error = getString(R.string.errorEmail)
//                        mBinding.txtUsername.requestFocus()
                        binding.txtPhone.error = getString(R.string.errorPhone)
                        binding.txtPhone.requestFocus()
                    }
                }
            } catch (e: Exception) {
                e
            }
        }
    }

}