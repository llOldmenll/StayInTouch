package com.oldmen.stayintouch.data.local

import android.content.Context.MODE_PRIVATE
import com.oldmen.stayintouch.CustomApplication
import com.oldmen.stayintouch.domain.models.UserSession

object UserSessionUtils {
    private const val USER_KEY = "UserSession"
    private const val SORT_BY = "SORT_BY"
    private const val SOURCE = "SOURCE"
    private const val PAGE_SIZE = "PAGE_SIZE"
    private const val PAGE = "PAGE"
    private const val FROM = "FROM"
    private const val TO = "TO"

    fun isSessionCreated(): Boolean = getSharedPref().contains(SORT_BY)

    fun saveSession(session: UserSession) {
        getSharedPref().edit()
                .putString(SORT_BY, session.sortBy)
                .putString(SOURCE, session.source)
                .putInt(PAGE_SIZE, session.pageSize)
                .putInt(PAGE, session.page)
                .putString(FROM, session.from)
                .putString(TO, session.to)
                .apply()

    }

    fun getSession(): UserSession {
        val sharedPref = getSharedPref()
        return UserSession(
                sortBy = sharedPref.getString(SORT_BY, "publishedAt"),
                source = sharedPref.getString(SOURCE, "abc-news"),
                pageSize = sharedPref.getInt(PAGE_SIZE, 10),
                page = sharedPref.getInt(PAGE, 1),
                from = sharedPref.getString(FROM, ""),
                to = sharedPref.getString(TO, ""))
    }

    fun setSortBy(sort: String) {
        getSharedPref().edit().putString(SORT_BY, sort).apply()
        setPage(0)
    }

    fun getSortedBy(): String = getSharedPref().getString(SORT_BY, "")

    fun setSource(src: String) {
        getSharedPref().edit().putString(SOURCE, src).apply()
        setPage(0)
    }

    fun getSource(): String = getSharedPref().getString(SOURCE, "")

    fun setPage(page: Int) = getSharedPref().edit().putInt(PAGE, page).apply()

    fun getPage(): Int = getSharedPref().getInt(PAGE, 1)

    fun setFrom(from: String) {
        getSharedPref().edit().putString(FROM, from).apply()
        setPage(0)
    }

    fun getFrom(): String = getSharedPref().getString(FROM, "")

    fun setTo(to: String) {
        getSharedPref().edit().putString(TO, to).apply()
        setPage(0)
    }

    fun getTo(): String = getSharedPref().getString(TO, "")

    fun resetPageCount() = getSharedPref().edit().putInt(PAGE, 1).apply()

    private fun getSharedPref() = CustomApplication.getContext().getSharedPreferences(USER_KEY, MODE_PRIVATE)

}