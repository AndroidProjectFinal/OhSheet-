package com.example.ohsheet.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ohsheet.R;
import com.example.ohsheet.entity.Genre;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Activity activity;
    private int layout;
    private List<Genre> list;

    public CategoryAdapter(Activity activity, int layout, List<Genre> list) {
        this.activity = activity;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;

        if(view == null){
            view = activity.getLayoutInflater().inflate(R.layout.customlayout_main,null);
            imageView = view.findViewById(R.id.imageCategory);
            view.setTag(R.id.imageCategory, imageView);
        }else {
            imageView = (ImageView) view.getTag(R.id.imageCategory);
        }
        Genre category = list.get(i);
//
////        Uri uri = Uri.parse(category.getCategoryImg());
////
////        imageView.setImageURI(uri);
//        File file = new File(category.getCategoryImg());
//
//        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//
//
//        imageView.setImageBitmap(myBitmap);
        imageView.setImageResource(category.getCategoryImg());
//        Log.i("img", file.getAbsolutePath());
        return view;
    }
}
