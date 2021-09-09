package com.technorizen.stanrz.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.models.SuccessResGetUploads;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravindra Birla on 26,August,2021
 */
public class ImagesAndVideosPagerAdapter extends PagerAdapter {

    private static final String TAG = "ImagesAndVideosPager";

    private  List<SuccessResGetUploads.UserPost> userPosts;

    private Context context;
    private final LayoutInflater mLayoutInflater;

    /**
     * The click event listener which will propagate click events to the parent or any other listener set
     */

    /**
     * Constructor for gallery adapter which will create and screen slide of images.
     *
     * @param context
     *         The context which will be used to inflate the layout for each page.
     * @param userPosts
     *         The list of items which need to be displayed as screen slide.
     */
    public ImagesAndVideosPagerAdapter(Context context,
                          @NonNull List<SuccessResGetUploads.UserPost> userPosts) {
        super();
        // Inflater which will be used for creating all the necessary pages
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // The items which will be displayed.
        this.userPosts = userPosts;
        this.context = context;
    }

    @Override
    public int getCount() {
        // Just to be safe, check also if we have an valid list of items - never return invalid size.
        return null == userPosts ? 0 : userPosts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // The object returned by instantiateItem() is a key/identifier. This method checks whether
        // the View passed to it (representing the page) is associated with that key or not.
        // It is required by a PagerAdapter to function properly.
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // This method should create the page for the given position passed to it as an argument.
        // In our case, we inflate() our layout resource to create the hierarchy of view objects and then
        // set resource for the ImageView in it.
        // Finally, the inflated view is added to the container (which should be the ViewPager) and return it as well.

        // inflate our layout resource
        View itemView = mLayoutInflater.inflate(R.layout.view_pager_item, container, false);

        // Display the resource on the view
        displayGalleryItem((AppCompatImageView) itemView.findViewById(R.id.ivImage), userPosts.get(position).getImage());

        // Add our inflated view to the container
        container.addView(itemView);

        // Return our view
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Removes the page from the container for the given position. We simply removed object using removeView()
        // but could’ve also used removeViewAt() by passing it the position.
        try {
            // Remove the view from the container
            container.removeView((View) object);

            // Try to clear resources used for displaying this view
            // Remove any resources used by this view
            unbindDrawables((View) object);
            // Invalidate the object
            object = null;
        } catch (Exception e) {
            Log.w(TAG, "destroyItem: failed to destroy item and clear its used resrces", e);
        }
    }

    /**
     * Recursively unbind any resources from the provided view. This method will clear the resources of all the
     * children of the view before invalidating the provided view itself.
     *
     * @param view
     *         The view for which to unbind resource.
     */
    protected void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    /**
     * Set an listener which will notify of any click events that are detected on the pages of the view pager.
     *
     * @param onItemClickListener
     *         The listener. If {@code null} it will disable any events from being sent.
     */


    /**
     * Display the gallery image into the image view provided.
     *
     * @param galleryView
     *         The view which will display the image.
     * @param galleryItem
     *         The item from which to get the image.
     */
    private void displayGalleryItem(AppCompatImageView galleryView, String galleryItem) {
        if (null != galleryItem) {

            Zoomy.Builder builder = new Zoomy.Builder((Activity) context).target(galleryView);
            builder.register();

            Glide.with(galleryView.getContext()) // Bind it with the context of the actual view used
                    .load(galleryItem) // Load the image
                    .format(DecodeFormat.PREFER_RGB_565) // the decode format - this will not use alpha at all
                    .centerInside() // scale type
                    .thumbnail(0.2f) // make use of the thumbnail which can display a down-sized version of the image
                    .into(galleryView); // Voilla - the target view
        }
    }
}