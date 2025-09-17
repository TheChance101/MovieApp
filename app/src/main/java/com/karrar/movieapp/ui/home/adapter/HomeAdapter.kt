package com.karrar.movieapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.karrar.movieapp.BR
import com.karrar.movieapp.R
import com.karrar.movieapp.domain.enums.HomeItemsType
import com.karrar.movieapp.ui.adapters.MovieAdapter
import com.karrar.movieapp.ui.adapters.MovieInteractionListener
import com.karrar.movieapp.ui.base.BaseAdapter
import com.karrar.movieapp.ui.base.BaseInteractionListener
import com.karrar.movieapp.ui.home.HomeInteractionListener
import com.karrar.movieapp.ui.home.HomeItem
import com.karrar.movieapp.ui.models.MediaUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeAdapter(
    private var homeItems: MutableList<HomeItem>,
    private val listener: BaseInteractionListener,
    private val scope: CoroutineScope
) : BaseAdapter<HomeItem>(homeItems, listener) {
    override val layoutID: Int = 0

    fun setItem(item: HomeItem) {
        val newItems = homeItems.apply {
            removeAt(item.priority)
            add(item.priority, item)
        }
        setItems(newItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), viewType, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (homeItems.isNotEmpty())
            bind(holder as ItemViewHolder, position)
    }

    override fun bind(holder: ItemViewHolder, position: Int) {
        if (position != -1)
            when (val currentItem = homeItems[position]) {
                is HomeItem.Slider -> {
                    val adapter = PopularMovieAdapter(currentItem.items, listener as HomeInteractionListener)
                    holder.binding.setVariable(BR.adapterRecycler, adapter)

                    val viewPager = holder.binding.root.findViewById<ViewPager2>(R.id.viewpager_popular_movie)
                    viewPager?.adapter = adapter
                    viewPager?.offscreenPageLimit = 3
                    attachCarouselTransformer(viewPager)
                    startAutoScroll(viewPager, currentItem.items.size)
                }

                is HomeItem.RecentlyReleased -> {
                    bindMovie(holder, currentItem.items, currentItem.type)
                }

                is HomeItem.OnTheAiring -> {
                    holder.binding.run {
                        setVariable(
                            BR.adapterRecycler,
                            TVShowAdapter(currentItem.items, listener as TVShowInteractionListener)
                        )
                        setVariable(BR.movieType, currentItem.type)
                    }
                }

                is HomeItem.Upcoming -> {
                    bindMovie(holder, currentItem.items, currentItem.type)
                }

                is HomeItem.BrowseEverything -> {
                    holder.binding.run {
                        setVariable(BR.listener, listener as HomeInteractionListener)
                    }
                }

                is HomeItem.RecentlyViewed -> {
                    holder.binding.setVariable(
                        BR.adapterRecycler,
                        RecentlyViewedAdapter(currentItem.items, listener as RecentlyViewedInteractionListener)
                    )
                }

                is HomeItem.LetUsChooseForYou -> {
                    holder.binding.run {
                        setVariable(BR.listener, listener as HomeInteractionListener)
                    }
                }

                is HomeItem.CollectionsList -> {
                    holder.binding.setVariable(
                        BR.adapterRecycler,
                        YourCollectionsAdapter(currentItem.items, listener as YourCollectionsInteractionListener)
                    )
                }
            }
    }

    private fun bindMovie(holder: ItemViewHolder, items: List<MediaUiState>, type: HomeItemsType) {
        holder.binding.run {
            setVariable(
                BR.adapterRecycler,
                MovieAdapter(items, listener as MovieInteractionListener)
            )
            setVariable(BR.movieType, type)
        }
    }

    override fun setItems(newItems: List<HomeItem>) {
        homeItems = newItems.sortedBy { it.priority }.toMutableList()
        super.setItems(homeItems)
    }

    override fun areItemsSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
        return oldItem.priority == newItem.priority
    }

    override fun areContentSame(
        oldPosition: HomeItem,
        newPosition: HomeItem,
    ): Boolean {
        return oldPosition == newPosition
    }

    override fun getItemViewType(position: Int): Int {
        if (homeItems.isNotEmpty()) {
            return when (homeItems[position]) {
                is HomeItem.BrowseEverything -> R.layout.item_browser_everything_cta
                is HomeItem.Slider -> R.layout.list_popular
                is HomeItem.OnTheAiring -> R.layout.list_tvshow
                is HomeItem.RecentlyReleased -> R.layout.list_movie
                is HomeItem.Upcoming, -> R.layout.list_movie
                is HomeItem.RecentlyViewed -> R.layout.list_recently_viewed
                is HomeItem.LetUsChooseForYou -> R.layout.item_let_us_choose_cta
                is HomeItem.CollectionsList -> R.layout.list_your_collections
            }
        }
        return -1
    }

    private fun attachCarouselTransformer(viewPager: ViewPager2) {
        val displayMetrics = viewPager.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val minSidePeek = viewPager.context.resources.getDimensionPixelOffset(R.dimen.spacing_extra_large)
        val sidePeek = maxOf(screenWidth * 0.05f, minSidePeek.toFloat())

        val extraLift = viewPager.context.resources.getDimensionPixelOffset(R.dimen.spacing_extra_extra_large)

        viewPager.setPageTransformer { page, position ->
            val offset = position * -sidePeek
            page.translationX = if (viewPager.layoutDirection == View.LAYOUT_DIRECTION_RTL) -offset else offset
            page.scaleX = 1f
            page.scaleY = 1f
            page.alpha = 0.8f + (1 - abs(position)) * 0.2f
            page.translationY = if (position == 0f) -extraLift.toFloat() else 0f
            page.translationZ = if (position == 0f) 1f else 0f
        }
    }

    fun startAutoScroll(viewPager: ViewPager2, itemCount: Int) {
        if (itemCount == 0) return

        val recyclerView = viewPager.getChildAt(0) as? RecyclerView ?: return

        scope.launch {
            var position = 0
            while (true) {
                delay(2000)
                position = (position + 1) % itemCount

                val smoothScroller = object : LinearSmoothScroller(viewPager.context) {
                    override fun getHorizontalSnapPreference(): Int = SNAP_TO_START
                    override fun calculateTimeForScrolling(dx: Int): Int {
                        return 400.coerceAtMost(super.calculateTimeForScrolling(dx))
                    }
                }

                smoothScroller.targetPosition = position
                recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }
    }


}
