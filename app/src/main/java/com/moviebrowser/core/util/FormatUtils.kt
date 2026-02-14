package com.moviebrowser.core.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Utility object for formatting-related helper functions.
 *
 * Contains extension functions that help transform raw data
 * into UI-friendly formats.
 */
object FormatUtils {

    /**
     * Extension function on nullable String to safely convert
     * a date from "yyyy-MM-dd" format (API format)
     * into "dd MMM yyyy" format (UI-friendly format).
     *
     * Example:
     * "2023-03-15" -> "15 Mar 2023"
     *
     * Returns an empty string if:
     * - The input is null
     * - The input is empty
     * - Parsing fails due to invalid format
     */
    fun String?.changeDateFormat(): String {

        // Prevents parsing if the string is null or empty
        if (this.isNullOrEmpty()) return ""

        return try {

            // Formatter matching API date format
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            // Formatter for displaying date in UI
            val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

            // Parse input string and reformat it
            LocalDate.parse(this, inputFormatter)
                .format(outputFormatter)

        } catch (e: Exception) {

            // In case of parsing error, return empty string
            ""
        }
    }

}
