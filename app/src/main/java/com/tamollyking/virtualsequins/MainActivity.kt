package com.tamollyking.virtualsequins

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.view.ViewGroup
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.tamollyking.virtualsequins.views.LayoutModel
import com.tamollyking.virtualsequins.views.Sequin
import com.tamollyking.virtualsequins.views.SequinLayout
import com.tamollyking.virtualsequins.views.SequinModel
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    lateinit var layoutJson: String

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var layout = findViewById<SequinLayout>(R.id.layout)

        layoutJson = readLayoutFile()

        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        val layoutMod = moshi.adapter<LayoutModel>(LayoutModel::class.java).fromJson(layoutJson)
        var sequinMap : Map<String, SequinModel> = HashMap<String, SequinModel>()
        assertNotNull(layoutMod)
        for (sequin in layoutMod!!.sequins) {
            sequinMap = sequinMap.plus(Pair(sequin.id, sequin))
        }
        val chainseed = 1000
        for (column in layoutMod.columns) {
            val firstId = column.sequins.get(0)
            val topSequin = Sequin(this, sequinMap.get(firstId))
            topSequin.id = chainseed + column.index
            layout.addView(topSequin)
            val constraints = ConstraintSet()
            constraints.clone(layout)
            //connect to top to start row
            constraints.connect(topSequin.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            var leftId = ConstraintSet.PARENT_ID
            var rightId = ConstraintSet.PARENT_ID
            if (column.index > 0) {
                leftId = topSequin.id - 1
            }
            if (column.index < layoutMod.columns.size - 1) {
                rightId = topSequin.id + 1
            }
            // connect horizontally
            constraints.addToHorizontalChain(topSequin.id, leftId, rightId)
            constraints.applyTo(layout)
            for (j in 1 until column.sequins.size) {
                val sequinId = column.sequins.get(j)
                val s = Sequin(this, sequinMap.get(sequinId))
                val seed = column.index * 2000
                s.id = seed + j
                s.z = 1.5f * j
                layout.addView(s)
                val constraintSet = ConstraintSet()
                constraintSet.clone(layout)
                val topId = if (j > 1) {
                    s.id - 1
                } else {
                    topSequin.id
                }
                // align horizontally with top of row
                constraintSet.connect(s.id, ConstraintSet.START, topSequin.id, ConstraintSet.START)
                constraintSet.connect(s.id, ConstraintSet.END, topSequin.id, ConstraintSet.END)
                // align vertically by centering on bottom of sequin above
                constraintSet.connect(s.id, ConstraintSet.TOP, topId, ConstraintSet.BOTTOM)
                constraintSet.connect(s.id, ConstraintSet.BOTTOM, topId, ConstraintSet.BOTTOM)
                constraintSet.applyTo(layout)
            }
        }
    }


    fun readLayoutFile(): String {
        val inputStream : InputStream = assets.open("Files/poo.json")
        val inputString = inputStream.bufferedReader().use {
            it.readText()
        }
        return inputString
    }

}
