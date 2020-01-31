package ca.ciccc.wmad.kaden.movcura.database.favorite

import android.content.Context
import androidx.room.*

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class FavoriteDB : RoomDatabase() {

    abstract val favoriteDBDao: FavoriteDBDao

    companion object {

        @Volatile
        private var INSTANCE: FavoriteDB? = null

        fun getInstance(context: Context): FavoriteDB {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteDB::class.java,
                        "favorite_movies_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}

@Dao
interface FavoriteDBDao {

    @Insert
    fun insert(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * FROM favorite_movies_table ORDER BY _id DESC")
    fun getAllFavoriteMoviesById(): List<Favorite>

    @Query("SELECT * FROM favorite_movies_table ORDER BY _id DESC LIMIT 1")
    fun getLatestFavoriteMovie(): Favorite?

    @Query("SELECT * FROM favorite_movies_table ORDER BY movie_id")
    fun getAllFavoriteMoviesByMovieId(): List<Favorite>
}

@Entity(tableName = "favorite_movies_table")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    var _id: Long = 0L,

    @ColumnInfo(name = "movie_id")
    val movieID: Long = -_id,

    @ColumnInfo(name = "movie_title")
    val movieTitle: String = "",

    @ColumnInfo(name = "poster_path")
    val posterPath: String = ""
)

val comparatorByIdDesc: Comparator<Favorite> = Comparator { f1, f2 ->
    ((f2._id - f1._id).toInt())
}

val comparatorByMovieId: Comparator<Favorite> = Comparator { f1, f2 ->
    ((f1.movieID - f2.movieID).toInt())
}
