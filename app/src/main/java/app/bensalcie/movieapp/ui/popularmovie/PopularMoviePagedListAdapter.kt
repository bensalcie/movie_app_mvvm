package app.bensalcie.movieapp.ui.popularmovie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.bensalcie.movieapp.R
import app.bensalcie.movieapp.data.api.POSTER_BASE_URL
import app.bensalcie.movieapp.data.repository.NetworkState
import app.bensalcie.movieapp.data.vo.Movie
import app.bensalcie.movieapp.ui.single_movie_details.SingleMovie
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_single_movie.*
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class PopularMoviePagedListAdapter(public val context: Context): PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallBack()) {
    val MOVIE_VIEW_TYPE =1
    val NETWORK_VIEW_TYPE =2
    private var networkState :NetworkState? =null
    /**
     *
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View
        if (viewType==MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_list_item,parent,false)
            return MovieItemViewHolder(view)
        }else{
            view = layoutInflater.inflate(R.layout.network_state_item,parent,false)
            return NetworkStateItemViewHolder(view)
        }
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getBindingAdapterPosition] which
     * will have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()+ if (hasExtraRow())1 else 0
    }
    private fun hasExtraRow():Boolean{

        return networkState!=null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount-1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallBack:DiffUtil.ItemCallback<Movie>(){
        /**
         * Called to check whether two objects represent the same item.
         *
         *
         * For example, if your items have unique ids, this method should check their id equality.
         *
         *
         * Note: `null` items in the list are assumed to be the same as another `null`
         * item and are assumed to not be the same as a non-`null` item. This callback will
         * not be invoked for either of those cases.
         *
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the two items represent the same object or false if they are different.
         * @see Callback.areItemsTheSame
         */
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * Called to check whether two items have the same data.
         *
         *
         * This information is used to detect if the contents of an item have changed.
         *
         *
         * This method to check equality instead of [Object.equals] so that you can
         * change its behavior depending on your UI.
         *
         *
         * For example, if you are using DiffUtil with a
         * [RecyclerView.Adapter], you should
         * return whether the items' visual representations are the same.
         *
         *
         * This method is called only if [.areItemsTheSame] returns `true` for
         * these items.
         *
         *
         * Note: Two `null` items are assumed to represent the same contents. This callback
         * will not be invoked for this case.
         *
         * @param oldItem The item in the old list.
         * @param newItem The item in the new list.
         * @return True if the contents of the items are the same or false if they are different.
         * @see Callback.areContentsTheSame
         */
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder (view: View): RecyclerView.ViewHolder(view) {

        fun bind(movie:Movie?,context:Context){

            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text =movie?.releaseDate
            Glide.with(context).load(POSTER_BASE_URL+movie?.posterPath).into(itemView.cv_iv_movie_poster)

            itemView.setOnClickListener {
                itemView.context.startActivity(Intent(context,SingleMovie::class.java).putExtra("id",movie?.id))
            }
        }
    }

    class NetworkStateItemViewHolder (view: View):RecyclerView.ViewHolder(view){
        fun  bind(networkState:NetworkState?){
            if (networkState!=null && networkState==NetworkState.LOADING){
                itemView.progress_bar_item.visibility=View.VISIBLE
            }else{
                itemView.progress_bar_item.visibility=View.GONE

            }


            if (networkState!=null && networkState==NetworkState.ERROR){
                itemView.errr_msg_item.visibility=View.VISIBLE
                itemView.errr_msg_item.text= networkState.msg
            }else if (networkState!=null && networkState==NetworkState.ENDOFLIST){
                itemView.errr_msg_item.visibility=View.VISIBLE
                itemView.errr_msg_item.text= networkState.msg
            }else{
                itemView.errr_msg_item.visibility=View.GONE

            }
        }
    }

    fun  setNetworkState(newnetworkState: NetworkState?){
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState =newnetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow!=hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount())
            }
        }else if(hasExtraRow && previousState!=newnetworkState){
            notifyItemChanged(itemCount-1)

        }
    }
}