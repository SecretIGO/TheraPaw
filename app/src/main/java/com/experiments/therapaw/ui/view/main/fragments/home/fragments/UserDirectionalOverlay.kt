package com.experiments.therapaw.ui.view.main.fragments.home.fragments

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import com.experiments.therapaw.R
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Overlay
import kotlin.math.cos
import kotlin.math.sin

class UserDirectionOverlay(private var location: GeoPoint) : Overlay() {
    private var rotationDegrees: Float = 0f

    fun updateRotation(newRotation: Float) {
        rotationDegrees = newRotation
    }

    @SuppressLint("ResourceAsColor")
    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        val projection = mapView.projection
        val screenPoint = Point()
        projection.toPixels(location, screenPoint)

        val paintCircle = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.STROKE
            strokeWidth = 5f
            alpha = 180
        }

        val paintArrow = Paint().apply {
            color = R.color.primary
            style = Paint.Style.FILL
            alpha = 220
        }

        val radius = 30f
        canvas.drawCircle(screenPoint.x.toFloat(), screenPoint.y.toFloat(), radius, paintCircle)

        val path = Path()
        val arrowSize = 20f
        val centerX = screenPoint.x.toFloat()
        val centerY = screenPoint.y.toFloat()

        val angleRadians = Math.toRadians(rotationDegrees.toDouble())
        val tipX = centerX + arrowSize * cos(angleRadians).toFloat()
        val tipY = centerY + arrowSize * sin(angleRadians).toFloat()
        val leftX = centerX + (arrowSize / 2) * cos(angleRadians + Math.PI * 3 / 4).toFloat()
        val leftY = centerY + (arrowSize / 2) * sin(angleRadians + Math.PI * 3 / 4).toFloat()
        val rightX = centerX + (arrowSize / 2) * cos(angleRadians - Math.PI * 3 / 4).toFloat()
        val rightY = centerY + (arrowSize / 2) * sin(angleRadians - Math.PI * 3 / 4).toFloat()

        path.moveTo(tipX, tipY)
        path.lineTo(leftX, leftY)
        path.lineTo(rightX, rightY)
        path.close()

        canvas.drawPath(path, paintArrow)
    }
}
