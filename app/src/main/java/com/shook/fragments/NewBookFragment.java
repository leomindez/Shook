package com.shook.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shook.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leonelmendez on 04/01/15.
 */
public class NewBookFragment extends Fragment{


    private static int CAMERA_IMAGE_RESULT = 1;
    private ImageView coverBook;
    private Bitmap coverBookBitmap;
    private EditText titleEdit,authorEdit,descEdit;
    private String mCurrentPhotoPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_book_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coverBook = (ImageView)view.findViewById(R.id.cover_book_container);
        titleEdit = (EditText)view.findViewById(R.id.title_edit);
        authorEdit = (EditText)view.findViewById(R.id.author_edit);
        descEdit = (EditText)view.findViewById(R.id.desc_edit);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_book,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_camera:
                initCamera();
                return true;
            case R.id.action_publish:
                saveBook(titleEdit.getText().toString(),authorEdit.getText().toString(),descEdit.getText().toString());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_IMAGE_RESULT && resultCode == Activity.RESULT_OK){
            //setCoverBook(data.getExtras());
            setPic(coverBook);
        }
    }

    private void initCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){

            File coverPhotoFile = null;

            try {
                coverPhotoFile = createImageFile();
                mCurrentPhotoPath = coverPhotoFile.getAbsolutePath();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(coverPhotoFile != null){
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(coverPhotoFile));
                startActivityForResult(cameraIntent,CAMERA_IMAGE_RESULT);
            }



        }
    }


    private void setCoverBook(Bundle cameraResult){
        if(cameraResult != null){
            Bitmap cover = (Bitmap) cameraResult.get("data");
            coverBookBitmap = cover;
            this.coverBook.setImageBitmap(cover);
        }
    }

    private void saveBook(String bookTitle, String bookAuthor, String bookDescription){

       if(!TextUtils.isEmpty(bookTitle) && !TextUtils.isEmpty(bookAuthor) && !TextUtils.isEmpty(bookDescription)){
           Log.d("TAG_DESC",bookDescription);

           if(mCurrentPhotoPath != null && !TextUtils.isEmpty(mCurrentPhotoPath)) {
               uploadBook(bookTitle, bookAuthor, bookDescription);
           }else {
               Toast.makeText(getActivity(), "Add a book's photo!", Toast.LENGTH_SHORT).show();
           }

       }
           /*
       }
       */
    }

    private void uploadBook(final String bookTitle, final String bookAuthor, final String bookDescription) {



        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Saving book ...");
        progressDialog.show();



     final ParseFile bookImage = convertBitmapIntoParseFile(bookTitle,mCurrentPhotoPath);
        if(bookImage != null) {
           bookImage.saveInBackground();

            ParseObject parseObject = new ParseObject("Book");
            parseObject.put("title", bookTitle);
            parseObject.put("author", bookAuthor);
            parseObject.put("description", bookDescription);
            parseObject.put("image", bookImage);
            parseObject.put("owner", ParseUser.getCurrentUser());
            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null)
                        e.printStackTrace();
                    progressDialog.dismiss();
                    getActivity().finish();
                }
            });
        }else {
            Toast.makeText(getActivity(), "Add a book's photo!", Toast.LENGTH_SHORT).show();
        }

    }


    private ParseFile convertBitmapIntoParseFile(String fileName,String pathFile){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(pathFile);
        if(bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] image = byteArrayOutputStream.toByteArray();

            return new ParseFile(fileName + "-cover.jpg", image);
        }else{
            return null;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "book-cover " + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory(),"/Shook/");
        if(!storageDir.exists())
            storageDir.mkdirs();



        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }


    private void setPic(ImageView mImageView) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }
}
