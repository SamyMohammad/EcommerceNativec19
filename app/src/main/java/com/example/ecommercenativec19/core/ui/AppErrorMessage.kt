package com.example.ecommercenativec19.core.ui

import androidx.annotation.StringRes
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.common.AppError

/** Maps a domain error to user-facing copy here, so the data and domain layers stay free of Android resources. */
@StringRes
fun AppError.toMessageRes(): Int = when (this) {
    AppError.Network -> R.string.error_network
    is AppError.Server -> R.string.error_server
    AppError.Parsing -> R.string.error_parsing
    AppError.Unknown -> R.string.error_unknown
}
