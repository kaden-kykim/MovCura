package ca.ciccc.wmad.kaden.movcura.global

import ca.ciccc.wmad.kaden.movcura.network.TMDbAPI
import com.google.gson.annotations.SerializedName

object TMDbConfiguration {

    var data: TMDbConfData? = null
        private set

    lateinit var baseImageUrl: String
        private set

    suspend fun setConfData(onResponse: ((Boolean) -> Unit)) {
        val getConfDataDeferred = TMDbAPI.retrofitService.getConfigurationAsync()
        try {
            data = getConfDataDeferred.await()
            baseImageUrl = data!!.images.secureBaseUrl
            onResponse(true)
        } catch (e: Exception) {
            baseImageUrl = "https://image.tmdb.org/t/p/"
            onResponse(false)
        }

    }
}

data class TMDbConfData(
    @SerializedName("images")
    val images: Images
) {
    data class Images(
        @SerializedName("base_url")
        val baseUrl: String,
        @SerializedName("secure_base_url")
        val secureBaseUrl: String,
        @SerializedName("backdrop_sizes")
        val backdropSizes: List<String>,
        @SerializedName("poster_sizes")
        val posterSizes: List<String>,
        @SerializedName("profile_sizes")
        val profileSizes: List<String>
    ) {
        override fun toString(): String {
            return "BaseUrl: $baseUrl,\nSecureBaseUrl: $secureBaseUrl\n"
        }
    }

    override fun toString(): String {
        return "$images"
    }
}