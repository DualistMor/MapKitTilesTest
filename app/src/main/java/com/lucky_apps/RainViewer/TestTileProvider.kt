package com.lucky_apps.RainViewer

import android.graphics.Bitmap
import com.huawei.hms.maps.model.Tile
import com.huawei.hms.maps.model.TileProvider
import okhttp3.*
import java.io.IOException
import java.nio.ByteBuffer


class TestTileProvider(private val url: String) : TileProvider {
    override fun getTile(p0: Int, p1: Int, p2: Int): Tile {
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url(url)
            .build()
        val resp = client.newCall(request).execute()
        return Tile(500, 500, resp.body!!.bytes())
    }
}

fun ByteArray.bitmap(width: Int, height: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(this))
    return bitmap
}