package com.example.joomina.controller.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.joomina.data.Cart
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CartViewModel() : ViewModel() {
    fun addCart(cart: Cart) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore
                .collection("Carts")
                .document(cart.userName)

            try {
                fireStoreRef.set(cart).await()
            } catch (e: Exception) {
                println("Lỗi khi thêm tài khoản: ${e.message}")
            }
        }
    }

    fun getAllCart(listCart: (List<Cart>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireStoreRef = Firebase.firestore.collection("Carts")

            try {
                val querySnapshot = fireStoreRef.get().await()
                val list = mutableListOf<Cart>()

                for (document in querySnapshot) {
                    val cart = document.toObject(Cart::class.java)
                    list.add(cart)
                }

                listCart(list)
            } catch (e: Exception) {
                // Xử lý lỗi khi có vấn đề xảy ra
                Log.e("getAllAccount", "Error getting documents: ", e)
                listCart(emptyList())
            }
        }
    }

    fun retrieveData(
        userName: String,
        data: (Cart) -> Unit,
        onError: (Exception) -> Unit = {}
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("Carts")
            .document(userName)
            try {
                fireStoreRef.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        onError(e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val cartData = snapshot.toObject(Cart::class.java)
                        if (cartData != null) {
                            CoroutineScope(Dispatchers.Main).launch {
                                data(cartData)
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
            .collection("Carts")
            .document(userName)

        try {
            fireStoreRef.delete().await()
        }catch (e: Exception) {
            println("error")
        }
    }
}