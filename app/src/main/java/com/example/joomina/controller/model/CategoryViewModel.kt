package com.example.joomina.controller.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.joomina.data.Account
import com.example.joomina.data.Categories
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CategoryViewModel() : ViewModel() {
    fun addCategory(category: Categories) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore
                .collection("Categories")
                .document(category.id.toString())

            try {
                fireStoreRef.set(category).await()
            } catch (e: Exception) {
                println("Lỗi khi thêm tài khoản: ${e.message}")
            }
        }
    }

    fun getAllCategories(listAccount: (List<Categories>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore.collection("Categories")

            try {
                val querySnapshot = fireStoreRef.get().await()
                val list = mutableListOf<Categories>()

                for (document in querySnapshot) {
                    val category = document.toObject(Categories::class.java)
                    list.add(category)
                }

                listAccount(list)
            } catch (e: Exception) {
                // Xử lý lỗi khi có vấn đề xảy ra
                Log.e("getAllAccount", "Error getting documents: ", e)
                listAccount(emptyList())
            }
        }
    }
}