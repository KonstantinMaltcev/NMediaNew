package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {

    @Query("UPDATE posts SET shares = shares + 1 WHERE id = :id")
    fun share(id: Long)

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE posts SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    @Query(
        """
        UPDATE posts SET
        likes = likes + CASE WHEN like_by_me THEN -1 ELSE 1 END,
        like_by_me = CASE WHEN like_by_me THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun like(id: Long)

    @Query("DELETE FROM posts WHERE id = :id")
    fun delete(id: Long)

}