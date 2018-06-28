package com.oldmen.stayintouch.presentation.ui.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Interpolator
import android.widget.ImageView
import com.oldmen.stayintouch.R
import com.oldmen.stayintouch.presentation.mvp.main.MainPresenter

/**
 * [android.view.View.OnClickListener] used to translate the product grid sheet downward on
 * the Y-axis when the navigation icon in the toolbar is pressed.
 */
class NavigationIconClickListener @JvmOverloads internal constructor(
        private val context: Context, private val sheet: View, private val interpolator: Interpolator? = null,
        private val openIcon: Drawable? = null, private val closeIcon: Drawable? = null,
        private val presenter: MainPresenter) : View.OnClickListener {

    private val animatorSet = AnimatorSet()
    private val height: Int
    private var backdropShown = false

    init {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    override fun onClick(view: View) {
        backdropShown = !backdropShown

        // Cancel the existing animations
        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()

        updateIcon(view)

        val translateY = height - context.resources.getDimensionPixelSize(R.dimen.articles_list_reveal_height_portrait)

        val animator = ObjectAnimator.ofFloat(sheet, "translationY",
                (if (backdropShown) translateY else 0).toFloat())
        animator.duration = 500
        if (interpolator != null) {
            animator.interpolator = interpolator
        }
        animatorSet.play(animator)
        animator.start()

        if (!backdropShown) presenter.loadArticlesIfNeeded()
    }

    private fun updateIcon(view: View) {
        if (openIcon != null && closeIcon != null) {
            if (view !is ImageView) {
                throw IllegalArgumentException("updateIcon() must be called on an ImageView")
            }
            if (backdropShown) {
                view.setImageDrawable(closeIcon)
            } else {
                view.setImageDrawable(openIcon)
            }
        }
    }
}
