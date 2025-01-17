/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel.database

import com.piappstudio.pimodel.*
import com.piappstudio.pimodel.error.PIError
import com.piappstudio.pimodel.database.dao.IEventDao
import com.piappstudio.pimodel.database.dao.IGuestDao
import com.piappstudio.pimodel.database.dao.IMediaDao
import com.piappstudio.pimodel.error.ErrorCode.DATABASE_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PiDataRepository @Inject constructor(private val eventDao:IEventDao,
                                           private val guestDao: IGuestDao,
                                           private val mediaDao:IMediaDao
) {

    suspend fun insert(eventInfo: EventInfo): Flow<Resource<Long?>> {
        return makeSafeApiCall {
            eventDao.insert(eventInfo)
        }
    }
    suspend fun update(eventInfo: EventInfo): Flow<Resource<Unit?>> {
        return makeSafeApiCall {
            eventDao.update(eventInfo)
        }
    }

    suspend fun fetchEvents(): List<EventSummary> {
        return eventDao.fetchEvents()
    }

    suspend fun insert(guestInfo: GuestInfo, lstMediaInfo: List<MediaInfo>): Flow<Resource<Long?>> {
        return makeSafeApiCall {
            val guestId = guestDao.insert(guestInfo)
            for (mediaInfo in lstMediaInfo) {
                val updatedMediaInfo = mediaInfo.copy(guestId = guestId)
                mediaDao.insert(updatedMediaInfo)
            }
            guestId

        }
    }

    suspend fun update(guestInfo: GuestInfo, lstMediaInfo: List<MediaInfo>): Flow<Resource<Unit?>> {
        return makeSafeApiCall {
            guestDao.update(guestInfo)
            // Delete all media items and insert it again
            mediaDao.delete(guestInfo.id)
            for (mediaInfo in lstMediaInfo) {
                val updatedMediaInfo = mediaInfo.copy(guestId = guestInfo.id)
                mediaDao.insert(updatedMediaInfo)
            }
        }
    }

    suspend fun delete(mediaInfo: MediaInfo):Flow<Resource<Unit?>> {
        return makeSafeApiCall {
            mediaDao.delete(mediaInfo)
        }
    }


    suspend fun fetchGuest(eventId:Long): Flow<Resource<List<GuestInfo>?>> {
        return makeSafeApiCall {
            guestDao.fetchGuest(eventId)
        }
    }

    suspend fun fetchGuestMedia(guestId:Long): Flow<Resource<List<MediaInfo>?>> {
        return makeSafeApiCall {
            mediaDao.fetchGuestMedias(guestId = guestId)
        }
    }

    private suspend fun <T> makeSafeApiCall(api: suspend () -> T?) = flow {
        try {
            emit(Resource.loading())
            val response = api.invoke()
            emit(Resource.success(response))
        } catch (ex: Exception) {
            Timber.e(ex)
            emit(Resource.error(error = PIError(code = DATABASE_ERROR, message = ex.message)))
        }
    }

    //region For backup
    fun fetchAllEvent():List<EventInfo> {
        return eventDao.fetchAllEvents()
    }
    fun fetchAllGuest():List<GuestInfo> {
        return guestDao.fetchAllGuest()
    }
    fun fetchAllMedia():List<MediaInfo> {
        return mediaDao.fetchAllMedia()
    }

    fun insertEvents(events: List<EventInfo>):List<Long> {
        return eventDao.insertEvents(events)
    }
    fun insertGuest(guests:List<GuestInfo>):List<Long> {
        return guestDao.insertGuests(guests)
    }

    fun insertMedia(lstMedia:List<MediaInfo>):List<Long> {
        return mediaDao.insertMedias(lstMedia)
    }

    //endregion

}