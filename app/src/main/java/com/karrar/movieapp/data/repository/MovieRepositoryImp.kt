package com.karrar.movieapp.data.repository

import com.karrar.movieapp.data.local.database.daos.MovieDao
import com.karrar.movieapp.data.local.database.entity.SearchHistoryEntity
import com.karrar.movieapp.data.local.database.entity.WatchHistoryEntity
import com.karrar.movieapp.data.remote.State
import com.karrar.movieapp.data.remote.response.*
import com.karrar.movieapp.data.remote.response.movie.RatedMovie
import com.karrar.movieapp.data.remote.response.movie.RatingDto
import com.karrar.movieapp.data.remote.service.MovieService
import com.karrar.movieapp.domain.mappers.*
import com.karrar.movieapp.domain.models.*
import com.karrar.movieapp.utilities.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class MovieRepositoryImp @Inject constructor(
    private val movieService: MovieService,
    private val actorDetailsMapper: ActorDetailsMapper,
    private val actorMapper: ActorMapper,
    private val genreMapper: GenreMapper,
    private val movieMapper: MovieMapper,
    private val tvShowsMapper: TVShowMapper,
    private val searchActorMapper: SearchActorMapper,
    private val seriesMapper: SearchSeriesMapper,
    private val movieDao: MovieDao,
    private val searchHistoryMapper: SearchHistoryMapper,
    private val movieDetailsMapper: MovieDetailsMapper,
    private val reviewMapper: ReviewMapper,
    private val trailerMapper: TrailerMapper,
    private val popularMovieMapper: PopularMovieMapper,
    private val accountMapper: AccountMapper,
    private val ratedMoviesMapper: RatedMoviesMapper,
    private val createdListsMapper: CreatedListsMapper,
) : BaseRepository(), MovieRepository {

    override suspend fun getPopularMovies2(genre: List<Genre>): List<PopularMovie> {
        return wrap2({ movieService.getPopularMovies() },
            { popularMovieMapper.mapGenreMovie(it.items , genre) })
    }

    override suspend fun getMovieGenreList2(): List<Genre> {
        return wrap2({ movieService.getGenreList() },
            { ListMapper(genreMapper).mapList(it.genres) })

    }

    override fun getTrendingActors(): Flow<State<List<Actor>>> {
        return wrap({ movieService.getTrendingActors() }) { response ->
            response.items?.map { actorMapper.map(it) } ?: emptyList()
        }
    }

    override fun getActorDetails(actorId: Int): Flow<State<ActorDetails>> {
        return wrap({ movieService.getActorDetails(actorId) }, { actorDetailsMapper.map(it) })
    }

    override fun getActorMovies(actorId: Int): Flow<State<List<Media>>> {
        return wrap({ movieService.getActorMovies(actorId) }, { actorMoviesDto ->
            actorMoviesDto.cast?.mapNotNull { cast ->
                cast?.let { movieMapper.map(it) }
            } ?: emptyList()
        })
    }

    override fun getUpcomingMovies(): Flow<State<List<Media>>> {
        return wrap({ movieService.getUpcomingMovies() }, { baseResponse ->
            baseResponse.items?.map { movieMapper.map(it) } ?: emptyList()
        })
    }

    override fun getTopRatedMovies(): Flow<State<List<Media>>> {
        return wrap({ movieService.getTopRatedMovies() }, { baseResponse ->
            baseResponse.items?.map { movieMapper.map(it) } ?: emptyList()
        })
    }

    override fun searchForActor(query: String): Flow<State<List<Media>>> {
        return wrap({ movieService.searchForActor(query) }, { response ->
            response.items?.filter { it.knownForDepartment == Constants.ACTING }?.map {
                it.let { searchActorMapper.map(it) }
            } ?: emptyList()
        })
    }

    override fun getNowPlayingMovies(): Flow<State<List<Media>>> {
        return wrap({ movieService.getNowPlayingMovies() }, { response ->
            response.items?.map { movieMapper.map(it) } ?: emptyList()
        })
    }

    override fun searchForMovie(query: String): Flow<State<List<Media>>> {
        return wrap({ movieService.searchForMovie(query) }, { response ->
            response.items?.map { movieMapper.map(it) } ?: emptyList()
        })
    }

    override fun searchForSeries(query: String): Flow<State<List<Media>>> {
        return wrap({ movieService.searchForSeries(query) }, { response ->
            response.items?.map { seriesMapper.map(it) } ?: emptyList()
        })
    }

    override fun getAllSearchHistory(): Flow<List<SearchHistory>> {
        return movieDao.getAllSearchHistory().map { response ->
            response.map { searchHistoryMapper.map(it) }
        }
    }

    override fun getTrendingMovies(): Flow<State<List<Media>>> {
        return wrap({ movieService.getTrendingMovies() }, { response ->
            response.items?.map { movieMapper.map(it) } ?: emptyList()
        })
    }

    override fun getMovieGenreList(): Flow<State<List<Genre>>> {
        return wrap({ movieService.getGenreList() }, { response ->
            response.genres?.map { genreMapper.map(it) } ?: emptyList()
        })
    }

    override fun getMovieListByGenreID(genreID: Int): Flow<State<List<Media>>> {
        return wrap({ movieService.getMovieListByGenre(genreID) }, { response ->
            response.items?.map { movieMapper.map(it) } ?: emptyList()
        })
    }

    override fun getDailyTrending(): Flow<State<List<Media>>> {
        return wrap({ movieService.getDailyTrending() }, { response ->
            response.items?.map { tvShowsMapper.map(it) } ?: emptyList()
        })
    }

    override fun getAllMovies(): Flow<State<List<Media>>> {
        return wrap({ movieService.getAllMovies() }, { response ->
            response.items?.map { movieMapper.map(it) } ?: emptyList()
        })
    }


    override fun getMovieDetails(movieId: Int): Flow<State<MovieDetails>> {
        return wrap({ movieService.getMovieDetails(movieId) }, { response ->
            movieDetailsMapper.map(response)
        })
    }

    override fun getMovieCast(movieId: Int): Flow<State<List<Actor>>> {
        return wrap({ movieService.getMovieCast(movieId) }, { response ->
            response.cast?.map { actorMapper.map(it) } ?: emptyList()
        })
    }

    override fun getSimilarMovie(movieId: Int): Flow<State<List<Media>>> {
        return wrap({ movieService.getSimilarMovie(movieId) }, { response ->
            response.items?.map { movieMapper.map(it) } ?: emptyList()
        })
    }

    override fun getMovieReviews(movieId: Int): Flow<State<List<Review>>> {
        return wrap({ movieService.getMovieReviews(movieId) }, { response ->
            response.items?.map { reviewMapper.map(it) } ?: emptyList()
        })
    }

    override fun setRating(movieId: Int, value: Float, session_id: String): Flow<State<RatingDto>> {
        return wrapWithFlow { movieService.postRating(movieId, value, session_id) }
    }

    override suspend fun getMovieTrailer(movieId: Int): Flow<State<Trailer>> {
        return wrap({ movieService.getMovieTrailer(movieId) }, {
            trailerMapper.map(it)
        })
    }

    override fun getAllLists(
        accountId: Int,
        sessionId: String,
    ): Flow<State<List<CreatedList>>> {
        return wrap({ movieService.getCreatedLists(accountId, sessionId) }) { baseResponse ->
            baseResponse.items?.map { createdListsMapper.map(it) } ?: emptyList()

        }
    }


    override fun addMovieToList(
        sessionId: String,
        listId: Int,
        movieId: Int,
    ): Flow<State<AddMovieDto>> {
        return wrapWithFlow { movieService.addMovieToList(listId, sessionId, movieId) }
    }

    override fun getListDetails(listId: Int): Flow<State<ListDetailsDto>> {
        return wrapWithFlow { movieService.getList(listId) }
    }

    override fun getRatedMovie(
        accountId: Int,
        sessionId: String
    ): Flow<State<BaseResponse<RatedMovie>>> {
        return wrapWithFlow { movieService.getRatedMovie(accountId, sessionId) }
    }

    override suspend fun insertSearchItem(item: SearchHistoryEntity) {
        return movieDao.insert(item)
    }

    override suspend fun deleteSearchItem(item: SearchHistoryEntity) {
        return movieDao.delete(item)
    }

    override fun getAccountDetails(sessionId: String): Flow<State<Account>> {
        return wrap({ movieService.getAccountDetails(sessionId) }, accountMapper::map)
    }

    override fun getRatedMovies(sessionId: String?): Flow<State<List<RatedMovies>>> {
        return wrap({ movieService.getRatedMovies(sessionId) }) { response ->
            response.items?.map {
                ratedMoviesMapper.map(it)
            } ?: emptyList()
        }
    }


    override suspend fun insertMovie(movie: WatchHistoryEntity) {
        return movieDao.insert(movie)
    }

    override fun getAllWatchedMovies(): Flow<List<WatchHistoryEntity>> {
        return movieDao.getAllWatchedMovies()
    }

    override suspend fun clearWatchHistory() {
        return movieDao.deleteAllWatchedMovies()
    }

    override fun createList(
        sessionId: String,
        name: String,
    ): Flow<State<AddListResponse>> {
        return wrapWithFlow {
            movieService.createList(sessionId, name)
        }
    }

    override suspend fun getTrendingMovies2(): List<Media> {
        return wrap2({ movieService.getTrendingMovies() },
            { ListMapper(movieMapper).mapList(it.items) })
    }

    override suspend fun getTrendingActors2(): List<Actor> {
        return wrap2({ movieService.getTrendingActors() },
            { ListMapper(actorMapper).mapList(it.items) })
    }

    override suspend fun getUpcomingMovies2(): List<Media> {
        return wrap2({ movieService.getUpcomingMovies() },
            { ListMapper(movieMapper).mapList(it.items) })
    }

    override suspend fun getNowPlayingMovies2(): List<Media> {
        return wrap2({ movieService.getNowPlayingMovies() },
            { ListMapper(movieMapper).mapList(it.items) })
    }

    override suspend fun getMovieListByGenreID2(genreID: Int): List<Media> {
        return wrap2({ movieService.getMovieListByGenre(genreID) },
            { ListMapper(movieMapper).mapList(it.items) })
    }
}