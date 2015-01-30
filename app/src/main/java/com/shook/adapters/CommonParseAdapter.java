package com.shook.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.shook.R;
import com.shook.model.Book;
import com.squareup.picasso.Picasso;

/**
 * Created by leonelmendez on 04/01/15.
 */
public class CommonParseAdapter extends ParseQueryAdapter<ParseObject> {

    public CommonParseAdapter(Context context, Class<? extends ParseObject> clazz) {
        super(context, clazz);
    }

    public CommonParseAdapter(Context context, QueryFactory<ParseObject> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {




        ViewHolder viewHolder;
        if(v == null){
            v = View.inflate(getContext(), R.layout.book_card_view_layout,null);
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)v.getTag();
        }

        ParseFile coverBookFile = object.getParseFile("image");
        Picasso.with(getContext())
        .load(coverBookFile.getUrl())
        .resize(380,320)
        .centerCrop()
        .into(viewHolder.getBookImage());

        viewHolder.getBookTitle().setText(object.getString("title"));
        viewHolder.getBookAuthor().setText(object.getString("author"));

        return v;


    }

    private static class ViewHolder{
        private ImageView bookImage;
        private TextView bookTitle;
        private TextView bookAuthor;

        public  ViewHolder(View container){

            bookImage = (ImageView)container.findViewById(R.id.book_image);
            bookTitle = (TextView)container.findViewById(R.id.book_title);
            bookAuthor = (TextView)container.findViewById(R.id.book_author);
        }

        public ImageView getBookImage() {
            return bookImage;
        }

        public TextView getBookTitle() {
            return bookTitle;
        }

        public TextView getBookAuthor() {
            return bookAuthor;
        }
    }


}
