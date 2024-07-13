package com.example.joomina.controller.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.joomina.data.Cart
import com.example.joomina.data.Categories
import com.example.joomina.data.history.History
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HistoryViewModel: ViewModel() {
    fun addHistory(history: History) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore
                .collection("Histories")
                .document(history.userName)

            try {
                fireStoreRef.set(history).await()
            } catch (e: Exception) {
                println("Lỗi khi thêm tài khoản: ${e.message}")
            }
        }
    }

    fun getAllHistories(listHistories: (List<History>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore.collection("Histories")

            try {
                val querySnapshot = fireStoreRef.get().await()
                val list = mutableListOf<History>()

                for (document in querySnapshot) {
                    val history = document.toObject(History::class.java)
                    list.add(history)
                }

                listHistories(list)
            } catch (e: Exception) {
                // Xử lý lỗi khi có vấn đề xảy ra
                Log.e("getAllAccount", "Error getting documents: ", e)
                listHistories(emptyList())
            }
        }
    }

    fun retrieveData(
        userName: String,
        data: (History) -> Unit,
        onError: (Exception) -> Unit = {}
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("Histories")
            .document(userName)
        try {
            fireStoreRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onError(e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val history = snapshot.toObject(History::class.java)
                    if (history != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            data(history)
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
            .collection("Histories")
            .document(userName)

        try {
            fireStoreRef.delete().await()
        }catch (e: Exception) {
            println("error")
        }
    }
}