package tech.blur.trasher.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.PendingResults
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import tech.blur.trasher.common.Result
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.common.toResult
import tech.blur.trasher.data.TrashRepository
import tech.blur.trasher.data.api.TrasherApi
import tech.blur.trasher.domain.Trashcan
import tech.blur.trasher.domain.Trashlist
import tech.blur.trasher.presentation.BaseViewModel

class MapViewModel(
    api: TrasherApi,
    schedulerProvider: SchedulerProvider,
    private val trashRepository: TrashRepository,
    private val geoApiContext: GeoApiContext
) : BaseViewModel() {

    private val loadTrashcans = PublishSubject.create<Unit>()

    private val mutableRoute = MutableLiveData<DirectionsResult?>()
    val route: LiveData<DirectionsResult?> = mutableRoute

    private val mutableNetworkProgress = MutableLiveData<Boolean>()
    val networkProgress: LiveData<Boolean> = mutableNetworkProgress

    private val mutableTrashcans = MutableLiveData<List<Trashcan>>()
    val trashcans: LiveData<List<Trashcan>> = mutableTrashcans

    init {
        loadTrashcans
            .doOnNext { mutableNetworkProgress.value = true }
            .flatMapSingle { api.getTrashCans().toResult() }
            .flatMapSingle {
                if (it is Result.Success) {
                    trashRepository.trashCans = Trashlist(it.data.data)
                }
                Single.just(it)
            }
            .observeOn(schedulerProvider.ui())
            .subscribe {
                when (it) {
                    is Result.Success -> {
                        mutableTrashcans.value = it.data.data
                    }
                    is Result.Failure -> {
                        when (it.throwable) {
                            is HttpException -> {
//                                if (it.throwable.code() == 401)
//                                    //unauth
//                                else
//
                            }
                            else -> {
                                println(it.throwable.message)
                            }
                        }
                    }
                }

                mutableNetworkProgress.value = false
            }.addTo(compositeDisposable)
    }

    fun loadTrashcans() {
        if (trashRepository.isTrashCansCahsAvailable) {
            mutableTrashcans.value = trashRepository.trashCans.list
        } else {
            loadTrashcans.onNext(Unit)
        }
    }

    fun getRoute(trashcan: Trashcan, origin: LatLng) {
        val apiRequest = DirectionsApi.newRequest(geoApiContext)

        apiRequest.origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
        apiRequest.destination(
            com.google.maps.model.LatLng(
                trashcan!!.latlng.latitude,
                trashcan.latlng.longitude
            )
        )
        apiRequest.mode(TravelMode.WALKING)

        apiRequest.setCallback(object : com.google.maps.PendingResult.Callback<DirectionsResult> {
            override fun onFailure(e: Throwable?) {
                e!!.printStackTrace()
            }

            override fun onResult(result: DirectionsResult?) {
                mutableRoute.postValue(result)
            }

        })
    }


}
