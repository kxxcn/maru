package dev.kxxcn.maru.view.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.view.base.BaseViewModel
import javax.inject.Inject

class TimelineViewModel @Inject constructor(
    repository: DataRepository
) : BaseViewModel() {

    val items: LiveData<List<TimelineRow>> = repository.observeSummary().map { summaries ->
        val summary = summaries.firstOrNull()
        TimelineRow.build(TimelineItem.create(), summary?.user?.wedding, summary?.weddingRemainDays)
    }
}
