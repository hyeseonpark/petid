package com.petid.petid.di

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class PetidGlideModule: AppGlideModule() {
    override fun applyOptions(
        context: Context,
        builder: GlideBuilder
    ) {
        super.applyOptions(context, builder)
        /*val calculator = MemorySizeCalculator.Builder(context).setMemoryCacheScreens(2F).build()
        builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))

        val bitmapPoolSizeBytes = 1024 * 1024 * 30 // 30mb
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSizeBytes))

        val diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, "petidCache", diskCacheSizeBytes))
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_RGB_565).disallowHardwareConfig())
        // .disallowHardwareBitmaps())
        builder.setLogLevel(Log.DEBUG);*/
    }
}