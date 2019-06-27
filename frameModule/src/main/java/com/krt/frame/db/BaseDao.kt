package com.krt.frame.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Update

@Dao
interface BaseDao<T> {
    @Insert
    fun insertItem(item: T) //插入单条数据

    @Insert
    fun insertItems(items: List<T>) //插入list数据

    @Delete
    fun deleteItem(item: T) //删除item

    @Update
    fun updateItem(item: T) //更新item
}