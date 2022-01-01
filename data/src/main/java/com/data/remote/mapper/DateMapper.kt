package com.data.remote.mapper

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DateMapper @Inject constructor() : Mapper<String?, String> {

    override fun mapFromRemote(
        time: String?,
    ): String {
        return if (!time.isNullOrEmpty()) {
            val simpleDateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = try {
                simpleDateFormatInput.parse(time)
            } catch (e: Exception) {
                null
            } ?: return ""
            val simpleDateFormatOutput = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            simpleDateFormatOutput.format(date)
        } else ""
    }
}
