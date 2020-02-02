package com.urgentx.randomusers.model

import android.os.Parcelable
import androidx.paging.PagedList
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(primaryKeys = ["first", "last", "value"])
data class User(
    val gender: String = "",
    @Embedded val name: Name = Name("", "", ""),
    @Embedded val dob: DOB = DOB(Date(), 1),
    val email: String = "",
    @Embedded val picture: Picture = Picture("", "", ""),
    @Embedded val id: ID = ID("", "")
) : Parcelable

@Parcelize
data class Name(val title: String, val first: String, val last: String) : Parcelable {
    fun contains(query: String) = "$title$first$last".contains(query)
}

@Parcelize
data class DOB(val date: Date, val age: Int) : Parcelable

@Parcelize
data class Picture(val large: String?, val medium: String?, val thumbnail: String?) : Parcelable {
    fun highestRes() = large ?: medium ?: thumbnail
}

@Parcelize
data class ID(val name: String, val value: String = "") : Parcelable

class DateConverter {

    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}

