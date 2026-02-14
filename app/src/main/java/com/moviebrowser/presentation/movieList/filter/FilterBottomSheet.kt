package com.moviebrowser.presentation.movieList.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.moviebrowser.R
import com.moviebrowser.domain.model.params.MovieFilterParams
import com.moviebrowser.domain.model.params.SortParam
import com.moviebrowser.ui.theme.Accent
import com.moviebrowser.ui.theme.Primary
import java.time.LocalDate
import androidx.compose.ui.res.stringResource
import com.moviebrowser.ui.theme.Dimens

/**
 * Bottom sheet composable for applying movie filters.
 *
 * Allows user to:
 * - Select sorting option
 * - Set minimum rating
 * - Enter release year
 *
 * Emits updated MovieFilterParams when applied.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    currentFilters: MovieFilterParams,
    onApply: (MovieFilterParams) -> Unit,
    onDismiss: () -> Unit
) {

    // Local UI state initialized from current filters
    var minRating by remember { mutableStateOf(currentFilters.minRating) }
    var releaseYear by remember { mutableStateOf(currentFilters.releaseYear?.toString() ?: "") }
    var sortParam by remember { mutableStateOf(currentFilters.sort) }

    // Holds validation error message for release year
    var releaseYearError by remember { mutableStateOf<String?>(null) }

    val currentYear = LocalDate.now().year
    val context = LocalContext.current

    /**
     * Validates release year input.
     *
     * Rules:
     * - Must be 4 digits
     * - Must be between 1900 and current year
     */
    fun validateYear(year: String): String? {
        if (year.isEmpty()) return null

        if (year.length < 4)
            return context.getString(R.string.year_validation_full)

        val yearInt = year.toIntOrNull()
            ?: return context.getString(R.string.invalid_year)

        if (yearInt < 1900) return context.getString(R.string.invalid_year)
        if (yearInt > currentYear) return context.getString(R.string.invalid_year)

        return null
    }

    // Apply button enabled only when no validation error
    val isApplyEnabled = releaseYearError == null

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(
            topStart = Dimens.xxl,
            topEnd = Dimens.xxl
        ),
        containerColor = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.xl)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.titleLarge,
                color = Primary
            )

            Spacer(Modifier.height(Dimens.xl))

            // ---------------- SORT SECTION ----------------

            Text(
                text = stringResource(R.string.sort_by),
                style = MaterialTheme.typography.titleMedium,
                color = Primary
            )

            Spacer(Modifier.height(Dimens.sm))

            // Dynamically render radio buttons for each SortParam
            SortParam.entries.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = sortParam == option,
                        onClick = { sortParam = option },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Primary
                        )
                    )
                    Text(
                        text = option.name.replace("_", " "),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(Modifier.height(Dimens.xl))

            // ---------------- MIN RATING SECTION ----------------

            Text(
                text = stringResource(
                    R.string.minimum_rating,
                    minRating.toInt()
                ),
                style = MaterialTheme.typography.titleMedium,
                color = Primary
            )

            Slider(
                value = minRating,
                onValueChange = { minRating = it },
                valueRange = 0f..10f,
                steps = 9,
                colors = SliderDefaults.colors(
                    thumbColor = Accent,
                    activeTrackColor = Primary,
                    inactiveTrackColor = Color.LightGray
                )
            )

            Spacer(Modifier.height(Dimens.xl))

            // ---------------- RELEASE YEAR SECTION ----------------

            OutlinedTextField(
                value = releaseYear,
                onValueChange = { input ->
                    if (input.length <= 4 && input.all { it.isDigit() }) {
                        releaseYear = input
                        releaseYearError = validateYear(input)
                    }
                },
                label = {
                    Text(stringResource(R.string.release_year))
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.md),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = releaseYearError != null,
                supportingText = {
                    if (releaseYearError != null) {
                        Text(releaseYearError!!)
                    }
                }
            )

            Spacer(Modifier.height(Dimens.radiusXLarge))

            // ---------------- ACTION BUTTONS ----------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Reset filters to default state
                Text(
                    text = stringResource(R.string.reset),
                    color = Color.Gray,
                    modifier = Modifier
                        .clickable {
                            releaseYear = ""
                            releaseYearError = null
                            minRating = 0f
                            sortParam = SortParam.entries.first()

                            onApply(MovieFilterParams())
                        }
                        .padding(Dimens.sm)
                )

                // Apply filters button
                Card(
                    shape = RoundedCornerShape(Dimens.radiusXLarge),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (isApplyEnabled) Primary
                            else Color.LightGray
                    ),
                    modifier = Modifier
                        .clickable(enabled = isApplyEnabled) {
                            if (isApplyEnabled) {
                                onApply(
                                    MovieFilterParams(
                                        minRating = minRating,
                                        releaseYear = releaseYear.toIntOrNull(),
                                        sort = sortParam
                                    )
                                )
                            }
                        }
                ) {
                    Text(
                        text = stringResource(R.string.apply),
                        color = Color.White,
                        modifier = Modifier.padding(
                            horizontal = Dimens.xxl,
                            vertical = Dimens.md
                        )
                    )
                }
            }

            Spacer(Modifier.height(Dimens.lg))
        }
    }
}
