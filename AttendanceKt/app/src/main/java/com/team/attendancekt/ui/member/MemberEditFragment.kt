package com.team.attendancekt.ui.member

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.room.util.FileUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.integration.android.IntentIntegrator
import com.team.attendancekt.MainActivity
import com.team.attendancekt.R
import com.team.attendancekt.databinding.MemberEditBinding
import kotlinx.android.synthetic.main.fragment_member_edit.*
import kotlinx.android.synthetic.main.layout_take_picture_action.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MemberEditFragment : Fragment() {

    lateinit var memberEditViewModel: MemberEditViewModel
    lateinit var binding: MemberEditBinding
    var currentPhotoFilePath: String = ""

    companion object {
        val KEY_MEMBER_ID = "member_id"
        val REQUEST_IMAGE_CAPTURE = 1;
        val PERMISSION_TAKE_PHOTO = 2;
        val REQUEST_PICK_IMAGE = 3;
        val PERMISSION_WRITE_STORAGE = 4;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity: MainActivity = requireActivity() as MainActivity
        activity.switchToggle(false)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        memberEditViewModel = ViewModelProviders.of(this).get(MemberEditViewModel::class.java)

        val id = arguments?.getInt(KEY_MEMBER_ID) ?: 0
        memberEditViewModel.init(id)
        memberEditViewModel.editMember?.observe(this, Observer {
            memberEditViewModel.member.value = it
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MemberEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = memberEditViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSave.setOnClickListener {
            memberEditViewModel.save()
            findNavController().navigateUp()
        }

        btnDelete.setOnClickListener {
            memberEditViewModel.delete()
            findNavController().navigateUp()
        }

        btnScan.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }

        btnTakePhoto.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_WRITE_STORAGE)
                return@setOnClickListener
            }
            showBottomSheetDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val activity: MainActivity = requireActivity() as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.switchToggle(true)
        activity.hideKeyBoard()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && currentPhotoFilePath != null) {
            val bitmap = writeImage(null, File(currentPhotoFilePath))
            binding.btnTakePhoto.setImageBitmap(bitmap)
            memberEditViewModel.member.value?.photo = currentPhotoFilePath
        } else if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val photoUri = data.data
            try {
                val bitmap: Bitmap = writeImage(photoUri, null)
                binding.btnTakePhoto.setImageBitmap(bitmap)
                memberEditViewModel.member.value?.photo = currentPhotoFilePath
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        } else if ( result != null){
            result.contents?.also {
                val member = memberEditViewModel.member.value
                member?.barcode = it
                memberEditViewModel.member.value = member
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_TAKE_PHOTO -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) dispatchTakePictureIntent()
            PERMISSION_WRITE_STORAGE ->  if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) showBottomSheetDialog()
        }
    }

    private fun writeImage(photoUri: Uri?, image: File?): Bitmap {
        lateinit var exifInterface: ExifInterface
        val file = createImageFile()
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        if (photoUri != null) {
            BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(photoUri), null, options)
        } else if (image != null) {
            BitmapFactory.decodeFile(image.path, options)
        }


        val REQUEST_SIZE = 240
        var width_tmp = options.outWidth
        var height_tmp = options.outHeight
        var scale = 1

        while (width_tmp / 2 >= REQUEST_SIZE && height_tmp / 2 >= REQUEST_SIZE) {
            width_tmp /= 2
            height_tmp /= 2
            scale *= 2
        }

        val scaledOpts = BitmapFactory.Options()
        scaledOpts.inSampleSize = scale

        val bitmap = if (photoUri != null) {
            BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(photoUri), null, scaledOpts)
        } else {
            BitmapFactory.decodeFile(image?.path, scaledOpts)
        }

        /*if (photoUri != null) {
            exifInterface = ExifInterface(getPath(photoUri))
        } else {
            //exifInterface = ExifInterface()
        }

        val rotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationDegrees = exifToDegrees(rotation)
        val matrix = Matrix()
        if (rotation != 0) matrix.preRotate(rotationDegrees.toFloat())

        //TODO image Rotate*/

        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))

        image?.also { it.delete() }

        return bitmap!!
    }

    private fun exifToDegrees(rotation: Int): Int {
        when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> return 90
            ExifInterface.ORIENTATION_ROTATE_180 -> return 180
            ExifInterface.ORIENTATION_ROTATE_270 -> return 270
        }
        return 0
    }

    private fun getPath(photoUri: Uri): String {
        val projections = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(photoUri, projections, null, null, null)
        if (cursor == null) {
            return ""
        }
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val s = cursor.getString(columnIndex)
            cursor.close()
            return s
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_take_picture_action, null)

        val layoutTakePhoto: LinearLayout = bottomSheetView.findViewById(R.id.layoutTakePhoto)
        val layoutChoosePhoto: LinearLayout = bottomSheetView.findViewById(R.id.layoutChoosePhoto)
        layoutTakePhoto.setOnClickListener {
            dialog.dismiss()
            dispatchTakePictureIntent()
        }

        layoutChoosePhoto.setOnClickListener {
            dialog.dismiss()
            dispatchChoosePictureIntent()
        }

        dialog.setContentView(bottomSheetView)
        dialog.show()
    }

    private fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_TAKE_PHOTO)
            return
        }

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (photoFile != null) {
                val photoUri = FileProvider.getUriForFile(requireContext(), "com.team.attendancekt.fileprovider", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun dispatchChoosePictureIntent() {
        val contentIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentIntent.type = "image/*"

        val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val chooserIntent = Intent.createChooser(contentIntent, "Choose Image Browser")

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickImageIntent))
        startActivityForResult(chooserIntent, REQUEST_PICK_IMAGE)
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val photoFile = "JPEG_" + timeStamp + "_"

        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(photoFile, ".jpg", storageDir)

        currentPhotoFilePath = image.absolutePath
        return  image
    }


}