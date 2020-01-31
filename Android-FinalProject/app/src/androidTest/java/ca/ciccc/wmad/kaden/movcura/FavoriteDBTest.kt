package ca.ciccc.wmad.kaden.movcura

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ca.ciccc.wmad.kaden.movcura.database.favorite.Favorite
import ca.ciccc.wmad.kaden.movcura.database.favorite.FavoriteDB
import ca.ciccc.wmad.kaden.movcura.database.favorite.FavoriteDBDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FavoriteDBTest {

    private lateinit var favoriteDao: FavoriteDBDao
    private lateinit var db: FavoriteDB

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, FavoriteDB::class.java)
            .allowMainThreadQueries()
            .build()
        favoriteDao = db.favoriteDBDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetFavorite() {
        val favorite = Favorite()
        favoriteDao.insert(favorite)
        val fav = favoriteDao.getAllFavoriteMoviesById()
        assertEquals(0, fav[0]._id)
    }
}