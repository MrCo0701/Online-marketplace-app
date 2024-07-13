package com.example.joomina.controller.model

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.joomina.data.Cart
import com.example.joomina.data.Comment
import com.example.joomina.data.Product
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class ProductViewModel: ViewModel() {
    fun addProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore
                .collection("Products")
                .document(product.id.toString())

            try {
                fireStoreRef.set(product).await()
            } catch (e: Exception) {
                println("Lỗi khi thêm tài khoản: ${e.message}")
            }
        }
    }

    fun getAllProduct(listProduct: (List<Product>) -> Unit) {
        var number = 0
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore.collection("Products")

            try {
                val querySnapshot = fireStoreRef.get().await()
                val listOfProducts = mutableListOf<Product>()

                for (document in querySnapshot) {
                    number++
                    val product = document.toObject(Product::class.java)
                    listOfProducts.add(product)
                }

                listProduct(listOfProducts)
            } catch (e: Exception) {
                // Xử lý lỗi khi có vấn đề xảy ra
                Log.e("getAllAccount", "Error getting documents: ", e)
                listProduct(emptyList())
            }
        }
    }

    fun retrieveData(
        id: Int,
        data: (Product) -> Unit,
        onError: (Exception) -> Unit = {}
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("Products")
            .document(id.toString())
        try {
            fireStoreRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onError(e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val product = snapshot.toObject(Product::class.java)
                    if (product != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            data(product)
                        }
                    }
                }
            }
        }catch (e: Exception) {
            onError(e)
        }
    }

    fun uploadImage(
        bitmap: Bitmap,
        callBack:(Boolean, String) -> Unit
    ) {
        val storage = Firebase.storage.reference
        val imageRef = storage.child("imageProduct/$bitmap")

        val bao = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao) //  luu bitmap vao baos
        val imageData = bao.toByteArray()

        imageRef.putBytes(imageData).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {uri ->
                val imageUrl = uri.toString()
                callBack(true, imageUrl)
            }.addOnFailureListener{
                callBack(false, null.toString())
            }
        }.addOnFailureListener{
            callBack(false, null.toString())
        }
    }

    fun deleteData(
        productId: String,
    ) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("Products")
            .document(productId)

        try {
            fireStoreRef.delete().await()
        }catch (e: Exception) {
            println("error")
        }
    }


    fun addComment(
        product: Product,
        comment: Comment
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore
                .collection("Products")
                .document(product.id.toString())
                .collection("comments")
                .document(comment.id.toString())

            try {
                fireStoreRef.set(comment).await()
            } catch (e: Exception) {
                println("Lỗi khi thêm tài khoản: ${e.message}")
            }
        }
    }

    fun getAllComment(
        product: Product,
        listComment: (List<Comment>) -> Unit
    ) {
        var number = 0
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore
                .collection("Products")
                .document(product.id.toString())
                .collection("comments")

            try {
                val querySnapshot = fireStoreRef.get().await()
                val listCommentTest = mutableListOf<Comment>()

                for (document in querySnapshot) {
                    number++
                    val comment = document.toObject(Comment::class.java)
                    listCommentTest.add(comment)
                }

                listComment(listCommentTest)
            } catch (e: Exception) {
                // Xử lý lỗi khi có vấn đề xảy ra
                Log.e("getAllAccount", "Error getting documents: ", e)
                listComment(emptyList())
            }
        }
    }
}