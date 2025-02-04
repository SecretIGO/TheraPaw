package com.experiments.therapaw.ui.view.main.fragments.home.fragments

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Overlay
import kotlin.math.cos
import kotlin.math.sin

class UserDirectionOverlay(private var location: GeoPoint) : Overlay() {
    private var rotationDegrees: Float = 0f

    fun updateRotation(newRotation: Float) {
        rotationDegrees = newRotation - 90f
    }

    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        val projection = mapView.projection
        val screenPoint = Point()
        projection.toPixels(location, screenPoint)

        val centerX = screenPoint.x.toFloat()
        val centerY = screenPoint.y.toFloat()

        val arrowSize = 26f
        val circleRadius = 18f
        val outerCircleBorder = circleRadius + 5f
        val innerCircleRadius = circleRadius - 10f

        val paintArrow = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
            alpha = 255
        }

        val angleRadians = Math.toRadians(rotationDegrees.toDouble())

        val tipX = centerX + (circleRadius + arrowSize) * cos(angleRadians).toFloat()
        val tipY = centerY + (circleRadius + arrowSize) * sin(angleRadians).toFloat()

        val leftX = centerX + circleRadius * cos(angleRadians + Math.PI / 2).toFloat()
        val leftY = centerY + circleRadius * sin(angleRadians + Math.PI / 2).toFloat()
        val rightX = centerX + circleRadius * cos(angleRadians - Math.PI / 2).toFloat()
        val rightY = centerY + circleRadius * sin(angleRadians - Math.PI / 2).toFloat()

        val path = Path().apply {
            moveTo(tipX, tipY)
            lineTo(leftX, leftY)
            lineTo(rightX, rightY)
            close()
        }

        canvas.drawPath(path, paintArrow)

        val paintOuterCircle = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            alpha = 255
        }
        canvas.drawCircle(centerX, centerY, outerCircleBorder, paintOuterCircle)

        val paintCircle = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
            alpha = 255
        }
        canvas.drawCircle(centerX, centerY, circleRadius, paintCircle)

        val paintInnerCircle = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            alpha = 255
        }
        canvas.drawCircle(centerX, centerY, innerCircleRadius, paintInnerCircle)
    }
}