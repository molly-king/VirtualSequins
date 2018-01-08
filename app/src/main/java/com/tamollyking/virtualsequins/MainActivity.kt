package com.tamollyking.virtualsequins

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.FloatingActionButton
import android.view.ViewGroup

class MainActivity : AppCompatActivity() {



    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var layout = findViewById<ConstraintLayout>(R.id.layout)
        var squareSize = 3
        var i = 0
        val chainseed = 1000
        while (i < squareSize) {
            val topSequin = Sequin(this)
            var j = 1
            topSequin.id = chainseed + i
            layout.addView(topSequin)
            val constraints = ConstraintSet()
            constraints.clone(layout)
            //connect to top to start row
            constraints.connect(topSequin.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            var leftId = ConstraintSet.PARENT_ID
            var rightId = ConstraintSet.PARENT_ID
            if (i > 0) {
                leftId = topSequin.id - 1
            }
            if (i < squareSize - 1) {
                rightId = topSequin.id + 1
            }
//            connect horizontally
            constraints.addToHorizontalChain(topSequin.id, leftId, rightId)
            constraints.setHorizontalChainStyle(topSequin.id, ConstraintSet.CHAIN_PACKED)
            constraints.applyTo(layout)
            while (j < squareSize) {
                val s = Sequin(this)
                val seed = 100 * i
                s.id = seed + j
                layout.addView(s, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                val constraintSet = ConstraintSet()
                constraintSet.clone(layout)
                var topId = if (j > 1)  s.id - 1 else topSequin.id
                // align horizontally with top of row
                constraintSet.connect(s.id, ConstraintSet.START, topSequin.id, ConstraintSet.START)
                constraintSet.connect(s.id, ConstraintSet.END, topSequin.id, ConstraintSet.END)
                // align vertically by centering on bottom of sequin above
                constraintSet.connect(s.id, ConstraintSet.TOP, topId, ConstraintSet.BOTTOM)
                constraintSet.connect(s.id, ConstraintSet.BOTTOM, topId, ConstraintSet.BOTTOM)
                constraintSet.applyTo(layout)
                j++
            }

            i++
        }
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener({
            flip(findViewById<Sequin>(1000))
        })
    }

    fun flip(sequin: Sequin) {
        sequin.flip()
    }
}