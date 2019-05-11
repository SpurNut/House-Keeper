package com.spurnut.housekeeper.model

import android.media.Image
import java.util.*

enum class URGENCY_IMPORTANCE_QUADRANT {
    URGENT_IMPORTANT, URGENT_NOT_IMPORTANT,
    NOT_URGENT_IMPORTANT, NOT_URGEND_NOT_IMPORTANT
}

class Task (var title: String, var urgency: URGENCY_IMPORTANCE_QUADRANT, var description: String,
             var images: Array<Image>?, var dueDate: Date?)