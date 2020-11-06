package com.example.bookworm;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BookPhotoTest {
    private Solo solo;
    private FirebaseAuth fAuth;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
        solo.clickOnText("Already signed up? Login here!");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username_login), "yuheng5@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.password_login), "123456");
        solo.clickOnButton("LOGIN");
        //solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("MY BOOKLIST");
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void AddPhotoInAddBook(){
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickOnButton("ADD BOOK");
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
        solo.clickOnButton("ADD PHOTO");
        AddBookActivity activity = (AddBookActivity) solo.getCurrentActivity();
        /**
         * Here the app will jump to gallery to select photo
         * which Robotium is not able to select the photo
         *
         * The photo selected will show up on the Imageview
         */
    }

    @Test
    public void AddPhotoInEditBook(){
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", EditBookActivity.class);
        solo.clickOnButton("ADD PHOTO");
        AddBookActivity activity = (AddBookActivity) solo.getCurrentActivity();
        /**
         * Here the app will jump to gallery to select photo
         * which Robotium is not able to select the photo.
         *
         * The photo selected will show up on the Imageview
         */
    }

    @Test
    public void ViewPhotoInAddBook(){
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickOnButton("ADD BOOK");
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
        AddBookActivity activity = (AddBookActivity) solo.getCurrentActivity();
        ImageView imageView = (ImageView) activity.findViewById(R.id.profile_photo);
        imageView.setImageResource(R.drawable.avatar_1);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        solo.clickOnButton("VIEW PHOTO");
        solo.waitForFragmentByTag("ViewPhotoFragment", 2000);
        Fragment fragment = solo.getCurrentActivity().getFragmentManager().findFragmentByTag("ViewPhotoFragment");
        LinearLayout l = (LinearLayout) fragment.getView();
        ImageView viewphoto = (ImageView) l.getChildAt(0);
        BitmapDrawable bd = (BitmapDrawable) viewphoto.getDrawable();
        assertEquals(bd,drawable);
    }

    @Test
    public void ViewPhotoInEditBook(){
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", EditBookActivity.class);
        AddBookActivity activity = (AddBookActivity) solo.getCurrentActivity();
        ImageView imageView = (ImageView) activity.findViewById(R.id.profile_photo);
        imageView.setImageResource(R.drawable.avatar_1);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        solo.clickOnButton("VIEW PHOTO");
        solo.waitForFragmentByTag("ViewPhotoFragment", 2000);
        Fragment fragment = solo.getCurrentActivity().getFragmentManager().findFragmentByTag("ViewPhotoFragment");
        LinearLayout l = (LinearLayout) fragment.getView();
        ImageView viewphoto = (ImageView) l.getChildAt(0);
        BitmapDrawable bd = (BitmapDrawable) viewphoto.getDrawable();
        assertEquals(bd,drawable);
    }

    @Test
    public void DeletePhotoInAddBook(){
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickOnButton("ADD BOOK");
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
        AddBookActivity activity = (AddBookActivity) solo.getCurrentActivity();
        ImageView imageView = (ImageView) activity.findViewById(R.id.profile_photo);
        imageView.setImageResource(R.drawable.avatar_1);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        solo.clickOnButton("DELETE PHOTO");
        assertEquals(imageView.getTag(),R.drawable.ic_book);
    }

    @Test
    public void DeletePhotoInEditBook(){
        solo.assertCurrentActivity("Wrong Activity", OwnerBooklistActivity.class);
        solo.clickInRecyclerView(0);
        solo.assertCurrentActivity("Wrong Activity", EditBookActivity.class);
        AddBookActivity activity = (AddBookActivity) solo.getCurrentActivity();
        ImageView imageView = (ImageView) activity.findViewById(R.id.profile_photo);
        imageView.setImageResource(R.drawable.avatar_1);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        solo.clickOnButton("DELETE PHOTO");
        assertEquals(imageView.getTag(),R.drawable.ic_book);
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}