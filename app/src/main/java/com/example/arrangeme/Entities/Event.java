package com.example.arrangeme.Entities;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.arrangeme.R;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Calendar;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/**
 * This entity represents events in weekly and daily calendars
 */
public class Event implements WeekViewDisplayable<Event> {
    @NotNull
    private final String id;
    private final String title;
    private final Calendar startTime;
    private final Calendar endTime;
    private final String location;
    private final int color;
    private final boolean isAllDay;
    private final boolean isCanceled;

    public Event(@NotNull String id, @NotNull String title, @NotNull Calendar startTime, @NotNull Calendar endTime, @NotNull String location, int color, boolean isAllDay, boolean isCanceled) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.color = color;
        this.isAllDay = isAllDay;
        this.isCanceled = isCanceled;
    }


    @NotNull
    @Override
    public WeekViewEvent toWeekViewEvent() {
        int backgroundColor = !this.isCanceled ? this.color : -1;
        int textColor = !this.isCanceled ? -1 : this.color;
        int borderWidthResId = !this.isCanceled ? R.dimen.border_width : R.dimen.border_width;
        WeekViewEvent.Style style = (new WeekViewEvent.Style.Builder()).setTextColor(textColor).setBackgroundColor(backgroundColor).setTextStrikeThrough(this.isCanceled).setBorderWidthResource(borderWidthResId).setBorderColor(this.color-10).build();
        return (new WeekViewEvent.Builder(this)).setId(Long.parseLong(this.id)).setTitle((CharSequence)this.title).setStartTime(this.startTime).setEndTime(this.endTime).setLocation((CharSequence)this.location).setAllDay(this.isAllDay).setStyle(style).build();

    }

    @NotNull
    public final String getId() {
        return this.id;
    }

    @NotNull
    public final String getTitle() {
        return this.title;
    }
}