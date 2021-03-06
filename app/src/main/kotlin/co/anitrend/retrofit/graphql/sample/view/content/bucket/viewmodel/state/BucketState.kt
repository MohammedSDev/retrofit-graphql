package co.anitrend.retrofit.graphql.sample.view.content.bucket.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.data.model.UserInterfaceState
import co.anitrend.retrofit.graphql.data.bucket.usecase.BucketUseCaseContract
import co.anitrend.retrofit.graphql.domain.entities.bucket.BucketFile

class BucketState(
    private val useCase: BucketUseCaseContract
) : ISupportViewModelState<List<BucketFile>> {

    private val useCaseResult = MutableLiveData<UserInterfaceState<List<BucketFile>>>()

    override val model =
        Transformations.switchMap(useCaseResult) { it.model }
    override val networkState =
        Transformations.switchMap(useCaseResult) { it.networkState }
    override val refreshState =
        Transformations.switchMap(useCaseResult) { it.refreshState }

    operator fun invoke() {
        val result = useCase()
        useCaseResult.postValue(result)
    }

    /**
     * Called upon [androidx.lifecycle.ViewModel.onCleared] and should optionally
     * call cancellation of any ongoing jobs.
     *
     * If your use case source is of type [co.anitrend.arch.domain.common.IUseCase]
     * then you could optionally call [co.anitrend.arch.domain.common.IUseCase.onCleared] here
     */
    override fun onCleared() {
        useCase.onCleared()
    }

    /**
     * Triggers use case to perform refresh operation
     */
    override fun refresh() {
        val uiModel = useCaseResult.value
        uiModel?.refresh?.invoke()
    }

    /**
     * Triggers use case to perform a retry operation
     */
    override fun retry() {
        val uiModel = useCaseResult.value
        uiModel?.retry?.invoke()
    }
}