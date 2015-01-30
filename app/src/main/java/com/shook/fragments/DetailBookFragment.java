package com.shook.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.internal.ImageRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.shook.R;
import com.shook.model.FavoriteBooks;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by leonelmendez on 05/01/15.
 */
public class DetailBookFragment extends Fragment implements ObservableScrollViewCallbacks{


    private ImageView bookImage;
    private CircleImageView userImage;
    private TextView username,bookTitle,bookDesc,bookAuthor;
    private Button requestButton;

    public static DetailBookFragment newInstance(Bundle args){
        DetailBookFragment detailBookFragment = new DetailBookFragment();
        detailBookFragment.setArguments(args);
        return detailBookFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.bookImage = (ImageView)view.findViewById(R.id.detail_image);
        this.userImage = (CircleImageView)view.findViewById(R.id.detail_user_image);
        this.username = (TextView)view.findViewById(R.id.detail_username);
        this.bookTitle = (TextView)view.findViewById(R.id.detail_book_title);
        this.bookAuthor = (TextView)view.findViewById(R.id.detail_book_author);
        this.bookDesc = (TextView)view.findViewById(R.id.detail_book_desc);
        this.requestButton = (Button)view.findViewById(R.id.sendRequestButton);

        ObservableScrollView observableScrollView = (ObservableScrollView)view.findViewById(R.id.observableScroll);
        observableScrollView.setScrollViewCallbacks(DetailBookFragment.this);

    }


    @Override
    public void onResume() {
            super.onResume();


            Log.d("TAG_OBJ",""+getArguments().getString("username"));
            Picasso.with(getActivity())
                    .load(getArguments().getString("url_image"))
                    .resize(390,340)
                    .centerCrop()
                    .into(this.bookImage);
            setProfileImage(this.userImage,getArguments().getString("facebookId"));
            this.username.setText(getArguments().getString("username"));
        this.bookAuthor.setText(getArguments().getString("book_author"));
        this.bookTitle.setText(getArguments().getString("book_title"));
        this.bookDesc.setText(getArguments().getString("book_desc"));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_book,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_favorite){
            item.setIcon(R.drawable.ic_action_favorite);
            saveFavorite(getArguments().getString("book_title"),getArguments().getString("book_author"),getArguments().getString("book_desc"),getArguments().getString("url_image"));
        }
        return super.onOptionsItemSelected(item);

    }

    private void setProfileImage(final CircleImageView profileImage,String imageUrl){
        try {
            Picasso.with(getActivity())
                    .load(ImageRequest.getProfilePictureUrl(imageUrl, 180, 180).toString())
                    .noFade()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            profileImage.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        if (scrollState == ScrollState.UP) {
            if(requestButton.getVisibility() == View.VISIBLE){
                requestButton.setVisibility(View.GONE);
            }

        } else if (scrollState == ScrollState.DOWN) {
            if((requestButton.getVisibility() == View.GONE)){
                requestButton.setVisibility(View.VISIBLE);
            }
        }

    }

    private void saveFavorite(String title, String author, String desc,String imageUrl){
        FavoriteBooks favoriteBook = new FavoriteBooks(title,author,desc,imageUrl);
        favoriteBook.save();
    }
}

