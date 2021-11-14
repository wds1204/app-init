package com.smart.wds.init.runtime.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.smart.wds.init.runtime.AppInitializer
import com.smart.wds.init.runtime.exception.StartupException
import com.smart.wds.init.runtime.manager.InitializerManager
import java.lang.IllegalStateException

class InitializationProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        context.takeIf { context -> context != null }?.let {
            val store = AppInitializer.mInstance.discoverAndInitialize(context!!)
            InitializerManager.Builder()
                .addAllInitializer(store.result)
                .build(context!!)
                .start()
                .await()
        } ?: throw StartupException("Context cannot be null")

        return true

    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        throw IllegalStateException("Not allowed.")
    }

    override fun getType(uri: Uri): String? {
        throw IllegalStateException("Not allowed.")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw IllegalStateException("Not allowed.")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw IllegalStateException("Not allowed.")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw IllegalStateException("Not allowed.")
    }
}