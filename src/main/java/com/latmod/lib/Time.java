package com.latmod.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;

public final class Time implements Comparable<Time>
{
    public final long millis;
    public final int seconds;
    public final int minutes;
    public final int hours;
    public final int day;
    public final int month;
    public final int year;

    private Time(@Nonnull Calendar c)
    {
        millis = c.getTimeInMillis();
        seconds = c.get(Calendar.SECOND);
        minutes = c.get(Calendar.MINUTE);
        hours = c.get(Calendar.HOUR_OF_DAY);
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
    }

    private static void append00(StringBuilder sb, int i)
    {
        if(i < 10)
        {
            sb.append('0');
        }
        sb.append(i);
    }

    private static void append000(StringBuilder sb, int i)
    {
        if(i < 100)
        {
            sb.append('0');
        }
        if(i < 10)
        {
            sb.append('0');
        }
        sb.append(i);
    }

    public static Time get(@Nonnull Calendar c)
    {
        return new Time(c);
    }

    public static Time get(long millis)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return get(c);
    }

    public static Time get(int year, int month, int day, int hours, int minutes, int seconds, long deltaMillis)
    {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hours, minutes, seconds);
        c.setTimeInMillis(c.getTimeInMillis() + deltaMillis);
        return null;
    }

    public static Time now()
    {
        return get(Calendar.getInstance());
    }

    @Nullable
    public static Time deserialize(@Nonnull JsonElement e)
    {
        if(e.isJsonPrimitive())
        {
            return get(e.getAsLong());
        }

        return null;
    }

    public boolean equalsTime(long t)
    {
        return millis == t;
    }

    @Override
    public int hashCode()
    {
        return Long.valueOf(millis).hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o != null && (o == this || (o instanceof Time && equalsTime(((Time) o).millis)) || (o instanceof Number && equalsTime(((Number) o).longValue())));
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append(',');
        append00(sb, month);
        sb.append(',');
        append00(sb, day);
        sb.append(',');
        append00(sb, hours);
        sb.append(',');
        append00(sb, minutes);
        sb.append(',');
        append00(sb, seconds);
        sb.append(',');
        append000(sb, (int) (millis % 1000L));
        return sb.toString();
    }

    @Override
    public int compareTo(@Nonnull Time o)
    {
        return Long.compare(millis, o.millis);
    }

    // Static //

    public String getTime()
    {
        StringBuilder sb = new StringBuilder();
        append00(sb, hours);
        sb.append(':');
        append00(sb, minutes);
        sb.append(':');
        append00(sb, seconds);
        return sb.toString();
    }

    public String getTimeHMS()
    {
        StringBuilder sb = new StringBuilder();

        if(hours > 0)
        {
            append00(sb, hours);
            sb.append('h');
        }

        if(hours > 0 || minutes > 0)
        {
            append00(sb, minutes);
            sb.append('m');
        }

        append00(sb, seconds);
        sb.append('s');
        return sb.toString();
    }

    public String getDate()
    {
        StringBuilder sb = new StringBuilder();
        append00(sb, day);
        sb.append('.');
        append00(sb, month);
        sb.append('.');
        sb.append(year);
        return sb.toString();
    }

    public String getDateAndTime()
    {
        return getDate() + ' ' + getTime();
    }

    public long getDelta()
    {
        return Math.abs(now().millis - millis);
    }

    public JsonElement getJson()
    {
        return new JsonPrimitive(millis);
    }
}