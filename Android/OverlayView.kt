/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.objectdetection

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import org.tensorflow.lite.task.vision.detector.Detection
import java.util.*

import kotlin.math.*



class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results: List<Detection> = LinkedList<Detection>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var scaleFactor: Float = 1f

    private var bounds = Rect()

    init {
        initPaints()
    }

    fun clear() {
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = ContextCompat.getColor(context!!, R.color.bounding_box_color)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }


    //  Duke
    var car_center_x = 370;
    var car_center_y = 250;


    override fun draw(canvas: Canvas) {
        super.draw(canvas)


        for (result in results) {
            // karl.kwon
            if (result.categories[0].label != "car")
                continue;

            val boundingBox = result.boundingBox

            val top = boundingBox.top * scaleFactor
            val bottom = boundingBox.bottom * scaleFactor
            val left = boundingBox.left * scaleFactor
            val right = boundingBox.right * scaleFactor


            //  Duke
            car_center_x = ( ( left + right ) / 2 ).toInt();
            car_center_y = ( ( top + bottom ) / 2 ).toInt();


            // Draw bounding box around detected objects
            val drawableRect = RectF(left, top, right, bottom)
            canvas.drawRect(drawableRect, boxPaint)

            // Create text to display alongside detected objects
            val drawableText =
                result.categories[0].label + " " +
                        String.format("%.2f", result.categories[0].score)

            // Draw rect behind display text
            textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()
            canvas.drawRect(
                left,
                top,
                left + textWidth + Companion.BOUNDING_RECT_TEXT_PADDING,
                top + textHeight + Companion.BOUNDING_RECT_TEXT_PADDING,
                textBackgroundPaint
            )

            // Draw text for detected object
            canvas.drawText(drawableText, left, top + bounds.height(), textPaint)
        }


        /////////////////////////////////////////////////////////////////////////////////
        // karl.kwon

/*        var paint = Paint()


        //  paint.setColor(Color.YELLOW);
        //  val drawableRect = RectF(0F, 0F, 900F, 600F)
        //  canvas.drawRect(drawableRect, paint)

        paint.setColor(Color.BLACK);
        val rect2 = RectF(50F, 50F, 300F, 300F)
        canvas.drawArc(rect2, 0F, 360F, true, paint)
*/

        // karl.kwon
        /////////////////////////////////////////////////////////////////////////////////

        draw_eye(canvas, 170, 250, car_center_x, car_center_y);
        draw_eye(canvas, 570,  250,  car_center_x, car_center_y);

    }

    //
    //  Duke
    //
    fun draw_eye (
        canvas: Canvas,
        eye_x : Int,
        eye_y : Int,
        point_x : Int,
        point_y : Int
    )
    {

        var distance_x = point_x - eye_x;
        var distance_y = point_y - eye_y;


        var distance = min( sqrt( distance_x.toDouble()  * distance_x.toDouble()  + distance_y.toDouble() * distance_y.toDouble() ), 80.0)
        var angle = atan2( distance_y.toDouble() , distance_x.toDouble() )

        var pupil_x = eye_x + (cos(angle) * distance)
        var pupil_y = eye_y + (sin(angle) * distance)


        var paint = Paint();

        //  pygame.draw.circle(screen, (255, 255, 255), [eye_x, eye_y], 160 )
        paint.setColor(Color.BLACK);
        val rect1 = RectF( eye_x - 120F, eye_y - 120F, eye_x + 120F, eye_y + 120F);
        canvas.drawArc(rect1, 0F, 360F, true, paint);

        //  pygame.draw.circle(screen, (0, 0, 100), [pupil_x, pupil_y], 50 )
        paint.setColor(Color.YELLOW);

        val rect2 = RectF( pupil_x.toFloat() - 30F, pupil_y.toFloat() - 30F, pupil_x.toFloat() + 30F, pupil_y.toFloat() + 30F);
        canvas.drawArc(rect2, 0F, 360F, true, paint);

    }


    fun setResults(
      detectionResults: MutableList<Detection>,
      imageHeight: Int,
      imageWidth: Int,
    ) {
        results = detectionResults

        // PreviewView is in FILL_START mode. So we need to scale up the bounding box to match with
        // the size that the captured images will be displayed.
        scaleFactor = max(width * 1f / imageWidth, height * 1f / imageHeight)
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}
