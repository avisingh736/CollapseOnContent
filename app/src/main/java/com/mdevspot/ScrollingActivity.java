package com.mdevspot;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ScrollingActivity extends AppCompatActivity {
    private int screenHeight;
    private int toolbarHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        screenHeight = getScreenHeight(this);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        final ViewGroup.LayoutParams toolbarLayoutParams = toolbar.getLayoutParams();
        if (toolbarLayoutParams != null) {
            toolbarHeight = toolbarLayoutParams.height;
        }

        final LinearLayout linearLayout = findViewById(R.id.linearLayout);
        ViewTreeObserver observer = linearLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = screenHeight - linearLayout.getHeight() - getStatusBarHeight();
                if (toolbarLayoutParams != null) {
                    toolbarLayoutParams.height = Math.max(height, toolbarHeight);
                    toolbar.setLayoutParams(toolbarLayoutParams);
                }

                // Removes the listener if possible
                ViewTreeObserver viewTreeObserver = linearLayout.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getScreenHeight(Context context) {
        int measuredHeight;
        Point size = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        wm.getDefaultDisplay().getSize(size);
        measuredHeight = size.y;

        return measuredHeight;
    }

    private int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }
}