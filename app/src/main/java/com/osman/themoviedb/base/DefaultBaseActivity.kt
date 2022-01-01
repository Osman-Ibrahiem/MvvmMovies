package com.osman.themoviedb.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class DefaultBaseActivity : AppCompatActivity {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    companion object {
        private const val KEY_ACTIVITY_SAVED_STATE = "activity_saved_state"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val savedState = savedInstanceState ?: intent?.getBundleExtra(KEY_ACTIVITY_SAVED_STATE)
        intent?.removeExtra(KEY_ACTIVITY_SAVED_STATE)
        super.onCreate(savedState)
        resetTitle()

//        KeyboardUtils.fixAndroidBug5497(this)
//        KeyboardUtils.fixSoftInputLeaks(this)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        val view = currentFocus
        val ret = super.dispatchTouchEvent(event)
        if (view is EditText) {
            val w = currentFocus
            val scrcoords = IntArray(2)
            if (w == null) return false
            w.getLocationOnScreen(scrcoords)
            val x = event.rawX + w.left - scrcoords[0]
            val y = event.rawY + w.top - scrcoords[1]
            if (event.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right
                        || y < w.top || y > w.bottom)
            ) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
            }
        }
        return ret
    }

    /**
     * updates the toolbar text locale if it set from the android:label property of Manifest
     */
    private fun resetTitle() {
        try {
            val label = packageManager.getActivityInfo(
                componentName,
                PackageManager.GET_META_DATA
            ).labelRes
            if (label != 0) {
                setTitle(label)
            }
        } catch (e: Exception) {
        }
    }

    override fun recreate() {
        try {
            restart()
        } catch (ex: Exception) {
            super.recreate()
        }
    }

    open fun restart() {
        val bundle = Bundle()
        onSaveInstanceState(bundle)
        if (intent == null) {
            intent = Intent(this, javaClass)
        }
        intent.putExtra(KEY_ACTIVITY_SAVED_STATE, bundle)
        finish()
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
