package vn.edu.usth.instagram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Tag;

import java.util.List;

import vn.edu.usth.instagram.R;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.Viewholder>{

    private Context mContext;
    private List<String> mTags;
    private List<String> mTagsCount;

    public TagAdapter(Context mContext, List<String> mTags, List<String> mTagsCount) {
        this.mContext = mContext;
        this.mTags = mTags;
        this.mTagsCount = mTagsCount;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, parent, false);

        return new TagAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.tag.setText("#" + mTags.get(position));
        holder.noOfPosts.setText(mTagsCount.get(position) + " posts");

    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public TextView tag;
        public TextView noOfPosts;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            tag = itemView.findViewById(R.id.hash_tag);
            noOfPosts = itemView.findViewById(R.id.no_of_posts);
        }
    }
    public void filter (List<String> filterTags, List<String> filterTagsCount){
        this.mTags = filterTags;
        this.mTagsCount = filterTagsCount;

        notifyDataSetChanged();
    }
}