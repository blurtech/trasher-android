package tech.blur.trasher.common.utils

import android.text.format.DateFormat

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val TAG = "DateUtils"

    val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"


    val currentDate: String
        get() {
            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val today = Calendar.getInstance().time
            return dateFormat.format(today)
        }

    val currentTime: String
        get() {
            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val today = Calendar.getInstance().time
            return dateFormat.format(today)
        }

    /**
     * @param time        in milliseconds (Timestamp)
     * @return
     */
    fun getDateTimeFromTimeStamp(time: Long): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = Date(time)
        return dateFormat.format(dateTime)
    }

    /**
     * Get Timestamp from date and time
     *
     * @param mDateTime   datetime String
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun getTimeStampFromDateTime(mDateTime: String): Long {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(mDateTime)
        return date.time
    }

    /**
     * Return  datetime String from date object
     *
     * @param mDateFormat format of date
     * @param date        date object that you want to parse
     * @return
     */
    fun formatDateTimeFromDate(mDateFormat: String, date: Date?): String? {
        return if (date == null) {
            null
        } else DateFormat.format(mDateFormat, date).toString()
    }

    /**
     * Convert one date format string  to another date format string in android
     * @param inputDateFormat Input SimpleDateFormat
     * @param outputDateFormat Output SimpleDateFormat
     * @param inputDate  input Date String
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun formatDateFromDateString(
        inputDateFormat: String,
        outputDateFormat: String,
        inputDate: String
    ): String {
        val mParsedDate: Date
        val mOutputDateString: String
        val mInputDateFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())
        val mOutputDateFormat = SimpleDateFormat(outputDateFormat, Locale.getDefault())
        mParsedDate = mInputDateFormat.parse(inputDate) ?: throw ParseException("error", -1)
        mOutputDateString = mOutputDateFormat.format(mParsedDate)
        return mOutputDateString
    }
}