package com.example.myinventory.presentation.add_product

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myinventory.MainActivity
import com.example.myinventory.R
import com.example.myinventory.databinding.FragmentAddProductBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedImageUri: Uri
    private lateinit var mViewModel: AddProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewModel =
            ViewModelProvider(requireActivity())[AddProductViewModel::class.java]
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {

        setUpProductTypesList()

        binding.ivBackBtn.setOnClickListener {
            val action =
                AddProductFragmentDirections.actionAddProductFragmentToProductListFragment()
            findNavController().navigate(action)
        }

        binding.btnSelectImage.setOnClickListener {
            pickImage()
        }

        handleAddProduct()
    }

    private fun handleAddProduct() {

        binding.btnAddProduct.setOnClickListener {

            binding.apply {
                val isInternetAvailable =
                    (requireActivity() as MainActivity).networkStatusChecker.isInternetAvailable
                if (isInternetAvailable) {
                    if (binding.etProductName.text.toString()
                            .isNotEmpty() && binding.etProductType.text.toString()
                            .isNotEmpty() && binding.etPrice.text.toString()
                            .isNotEmpty() && binding.etTax.text.toString().isNotEmpty()
                    ) {
                        addProduct()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please fill all the details to add the product",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    showUserFeedbackDialogBox(
                        requireContext().getString(R.string.show_no_internet_dialog_message),
                        requireContext().getString(R.string.show_no_internet_dialog_title),
                        requireContext().getString(R.string.dialog_box_okay),
                        requireContext().getString(R.string.dialog_box_cancel),
                    )
                }
            }
        }
    }

    private fun showUserFeedbackDialogBox(
        message: String,
        title: String,
        positiveBtnMessage: String,
        negativeBtnMessage: String,
        positiveBtnAction: () -> Unit = {},
        negativeBtnAction: () -> Unit = {}
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveBtnMessage) { dialog, which ->
            dialog.dismiss()
            positiveBtnAction()

        }
        builder.setNegativeButton(negativeBtnMessage) { dialog, which ->
            dialog.dismiss()
            negativeBtnAction()
        }
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }

    private fun setUpProductTypesList() {
        val productTypes = resources.getStringArray(R.array.product_type)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_product_type, productTypes)
        binding.etProductType.setAdapter(adapter)
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    if (isImageTypeAllowed(uri)) {
                        selectedImageUri = uri
                        binding.ivProductImage.setImageURI(selectedImageUri)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Unsupported image type",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            requireContext().contentResolver.query(uri, projection, null, null, null)
        val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val filePath: String? = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return filePath
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = IMAGE_TYPES
        pickImageLauncher.launch(intent)
    }

    private fun isImageTypeAllowed(uri: Uri): Boolean {
        val contentResolver = requireContext().contentResolver
        val type = contentResolver.getType(uri)
        return IMAGE_TYPES.contains(type ?: "")
    }

    companion object {
        private const val IMAGE_TYPES = "image/jpeg,image/png"
    }

    private fun addProduct() {

        binding.progressBar.visibility = View.VISIBLE

        val productName = binding.etProductName.text.toString()
        val productType = binding.etProductType.text.toString()
        val price = binding.etPrice.text.toString()
        val tax = binding.etTax.text.toString()

        val productNameBody =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                productName
            )
        val productTypeBody = RequestBody.create("text/plain".toMediaTypeOrNull(), productType)
        val priceBody = RequestBody.create("text/plain".toMediaTypeOrNull(), price)
        val taxBody = RequestBody.create("text/plain".toMediaTypeOrNull(), tax)

        if (this@AddProductFragment::selectedImageUri.isInitialized) {
            val file = getRealPathFromURI(selectedImageUri)?.let { File(it) }
            val imageRequestBody = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart =
                MultipartBody.Part.createFormData(
                    "files[]",
                    file?.name,
                    imageRequestBody!!
                )

            mViewModel.addProductOnServer(
                productNameBody,
                productTypeBody,
                taxBody,
                priceBody,
                imagePart
            )
        } else {
            mViewModel.addProductOnServer(
                productNameBody,
                productTypeBody,
                taxBody,
                priceBody,
                null
            )
        }

        mViewModel.isSuccessful.observe(requireActivity()) { isSuccessful ->

            binding.progressBar.visibility = View.GONE

            if (isSuccessful) {

                val positiveBtnAction: () -> Unit = {
                    binding.apply {
                        etPrice.setText("")
                        etTax.setText("")
                        etProductName.setText("")
                        etProductType.setText("")
                        etProductType.clearFocus()
                        ivProductImage.setImageResource(R.drawable.ic_product_placeholder)
                    }
                }

                val negativeBtnAction: () -> Unit = {
                    val action =
                        AddProductFragmentDirections.actionAddProductFragmentToProductListFragment()
                    findNavController().navigate(action)
                }

                showUserFeedbackDialogBox(
                    requireContext().getString(R.string.dialog_box_product_added_success_message),
                    requireContext().getString(R.string.dialog_box_product_added_success_title),
                    requireContext().getString(R.string.dialog_box_yes),
                    requireContext().getString(R.string.dialog_box_no),
                    positiveBtnAction,
                    negativeBtnAction
                )
            } else {
                showUserFeedbackDialogBox(
                    requireContext().getString(R.string.dialog_box_error_message),
                    requireContext().getString(R.string.dialog_box_error__title),
                    requireContext().getString(R.string.dialog_box_okay),
                    requireContext().getString(R.string.dialog_box_cancel)
                )
            }
        }
    }
}