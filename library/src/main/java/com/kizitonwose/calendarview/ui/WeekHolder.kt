package com.kizitonwose.calendarview.ui

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner

internal class WeekHolder(
    dayConfig: DayConfig,
    startWeekConfig: WeekStartEndConfig?,
    endWeekConfig: WeekStartEndConfig?
) {

    val startWeekHolder = startWeekConfig?.takeIf { it.width > 0 }?.let {
        WeekStartEndHolder(it)
    }
    val dayHolders = (1..7).map { DayHolder(dayConfig) }
    val endWeekHolder = endWeekConfig?.takeIf { it.width > 0 }?.let {
        WeekStartEndHolder(it)
    }

    private lateinit var container: LinearLayout

    fun inflateWeekView(parent: LinearLayout): View {
        container = LinearLayout(parent.context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
            weightSum = dayHolders.count().toFloat()

            startWeekHolder?.also {
                addView(it.inflateWeekStartEndView(this))
            }

            for (holder in dayHolders) {
                addView(holder.inflateDayView(this))
            }

            endWeekHolder?.also {
                addView(it.inflateWeekStartEndView(this))
            }

        }
        return container
    }

    fun bindWeekView(daysOfWeek: List<CalendarDay>) {
        container.visibility = if (daysOfWeek.isEmpty()) View.GONE else View.VISIBLE
        startWeekHolder?.also {
            //set's the first week day, or first day of this month, or null
            it.bindStarEndWeekView(daysOfWeek.firstOrNull { day -> day.owner == DayOwner.THIS_MONTH })
        }
        dayHolders.forEachIndexed { index, holder ->
            // Indices can be null if OutDateStyle is NONE. We set the
            // visibility for the views at these indices to INVISIBLE.
            holder.bindDayView(daysOfWeek.getOrNull(index))
        }
        endWeekHolder?.also {
            //set's the last week day, or last day of this month, or null
            it.bindStarEndWeekView(daysOfWeek.lastOrNull { day -> day.owner == DayOwner.THIS_MONTH })
        }
    }
}
