package com.kevinhomorales.storagefirebasekotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.kevinhomorales.storagefirebasekotlin.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val storage = FirebaseStorage.getInstance()
    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActions()
    }

    private fun setUpActions() {
        binding.uploadButtonId.setOnClickListener {
            selectImage()
        }
        binding.downloadButtonId.setOnClickListener {
            downloadImage()
        }
    }

    private fun uploadImage() {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("subida/storage.png")
        val task = imagesRef.putFile(imageUri)
        task.addOnSuccessListener {
            // Ok de la subida
            binding.firstImageViewId.setImageURI(null);
            Toast.makeText(this,"Imagen subida con éxito",Toast.LENGTH_SHORT).show();
        }.addOnFailureListener {
            // Controlamos nuestros errores
            println("Error")
        }
    }

    private fun downloadImage() {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("descarga/storage.png")
        imagesRef.downloadUrl.addOnSuccessListener { url ->
            // Llega el archivo
            Glide.with(this)
                .load(url)
                .into(binding.secondImageViewId)
        }.addOnFailureListener {
            // Controlamos nuestros errores
            println("Error")
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null && data.data != null) {
            imageUri = data.data!!
            binding.firstImageViewId.setImageURI(imageUri)
            uploadImage()
        }
    }

}