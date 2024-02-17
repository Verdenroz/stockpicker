import com.farmingdale.stockscreener.model.remote.newsResponses.ArticleResponse
import kotlinx.serialization.Serializable

/**
 * Data response for the news API
 * @param articles list of [ArticleResponse]
 */
@Serializable
data class NewsResponse(
    val articles: List<ArticleResponse>
)