package dev.kxxcn.maru.view.landmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.source.api.dto.DirectionDto
import dev.kxxcn.maru.util.extension.km
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LandmarkViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _snackbarText = MutableLiveData<Event<String?>>()
    val snackbarText: LiveData<Event<String?>> = _snackbarText

    private val _directionEvent = MutableLiveData<Event<DirectionDto?>>()
    val directionEvent: LiveData<Event<DirectionDto?>> = _directionEvent

    private val _bottomTitle = MutableLiveData<String>()
    val bottomTitle: LiveData<String> = _bottomTitle

    private val _bottomDistance = MutableLiveData<Int>()
    val bottomDistance: LiveData<Int> = _bottomDistance

    private val _bottomTime = MutableLiveData<Long>()
    val bottomTime: LiveData<Long> = _bottomTime

    private val _bottomTaxiFare = MutableLiveData<Int>()
    val bottomTaxiFare: LiveData<Int> = _bottomTaxiFare

    private val _bottomTollFare = MutableLiveData<Int>()
    val bottomTollFare: LiveData<Int> = _bottomTollFare

    private val _bottomFuelPrice = MutableLiveData<Int>()
    val bottomFuelPrice: LiveData<Int> = _bottomFuelPrice

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun close() {
        _closeEvent.value = Event(Unit)
    }

    fun direction(
        longitude: Double,
        latitude: Double,
        filterType: LandmarkFilterType
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.getDirection(
                "$longitude, $latitude",
                "${filterType.longitude}, ${filterType.latitude}"
            )
            if (result is Success) {
                if (result.data?.code == 0) {
                    val summary = result.data.route.traoptimal[0].summary
                    _bottomTitle.value = filterType.title
                    _bottomDistance.value = summary.distance.km
                    _bottomTime.value =
                        TimeUnit.MILLISECONDS.toMinutes(summary.duration.toLong())
                    _bottomTaxiFare.value = summary.taxiFare
                    _bottomTollFare.value = summary.tollFare
                    _bottomFuelPrice.value = summary.fuelPrice
                    _directionEvent.value = Event(result.data)
                } else {
                    _snackbarText.value = Event(result.data?.message)
                }
            }
            _isLoading.value = false
        }
    }
}
