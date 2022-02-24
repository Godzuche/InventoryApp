package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase: RoomDatabase() {

    abstract fun itemDao(): ItemDao

    private class ItemRoomDatabaseCallback(private val scope: CoroutineScope)
        : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch { prepopulateDatabase(database.itemDao()) }
            }
        }

        suspend fun prepopulateDatabase(itemDao: ItemDao) {
            // Delete all content
            itemDao.deleteAll()

            // Add sample items
            var item = Item(
                itemName = "Apple",
                itemPrice = 2.00,
                quantityInStock = 100
            )
            itemDao.insert(item)
            item = Item(
                itemName = "Grape",
                itemPrice = 1.00,
                quantityInStock = 70
            )
            itemDao.insert(item)
            item = Item(
                itemName = "Lemon",
                itemPrice = 2.50,
                quantityInStock = 50
            )
            itemDao.insert(item)
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ItemRoomDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}