package com.lucky_apps.RainViewer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.TileOverlayOptions
import com.huawei.hms.maps.model.TileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnMapReadyCallback, HuaweiMap.OnMapLoadedCallback {

    lateinit var mapFragment: SupportMapFragment
    lateinit var map: HuaweiMap

    val ioScope = CoroutineScope(Dispatchers.IO)
    val uiScope = CoroutineScope(Dispatchers.Main)

    var imageUrls: ArrayList<String> = arrayListOf(
        "https://www.gettyimages.ca/gi-resources/images/500px/983801190.jpg",
        "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    var index: Int = 0
    override fun onMapReady(map: HuaweiMap) {
        this.map = map
        map.setOnMapLoadedCallback(this)
    }

    override fun onMapLoaded() {
        Log.d("onMapLoaded", "onMapLoaded")
        if (index >= imageUrls.size) {
            index = 0
        }
        uiScope.launch {
            map.addTileOverlay(TileOverlayOptions().tileProvider(getProvidedTile(index++)))
            map.setOnMapLoadedCallback(this@MainActivity)
        }
    }

    suspend fun getProvidedTile(index: Int): TileProvider {
        return ioScope.async {
            Thread.sleep(1000)
            return@async TestTileProvider(imageUrls[index]) }.await()
    }
}
