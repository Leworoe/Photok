/*
 *   Copyright 2020-2021 Leon Latsch
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package dev.leonlatsch.photok.ui.gallery

import android.app.Application
import android.view.View
import androidx.databinding.Bindable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.leonlatsch.photok.BR
import dev.leonlatsch.photok.model.repositories.PhotoRepository
import dev.leonlatsch.photok.ui.components.bindings.ObservableViewModel
import javax.inject.Inject

/**
 * ViewModel for the Gallery.
 * Holds a Flow for the photos.
 *
 * @since 1.0.0
 * @author Leon Latsch
 */
@HiltViewModel
class GalleryViewModel @Inject constructor(
    app: Application,
    val photoRepository: PhotoRepository
) : ObservableViewModel(app) {

    @get:Bindable
    var placeholderVisibility: Int = View.VISIBLE
        set(value) {
            field = value
            notifyChange(BR.placeholderVisibility, value)
        }

    @get:Bindable
    var labelsVisibility: Int = View.GONE
        set(value) {
            field = value
            notifyChange(BR.labelsVisibility, value)
        }

    val photos = Pager(
        PagingConfig(
            pageSize = PAGE_SIZE,
            maxSize = MAX_SIZE,
        )
    ) {
        photoRepository.getAllPaged()
    }.flow

    fun togglePlaceholder(itemCount: Int) {
        if (itemCount > 0) {
            labelsVisibility = View.VISIBLE
            placeholderVisibility = View.GONE
        } else {
            placeholderVisibility = View.VISIBLE
            labelsVisibility = View.GONE
        }
    }

    companion object {
        private const val PAGE_SIZE = 100
        private const val MAX_SIZE = 800
    }
}