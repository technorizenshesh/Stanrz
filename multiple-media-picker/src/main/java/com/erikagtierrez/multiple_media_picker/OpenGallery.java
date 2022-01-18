
package com.erikagtierrez.multiple_media_picker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.erikagtierrez.multiple_media_picker.Adapters.MediaAdapter;
import com.erikagtierrez.multiple_media_picker.Adapters.MediaBottomAdapter;
import com.erikagtierrez.multiple_media_picker.Fragments.OneFragment;
import com.erikagtierrez.multiple_media_picker.Fragments.TwoFragment;
import com.erikagtierrez.multiple_media_picker.util.SpacesItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OpenGallery extends AppCompatActivity {

    private RecyclerView recyclerView,rvSelectedItems;
    private MediaAdapter mAdapter;
    private MediaBottomAdapter mediaBottomAdapter;
    private List<String> mediaList = new ArrayList<>();
    public static List<Boolean> selected = new ArrayList<>();
    public static ArrayList<String> imagesSelected = new ArrayList<>();
    public static String parent;

    private boolean isVideo = false;

    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_open_gallery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppCompatTextView bntNext = findViewById(R.id.bntNext);
        bntNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
        toolbar.setNavigationIcon(R.drawable.arrow_back);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setTitle(Gallery.title);
        if (imagesSelected.size() > 0) {
            setTitle(String.valueOf(imagesSelected.size()));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        rvSelectedItems = findViewById(R.id.rvSelectedItems);

        parent = getIntent().getExtras().getString("FROM");
        mediaList.clear();
        selected.clear();
        if (parent.equals("Images")) {
            mediaList.addAll(OneFragment.imagesList);
            selected.addAll(OneFragment.selected);
            isVideo = false;
        } else {
            mediaList.addAll(TwoFragment.videosList);
            selected.addAll(TwoFragment.selected);
            isVideo = true;
        }
        populateRecyclerView();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvSelectedItems.setLayoutManager(linearLayoutManager);
        rvSelectedItems.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen._10sdp)));
        mediaBottomAdapter = new MediaBottomAdapter(imagesSelected,selected,this);
        rvSelectedItems.setAdapter(mediaBottomAdapter);
    }

    private void populateRecyclerView() {
        for (int i = 0; i < selected.size(); i++) {
            if (imagesSelected.contains(mediaList.get(i))) {
                selected.set(i, true);
            } else {
                selected.set(i, false);
            }
        }
        mAdapter = new MediaAdapter(mediaList, selected, getApplicationContext());
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,6);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position % 5){
                    case 0:
                    case 1:
                        return 3;
                    case 2:
                    case 3:
                    case 4:
                        return 2;
                }
                throw new IllegalStateException("internal error");
            }
        });
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen._3sdp)));

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if(isVideo)
                {

                    if (!selected.get(position).equals(true) && imagesSelected.size() < 1) {

                        imagesSelected.add(mediaList.get(position));
                        selected.set(position, !selected.get(position));
                        mAdapter.notifyItemChanged(position);

                    } else if (selected.get(position).equals(true)) {
                        if (imagesSelected.indexOf(mediaList.get(position)) != -1) {
                            imagesSelected.remove(imagesSelected.indexOf(mediaList.get(position)));
                            selected.set(position, !selected.get(position));
                            mAdapter.notifyItemChanged(position);
                        }
                    }

                    Gallery.selectionTitle = imagesSelected.size();
                    if (imagesSelected.size() != 0) {
                        setTitle(String.valueOf(imagesSelected.size()));
                    } else {
                        setTitle(Gallery.title);
                    }

                }
                else
                {

                    if (!selected.get(position).equals(true) && imagesSelected.size() < Gallery.maxSelection) {

                        imagesSelected.add(mediaList.get(position));
                        selected.set(position, !selected.get(position));
                        mAdapter.notifyItemChanged(position);

                    } else if (selected.get(position).equals(true)) {
                        if (imagesSelected.indexOf(mediaList.get(position)) != -1) {
                            imagesSelected.remove(imagesSelected.indexOf(mediaList.get(position)));
                            selected.set(position, !selected.get(position));
                            mAdapter.notifyItemChanged(position);
                        }
                    }

                    Gallery.selectionTitle = imagesSelected.size();
                    if (imagesSelected.size() != 0) {
                        setTitle(String.valueOf(imagesSelected.size()));
                    } else {
                        setTitle(Gallery.title);
                    }

                }

                mediaBottomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private OpenGallery.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final OpenGallery.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}

