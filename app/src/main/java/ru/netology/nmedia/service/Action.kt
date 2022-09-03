package ru.netology.nmedia.service

internal enum class Action(
    val key: String
) {
    Id("ID"),
    Author("AUTHOR"),
    Content("CONTENT"),
    Published("PUBLISHED"),
    Like("LIKE"),
    LikedByMe("LIKE_BY_ME"),
    Shares("SHARES"),
    IsReposted("IS_REPOSTED"),
    ViewCount("VIEW_COUNT"),
    Video("STRING");

    companion object{
        const val KEY = "action"
    }

}