package com.fr1014.mycoludmusic.ui.home.songsale

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.fr1014.frecyclerviewadapter.BaseAdapter
import com.fr1014.frecyclerviewadapter.BaseViewHolder
import com.fr1014.mycoludmusic.R
import com.fr1014.mycoludmusic.app.AppViewModelFactory
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.songsale.Product
import com.fr1014.mycoludmusic.databinding.FragmentDetailSaleBinding
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer
import com.fr1014.mycoludmusic.musicmanager.Music
import com.fr1014.mycoludmusic.utils.glide.GlideApp
import com.fr1014.mymvvm.base.BaseFragment

const val KEY_POSITION = "key_position"

class DetailSaleFragment : BaseFragment<FragmentDetailSaleBinding, SongSaleViewModel>() {
    //0专辑 1单曲
    var mAlbumType = 0
    var mDataType = 0

    //type : daily:日榜,week:周榜,year:年榜,total:总榜
    val type = arrayOf("daily", "week", "year", "total")
    private lateinit var mAdapter: SongSaleAdapter

    companion object {
        fun getInstance(type: Int, position: Int): DetailSaleFragment {
            val bundle = Bundle()
            bundle.putInt(TYPE_FRAGMENT, type)
            bundle.putInt(KEY_POSITION, position)
            val detailSaleFragment = DetailSaleFragment()
            detailSaleFragment.arguments = bundle
            return detailSaleFragment
        }
    }

    override fun initParam() {
        arguments?.apply {
            mAlbumType = getInt(TYPE_FRAGMENT)
            mDataType = getInt(KEY_POSITION)
        }
    }

    override fun getViewBinding(container: ViewGroup?): FragmentDetailSaleBinding {
        return FragmentDetailSaleBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): SongSaleViewModel {
        val factory = AppViewModelFactory.getInstance(MyApplication.getInstance())
        return ViewModelProvider(this, factory).get(SongSaleViewModel::class.java)
    }

    override fun initView() {
        mAdapter = SongSaleAdapter(R.layout.item_song_sale)
        mViewBinding.apply {
            rvSongSale.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }
        }

        mAdapter.onItemClickListener = BaseAdapter.OnItemClickListener { adapter, view, position ->
            when (view.id) {
                R.id.item_song_sale -> {
                    when (mAlbumType) {

                        TYPE_ALBUM -> { //专辑榜

                        }

                        TYPE_SINGLE -> { //单曲榜
                            val product = mAdapter.getData(position)
                            val music = Music(product.artistName, product.albumName, "", "", true)
                            AudioPlayer.get().addAndPlay(music,true)
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        mViewModel.getSongSaleList(type[mDataType], mAlbumType)
    }

    override fun initViewObservable() {
        mViewModel.getProducts().observe(this, Observer {
            mAdapter.setData(it)
        })
    }
}

class SongSaleAdapter(layoutRes: Int) : BaseAdapter<Product, BaseViewHolder>(layoutRes) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, data: Product) {
        holder.apply {
            getView<TextView>(R.id.tv_order).text = holder.adapterPosition.plus(1).toString()
            getView<View>(R.id.place_view).visibility = if (holder.layoutPosition == itemCount.minus(1)) View.VISIBLE else View.GONE
            getView<TextView>(R.id.tv_album_name).text = data.albumName
            getView<TextView>(R.id.tv_artist_name).text = data.artistName
            getView<TextView>(R.id.tv_sale_num).text = "已售${data.saleNum}张"
            getView<TextView>(R.id.tv_order).setTextColor(if (holder.adapterPosition <= 2) mContext.resources.getColor(R.color.red) else mContext.resources.getColor(R.color.font_gray))
            GlideApp.with(itemView)
                    .load(data.coverUrl + "?param=200y200")
                    .apply(RequestOptions().placeholder(R.drawable.ic_placeholder).centerCrop().transform(RoundedCorners(8)))
                    .into(getView(R.id.iv_cover))
            addOnClickListener(R.id.item_song_sale)
        }
    }

}