package com.kizitonwose.calendarview.ui

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.annotation.Px
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.utils.inflate

internal data class WeekStartEndConfig(
    @Px val width: Int,
    @LayoutRes val weekStartEndRes: Int,
    val viewBinder: WeekStartEndBinder<ViewContainer>?
)

internal class WeekStartEndHolder(private val config: WeekStartEndConfig) {

    private lateinit var startEndView: View
    private lateinit var containerView: FrameLayout
    private lateinit var viewContainer: ViewContainer

    /**
     * CalendarDay for start or end of the week (or this month)
     */
    var day: CalendarDay? = null

    fun inflateWeekStartEndView(parent: LinearLayout): View {
        startEndView = parent.inflate(config.weekStartEndRes).apply {
            // We ensure the layout params of the supplied child view is
            // MATCH_PARENT so it fills the parent container.
            layoutParams = layoutParams.apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
                width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
        containerView = FrameLayout(parent.context).apply {
            // This will be placed in the start or end in WeekLayout(A LinearLayout) with width
            layoutParams = LinearLayout.LayoutParams(config.width, ViewGroup.LayoutParams.MATCH_PARENT)
            addView(startEndView)
        }
        return containerView
    }

    fun bindStarEndWeekView(currentDay: CalendarDay?) {
        this.day = currentDay
        if (!::viewContainer.isInitialized) {
            viewContainer = config.viewBinder!!.create(startEndView)
        }

        val dayHash = currentDay?.date.hashCode()
        if (containerView.id != dayHash) {
            containerView.id = dayHash
        }

        if (currentDay != null) {
            if (containerView.visibility != View.VISIBLE) {
                containerView.visibility = View.VISIBLE
            }
            config.viewBinder!!.bind(viewContainer, currentDay)
        } else if (containerView.visibility != View.GONE) {
            containerView.visibility = View.GONE
        }
    }

    fun reloadView() {
        bindStarEndWeekView(day)
    }
}
