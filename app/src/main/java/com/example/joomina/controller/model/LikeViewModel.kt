package com.example.joomina.controller.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.joomina.data.Cart
import com.example.joomina.data.Like
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LikeViewModel() : ViewModel() {
    fun addLikeProduct(like: Like) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore
                .collection("Like")
                .document(like.userName)

            try {
                fireStoreRef.set(like).await()
            } catch (e: Exception) {
                println("Lỗi khi thêm tài khoản: ${e.message}")
            }
        }
    }

    fun getAllCart(listLike: (List<Like>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore.collection("Like")

            try {
                val querySnapshot = fireStoreRef.get().await()
                val list = mutableListOf<Like>()

                for (document in querySnapshot) {
                    val like = document.toObject(Like::class.java)
                    list.add(like)
                }

                listLike(list)
            } catch (e: Exception) {
                // Xử lý lỗi khi có vấn đề xảy ra
                Log.e("getAllAccount", "Error getting documents: ", e)
                listLike(emptyList())
            }
        }
    }

    fun retrieveData(
        userName: String,
        data: (Like) -> Unit,
        onError: (Exception) -> Unit = {}
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("Like")
            .document(userName)
            try {
                fireStoreRef.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        onError(e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val likeData = snapshot.toObject(Like::class.java)
                        if (likeData != null) {
                            CoroutineScope(Dispatchers.Main).launch {
                                data(likeData)
                            }
                        }
                    }
                }
            }catch (e: Exception) {
                onError(e)
            }
    }

    fun deleteData(
        userName: String,
    ) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("Like")
            .document(userName)

        try {
            fireStoreRef.delete().await()
        }catch (e: Exception) {
            println("error")
        }
    }
}