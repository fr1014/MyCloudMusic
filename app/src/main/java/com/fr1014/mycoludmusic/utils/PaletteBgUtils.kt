package com.fr1014.mycoludmusic.utils

import android.graphics.*
import android.widget.ImageView
import androidx.palette.graphics.Palette

class PaletteBgUtils {
    companion object {
        private val mCanvas = Canvas()
        private val mPaint = Paint()

        fun paletteTopBg(bg: ImageView, bitmap: Bitmap) {
            Palette.from(bitmap).generate(object : Palette.PaletteAsyncListener {
                override fun onGenerated(palette: Palette?) {
                    //记得判空
                    if (palette == null) return;
                    when {
                        palette.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                            createLinearGradientBitmap(
                                    bg, palette.getDarkMutedColor(Color.TRANSPARENT),
                                    palette.getDarkMutedColor(
                                            Color.TRANSPARENT
                                    )
                            )
                        }
                        palette.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                            createLinearGradientBitmap(
                                    bg, palette.getDarkVibrantColor(Color.TRANSPARENT),
                                    palette.getDarkVibrantColor(
                                            Color.TRANSPARENT
                                    )
                            )
                        }
                        else -> {
                            createLinearGradientBitmap(
                                    bg, palette.getLightVibrantColor(Color.TRANSPARENT),
                                    palette.getLightVibrantColor(
                                            Color.TRANSPARENT
                                    )
                            )
                        }
                    }

                }
            })
        }

        fun paletteDownBg(bg: ImageView, bitmap: Bitmap) {
            Palette.from(bitmap).generate(object : Palette.PaletteAsyncListener {
                override fun onGenerated(palette: Palette?) {
                    //记得判空
                    if (palette == null) return;
                    when {
                        palette.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                            createLinearGradientBitmap(
                                    bg, palette.getDarkMutedColor(Color.TRANSPARENT),
                                    palette.getMutedColor(
                                            Color.TRANSPARENT
                                    )
                            )
                        }
                        palette.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                            createLinearGradientBitmap(
                                    bg, palette.getDarkVibrantColor(Color.TRANSPARENT),
                                    palette.getVibrantColor(
                                            Color.TRANSPARENT
                                    )
                            )
                        }
                        else -> {
                            createLinearGradientBitmap(
                                    bg, palette.getLightVibrantColor(Color.TRANSPARENT),
                                    palette.getLightMutedColor(
                                            Color.TRANSPARENT
                                    )
                            )
                        }
                    }
                }

            })
        }

        //创建线性渐变背景色
        private fun createLinearGradientBitmap(bg: ImageView, darkColor: Int, color: Int) {
            val bgColors = IntArray(2)
            bgColors[0] = darkColor
            bgColors[1] = color
            val bgBitmap = Bitmap.createBitmap(bg.width, bg.height, Bitmap.Config.ARGB_4444)
            mCanvas.setBitmap(bgBitmap)
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            val gradient: LinearGradient? = bgBitmap?.height?.let {
                LinearGradient(
                        0f, 0f, 0f,
                        it.toFloat(), bgColors[0], bgColors[1], Shader.TileMode.CLAMP
                )
            }
            mPaint.shader = gradient
            val rectF = RectF(0f, 0f, bgBitmap.width.toFloat(), bgBitmap.height.toFloat())
            mCanvas.drawRect(rectF, mPaint)
            bg.setImageBitmap(bgBitmap)
        }
    }
}
