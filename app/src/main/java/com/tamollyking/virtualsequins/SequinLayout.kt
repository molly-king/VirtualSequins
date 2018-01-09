package com.tamollyking.virtualsequins

import android.content.Context
import android.graphics.Rect
import android.support.constraint.ConstraintLayout
import android.view.MotionEvent
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup



/**
 * Created by mollyrand on 1/8/18.
 */
class SequinLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    var lastY = 0.0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> return false
            MotionEvent.ACTION_MOVE -> {
                handleMove(event)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun handleMove(event: MotionEvent) {
        val delta = event.y - lastY
        Log.d("delta", " " + delta)
        val sequins = findViewsAt(this, event.x.toInt(), event.y.toInt())
        for (s in sequins) {
            s.flip(delta)
        }
        lastY = event.y
    }

    private fun findViewsAt(viewGroup: ViewGroup, x: Int, y: Int): List<Sequin> {
        val list = ArrayList<Sequin>()
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is Sequin) {
                val location = IntArray(2)
                child.getLocationOnScreen(location)
                val rect = Rect(location[0], location[1], location[0] + child.width, location[1] + child.height)
                if (rect.contains(x, y)) {
                    list.add(child)
                }
            }
        }
        return list
    }
}