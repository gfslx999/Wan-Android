package com.gfs.test.wanandroid.mvvm.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.gfs.test.wanandroid.databinding.ItemBannerTestBinding;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 首页-工作重点适配器
 */
public class HomeKeyWorkAdapter extends BannerAdapter<String, HomeKeyWorkAdapter.VH> {

    private final Context mContext;

    public HomeKeyWorkAdapter(Context context, List<String> list) {
        super(list);
        mContext = context;
    }

    @Override
    public VH onCreateHolder(ViewGroup parent, int viewType) {
        ItemBannerTestBinding binding = ItemBannerTestBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindView(VH holder, String data, int position, int size) {
        if (data == null) {
            return;
        }
        holder.binding.tvTitle.setText(data);
    }

    public static class VH extends RecyclerView.ViewHolder {
        public ItemBannerTestBinding binding;

        public VH(ItemBannerTestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
