package com.domain.common.model

import android.graphics.Color
import com.domain.core.model.BaseModel

data class Movie(
    val id: Long,
    val title: String,
    val poster: String,
    val date: String,
    val votes: Int,
    val votesColor: String,
    val votesString: String,
) : BaseModel