package com.example.smartflowassessment.data.repository

import com.example.smartflowassessment.data.api.ApiService
import com.example.smartflowassessment.data.db.ItemDao
import com.example.smartflowassessment.data.model.Item
import javax.inject.Inject

class ItemRepository
    @Inject
    constructor(
        private val api: ApiService,
        private val dao: ItemDao,
    ) {
        private var hasSynced = false

        suspend fun getItems(): List<Item> =
            try {
                if (!hasSynced) {
                    val response = api.getAllProducts()
                    dao.insertAll(response.products)
                    hasSynced = true
                }
                dao.getAllItems()
            } catch (e: Exception) {
                dao.getAllItems()
            }

        suspend fun insertItem(item: Item) {
            dao.insertItem(item)
        }

        suspend fun getItemById(id: Int): Item? = dao.getItemById(id)

        suspend fun deleteItem(item: Item): Boolean =
            try {
                api.deleteProduct(item.id)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
    }
