package com.petid.petid.di

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class PetidGlideModule: AppGlideModule() {
    override fun applyOptions(
        context: Context,
        builder: GlideBuilder
    ) {
        super.applyOptions(context, builder)
        val calculator = MemorySizeCalculator.Builder(context).setMemoryCacheScreens(3F).build()
        builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))

        val bitmapPoolSizeBytes = 1024 * 1024 * 45 // 30mb
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSizeBytes.toLong()))

        val diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, "petidCache", diskCacheSizeBytes.toLong()))
        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_RGB_565).disallowHardwareConfig())
    }
}