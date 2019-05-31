package com.effective.android.video.core;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.effective.android.video.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.DiscontinuityReason;
import com.google.android.exoplayer2.Player.VideoComponent;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.ResizeMode;
import com.google.android.exoplayer2.ui.spherical.SingleTapListener;
import com.google.android.exoplayer2.ui.spherical.SphericalSurfaceView;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class SimplePlayerBaseView extends FrameLayout {

    // LINT.IfChange
    private static final int SURFACE_TYPE_NONE = 0;
    private static final int SURFACE_TYPE_SURFACE_VIEW = 1;
    private static final int SURFACE_TYPE_TEXTURE_VIEW = 2;
    private static final int SURFACE_TYPE_MONO360_VIEW = 3;
    // LINT.ThenChange(../../../../../../res/values/attrs.xml)

    @Nullable
    private final AspectRatioFrameLayout contentFrame;
    private final View shutterView;
    @Nullable private final View surfaceView;
    private final ImageView artworkView;
    private final ComponentListener componentListener;

    private Player player;
    private boolean useArtwork;
    @Nullable private Drawable defaultArtwork;
    private boolean keepContentOnPlayerReset;
    private int textureViewRotation;

    public SimplePlayerBaseView(Context context) {
        this(context, null);
    }

    public SimplePlayerBaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimplePlayerBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) {
            contentFrame = null;
            shutterView = null;
            surfaceView = null;
            artworkView = null;
            componentListener = null;
            ImageView logo = new ImageView(context);
            if (Util.SDK_INT >= 23) {
                configureEditModeLogoV23(getResources(), logo);
            } else {
                configureEditModeLogo(getResources(), logo);
            }
            addView(logo);
            return;
        }

        boolean shutterColorSet = false;
        int shutterColor = 0;
        int playerLayoutId = R.layout.video_palyer_base_view_layout;
        boolean useArtwork = true;
        int defaultArtworkId = 0;
        int surfaceType = SURFACE_TYPE_SURFACE_VIEW;
        int resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PlayerView, 0, 0);
            try {
                shutterColorSet = a.hasValue(R.styleable.PlayerView_shutter_background_color);
                shutterColor = a.getColor(R.styleable.PlayerView_shutter_background_color, shutterColor);
                playerLayoutId = a.getResourceId(R.styleable.PlayerView_player_layout_id, playerLayoutId);
                useArtwork = a.getBoolean(R.styleable.PlayerView_use_artwork, useArtwork);
                defaultArtworkId = a.getResourceId(R.styleable.PlayerView_default_artwork, defaultArtworkId);
                surfaceType = a.getInt(R.styleable.PlayerView_surface_type, surfaceType);
                resizeMode = a.getInt(R.styleable.PlayerView_resize_mode, resizeMode);
                keepContentOnPlayerReset = a.getBoolean(R.styleable.PlayerView_keep_content_on_player_reset, keepContentOnPlayerReset);
            } finally {
                a.recycle();
            }
        }

        LayoutInflater.from(context).inflate(playerLayoutId, this);
        componentListener = new ComponentListener();
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        // Content frame.
        contentFrame = findViewById(R.id.exo_content_frame);
        if (contentFrame != null) {
            setResizeModeRaw(contentFrame, resizeMode);
        }

        // Shutter view.
        shutterView = findViewById(R.id.exo_shutter);
        if (shutterView != null && shutterColorSet) {
            shutterView.setBackgroundColor(shutterColor);
        }

        // Create a surface view and insert it into the content frame, if there is one.
        if (contentFrame != null && surfaceType != SURFACE_TYPE_NONE) {
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            switch (surfaceType) {
                case SURFACE_TYPE_TEXTURE_VIEW:
                    surfaceView = new TextureView(context);
                    break;
                case SURFACE_TYPE_MONO360_VIEW:
                    Assertions.checkState(Util.SDK_INT >= 15);
                    SphericalSurfaceView sphericalSurfaceView = new SphericalSurfaceView(context);
                    sphericalSurfaceView.setSurfaceListener(componentListener);
                    sphericalSurfaceView.setSingleTapListener(componentListener);
                    surfaceView = sphericalSurfaceView;
                    break;
                default:
                    surfaceView = new SurfaceView(context);
                    break;
            }
            surfaceView.setLayoutParams(params);
            contentFrame.addView(surfaceView, 0);
        } else {
            surfaceView = null;
        }

        // Artwork view.
        artworkView = findViewById(R.id.exo_artwork);
        this.useArtwork = useArtwork && artworkView != null;
        if (defaultArtworkId != 0) {
            defaultArtwork = ContextCompat.getDrawable(getContext(), defaultArtworkId);
        }

    }

    /**
     * Switches the view targeted by a given {@link Player}.
     *
     * @param player The player whose target view is being switched.
     * @param oldPlayerView The old view to detach from the player.
     * @param newPlayerView The new view to attach to the player.
     */
    public static void switchTargetView(
            Player player, @Nullable SimplePlayerBaseView oldPlayerView, @Nullable SimplePlayerBaseView newPlayerView) {
        if (oldPlayerView == newPlayerView) {
            return;
        }
        // We attach the new view before detaching the old one because this ordering allows the player
        // to swap directly from one surface to another, without transitioning through a state where no
        // surface is attached. This is significantly more efficient and achieves a more seamless
        // transition when using platform provided video decoders.
        if (newPlayerView != null) {
            newPlayerView.setPlayer(player);
        }
        if (oldPlayerView != null) {
            oldPlayerView.setPlayer(null);
        }
    }

    /** Returns the player currently set on this view, or null if no player is set. */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the {@link Player} to use.
     *
     * <p>To transition a {@link Player} from targeting one view to another, it's recommended to use
     * {@link #switchTargetView(Player, SimplePlayerBaseView, SimplePlayerBaseView)} rather than this method. If you do
     * wish to use this method directly, be sure to attach the player to the new view <em>before</em>
     * calling {@code setPlayer(null)} to detach it from the old one. This ordering is significantly
     * more efficient and may allow for more seamless transitions.
     *
     * @param player The {@link Player} to use, or {@code null} to detach the current player. Only
     *     players which are accessed on the main thread are supported ({@code
     *     player.getApplicationLooper() == Looper.getMainLooper()}).
     */
    public void setPlayer(@Nullable Player player) {
        Assertions.checkState(Looper.myLooper() == Looper.getMainLooper());
        Assertions.checkArgument(
                player == null || player.getApplicationLooper() == Looper.getMainLooper());
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.removeListener(componentListener);
            Player.VideoComponent oldVideoComponent = this.player.getVideoComponent();
            if (oldVideoComponent != null) {
                oldVideoComponent.removeVideoListener(componentListener);
                if (surfaceView instanceof TextureView) {
                    oldVideoComponent.clearVideoTextureView((TextureView) surfaceView);
                } else if (surfaceView instanceof SphericalSurfaceView) {
                    ((SphericalSurfaceView) surfaceView).setVideoComponent(null);
                } else if (surfaceView instanceof SurfaceView) {
                    oldVideoComponent.clearVideoSurfaceView((SurfaceView) surfaceView);
                }
            }
            Player.TextComponent oldTextComponent = this.player.getTextComponent();
            if (oldTextComponent != null) {
                oldTextComponent.removeTextOutput(componentListener);
            }
        }
        this.player = player;
        updateForCurrentTrackSelections(/* isNewPlayer= */ true);
        if (player != null) {
            Player.VideoComponent newVideoComponent = player.getVideoComponent();
            if (newVideoComponent != null) {
                if (surfaceView instanceof TextureView) {
                    newVideoComponent.setVideoTextureView((TextureView) surfaceView);
                } else if (surfaceView instanceof SphericalSurfaceView) {
                    ((SphericalSurfaceView) surfaceView).setVideoComponent(newVideoComponent);
                } else if (surfaceView instanceof SurfaceView) {
                    newVideoComponent.setVideoSurfaceView((SurfaceView) surfaceView);
                }
                newVideoComponent.addVideoListener(componentListener);
            }
            Player.TextComponent newTextComponent = player.getTextComponent();
            if (newTextComponent != null) {
                newTextComponent.addTextOutput(componentListener);
            }
            player.addListener(componentListener);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (surfaceView instanceof SurfaceView) {
            // Work around https://github.com/google/ExoPlayer/issues/3160.
            surfaceView.setVisibility(visibility);
        }
    }

    /**
     * Sets the {@link ResizeMode}.
     *
     * @param resizeMode The {@link ResizeMode}.
     */
    public void setResizeMode(@ResizeMode int resizeMode) {
        Assertions.checkState(contentFrame != null);
        contentFrame.setResizeMode(resizeMode);
    }

    /** Returns the {@link ResizeMode}. */
    public @ResizeMode
    int getResizeMode() {
        Assertions.checkState(contentFrame != null);
        return contentFrame.getResizeMode();
    }

    /** Returns whether artwork is displayed if present in the media. */
    public boolean getUseArtwork() {
        return useArtwork;
    }

    /**
     * Sets whether artwork is displayed if present in the media.
     *
     * @param useArtwork Whether artwork is displayed.
     */
    public void setUseArtwork(boolean useArtwork) {
        Assertions.checkState(!useArtwork || artworkView != null);
        if (this.useArtwork != useArtwork) {
            this.useArtwork = useArtwork;
            updateForCurrentTrackSelections(/* isNewPlayer= */ false);
        }
    }

    /** Returns the default artwork to display. */
    public @Nullable Drawable getDefaultArtwork() {
        return defaultArtwork;
    }

    /**
     * Sets the default artwork to display if {@code useArtwork} is {@code true} and no artwork is
     * present in the media.
     *
     * @param defaultArtwork the default artwork to display.
     * @deprecated use (@link {@link #setDefaultArtwork(Drawable)} instead.
     */
    @Deprecated
    public void setDefaultArtwork(@Nullable Bitmap defaultArtwork) {
        setDefaultArtwork(
                defaultArtwork == null ? null : new BitmapDrawable(getResources(), defaultArtwork));
    }

    /**
     * Sets the default artwork to display if {@code useArtwork} is {@code true} and no artwork is
     * present in the media.
     *
     * @param defaultArtwork the default artwork to display
     */
    public void setDefaultArtwork(@Nullable Drawable defaultArtwork) {
        if (this.defaultArtwork != defaultArtwork) {
            this.defaultArtwork = defaultArtwork;
            updateForCurrentTrackSelections(/* isNewPlayer= */ false);
        }
    }

    /**
     * Sets the background color of the {@code exo_shutter} view.
     *
     * @param color The background color.
     */
    public void setShutterBackgroundColor(int color) {
        if (shutterView != null) {
            shutterView.setBackgroundColor(color);
        }
    }

    /**
     * Sets whether the currently displayed video frame or media artwork is kept visible when the
     * player is reset. A player reset is defined to mean the player being re-prepared with different
     * media, the player transitioning to unprepared media, {@link Player#stop(boolean)} being called
     * with {@code reset=true}, or the player being replaced or cleared by calling {@link
     * #setPlayer(Player)}.
     *
     * <p>If enabled, the currently displayed video frame or media artwork will be kept visible until
     * the player set on the view has been successfully prepared with new media and loaded enough of
     * it to have determined the available tracks. Hence enabling this option allows transitioning
     * from playing one piece of media to another, or from using one player instance to another,
     * without clearing the view's content.
     *
     * <p>If disabled, the currently displayed video frame or media artwork will be hidden as soon as
     * the player is reset. Note that the video frame is hidden by making {@code exo_shutter} visible.
     * Hence the video frame will not be hidden if using a custom layout that omits this view.
     *
     * @param keepContentOnPlayerReset Whether the currently displayed video frame or media artwork is
     *     kept visible when the player is reset.
     */
    public void setKeepContentOnPlayerReset(boolean keepContentOnPlayerReset) {
        if (this.keepContentOnPlayerReset != keepContentOnPlayerReset) {
            this.keepContentOnPlayerReset = keepContentOnPlayerReset;
            updateForCurrentTrackSelections(/* isNewPlayer= */ false);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (player != null && player.isPlayingAd()) {
            return super.dispatchKeyEvent(event);
        }
        boolean isDpadWhenControlHidden = isDpadKey(event.getKeyCode());
        boolean handled = isDpadWhenControlHidden || super.dispatchKeyEvent(event);
        return handled;
    }

    /**
     * Set the {@link AspectRatioFrameLayout.AspectRatioListener}.
     *
     * @param listener The listener to be notified about aspect ratios changes of the video content or
     *     the content frame.
     */
    public void setAspectRatioListener(AspectRatioFrameLayout.AspectRatioListener listener) {
        Assertions.checkState(contentFrame != null);
        contentFrame.setAspectRatioListener(listener);
    }

    /**
     * Gets the view onto which video is rendered. This is a:
     *
     * <ul>
     *   <li>{@link SurfaceView} by default, or if the {@code surface_type} attribute is set to {@code
     *       surface_view}.
     *   <li>{@link TextureView} if {@code surface_type} is {@code texture_view}.
     *   <li>{@link SphericalSurfaceView} if {@code surface_type} is {@code spherical_view}.
     *   <li>{@code null} if {@code surface_type} is {@code none}.
     * </ul>
     *
     * @return The {@link SurfaceView}, {@link TextureView}, {@link SphericalSurfaceView} or {@code
     *     null}.
     */
    public View getVideoSurfaceView() {
        return surfaceView;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        return performClick();
    }

    /**
     * Should be called when the player is visible to the user and if {@code surface_type} is {@code
     * spherical_view}. It is the counterpart to {@link #onPause()}.
     *
     * <p>This method should typically be called in {@link Activity#onStart()}, or {@link
     * Activity#onResume()} for API versions &lt;= 23.
     */
    public void onResume() {
        if (surfaceView instanceof SphericalSurfaceView) {
            ((SphericalSurfaceView) surfaceView).onResume();
        }
    }

    /**
     * Should be called when the player is no longer visible to the user and if {@code surface_type}
     * is {@code spherical_view}. It is the counterpart to {@link #onResume()}.
     *
     * <p>This method should typically be called in {@link Activity#onStop()}, or {@link
     * Activity#onPause()} for API versions &lt;= 23.
     */
    public void onPause() {
        if (surfaceView instanceof SphericalSurfaceView) {
            ((SphericalSurfaceView) surfaceView).onPause();
        }
    }

    /**
     * Called when there's a change in the aspect ratio of the content being displayed. The default
     * implementation sets the aspect ratio of the content frame to that of the content, unless the
     * content view is a {@link SphericalSurfaceView} in which case the frame's aspect ratio is
     * cleared.
     *
     * @param contentAspectRatio The aspect ratio of the content.
     * @param contentFrame The content frame, or {@code null}.
     * @param contentView The view that holds the content being displayed, or {@code null}.
     */
    protected void onContentAspectRatioChanged(
            float contentAspectRatio,
            @Nullable AspectRatioFrameLayout contentFrame,
            @Nullable View contentView) {
        if (contentFrame != null) {
            contentFrame.setAspectRatio(
                    contentView instanceof SphericalSurfaceView ? 0 : contentAspectRatio);
        }
    }


    private void updateForCurrentTrackSelections(boolean isNewPlayer) {
        if (player == null || player.getCurrentTrackGroups().isEmpty()) {
            if (!keepContentOnPlayerReset) {
                hideArtwork();
                closeShutter();
            }
            return;
        }

        if (isNewPlayer && !keepContentOnPlayerReset) {
            // Hide any video from the previous player.
            closeShutter();
        }

        TrackSelectionArray selections = player.getCurrentTrackSelections();
        for (int i = 0; i < selections.length; i++) {
            if (player.getRendererType(i) == C.TRACK_TYPE_VIDEO && selections.get(i) != null) {
                // Video enabled so artwork must be hidden. If the shutter is closed, it will be opened in
                // onRenderedFirstFrame().
                hideArtwork();
                return;
            }
        }

        // Video disabled so the shutter must be closed.
        closeShutter();
        // Display artwork if enabled and available, else hide it.
        if (useArtwork) {
            for (int i = 0; i < selections.length; i++) {
                TrackSelection selection = selections.get(i);
                if (selection != null) {
                    for (int j = 0; j < selection.length(); j++) {
                        Metadata metadata = selection.getFormat(j).metadata;
                        if (metadata != null && setArtworkFromMetadata(metadata)) {
                            return;
                        }
                    }
                }
            }
            if (setDrawableArtwork(defaultArtwork)) {
                return;
            }
        }
        // Artwork disabled or unavailable.
        hideArtwork();
    }

    private boolean setArtworkFromMetadata(Metadata metadata) {
        for (int i = 0; i < metadata.length(); i++) {
            Metadata.Entry metadataEntry = metadata.get(i);
            if (metadataEntry instanceof ApicFrame) {
                byte[] bitmapData = ((ApicFrame) metadataEntry).pictureData;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
                return setDrawableArtwork(new BitmapDrawable(getResources(), bitmap));
            }
        }
        return false;
    }

    private boolean setDrawableArtwork(@Nullable Drawable drawable) {
        if (drawable != null) {
            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();
            if (drawableWidth > 0 && drawableHeight > 0) {
                float artworkAspectRatio = (float) drawableWidth / drawableHeight;
                onContentAspectRatioChanged(artworkAspectRatio, contentFrame, artworkView);
                artworkView.setImageDrawable(drawable);
                artworkView.setVisibility(VISIBLE);
                return true;
            }
        }
        return false;
    }

    private void hideArtwork() {
        if (artworkView != null) {
            artworkView.setImageResource(android.R.color.transparent); // Clears any bitmap reference.
            artworkView.setVisibility(INVISIBLE);
        }
    }

    private void closeShutter() {
        if (shutterView != null) {
            shutterView.setVisibility(View.VISIBLE);
        }
    }

    @TargetApi(23)
    private static void configureEditModeLogoV23(Resources resources, ImageView logo) {
        logo.setImageDrawable(resources.getDrawable(R.drawable.exo_edit_mode_logo, null));
        logo.setBackgroundColor(resources.getColor(R.color.exo_edit_mode_background_color, null));
    }

    @SuppressWarnings("deprecation")
    private static void configureEditModeLogo(Resources resources, ImageView logo) {
        logo.setImageDrawable(resources.getDrawable(R.drawable.exo_edit_mode_logo));
        logo.setBackgroundColor(resources.getColor(R.color.exo_edit_mode_background_color));
    }

    @SuppressWarnings("ResourceType")
    private static void setResizeModeRaw(AspectRatioFrameLayout aspectRatioFrame, int resizeMode) {
        aspectRatioFrame.setResizeMode(resizeMode);
    }

    /** Applies a texture rotation to a {@link TextureView}. */
    private static void applyTextureViewRotation(TextureView textureView, int textureViewRotation) {
        float textureViewWidth = textureView.getWidth();
        float textureViewHeight = textureView.getHeight();
        if (textureViewWidth == 0 || textureViewHeight == 0 || textureViewRotation == 0) {
            textureView.setTransform(null);
        } else {
            Matrix transformMatrix = new Matrix();
            float pivotX = textureViewWidth / 2;
            float pivotY = textureViewHeight / 2;
            transformMatrix.postRotate(textureViewRotation, pivotX, pivotY);

            // After rotation, scale the rotated texture to fit the TextureView size.
            RectF originalTextureRect = new RectF(0, 0, textureViewWidth, textureViewHeight);
            RectF rotatedTextureRect = new RectF();
            transformMatrix.mapRect(rotatedTextureRect, originalTextureRect);
            transformMatrix.postScale(
                    textureViewWidth / rotatedTextureRect.width(),
                    textureViewHeight / rotatedTextureRect.height(),
                    pivotX,
                    pivotY);
            textureView.setTransform(transformMatrix);
        }
    }

    @SuppressLint("InlinedApi")
    private boolean isDpadKey(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_DPAD_UP_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_LEFT
                || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                || keyCode == KeyEvent.KEYCODE_DPAD_UP_LEFT
                || keyCode == KeyEvent.KEYCODE_DPAD_CENTER;
    }

    private final class ComponentListener
            implements Player.EventListener,
            TextOutput,
            VideoListener,
            OnLayoutChangeListener,
            SphericalSurfaceView.SurfaceListener,
            SingleTapListener {

        // TextOutput implementation

        @Override
        public void onCues(List<Cue> cues) {

        }

        // VideoListener implementation

        @Override
        public void onVideoSizeChanged(
                int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            float videoAspectRatio =
                    (height == 0 || width == 0) ? 1 : (width * pixelWidthHeightRatio) / height;

            if (surfaceView instanceof TextureView) {
                // Try to apply rotation transformation when our surface is a TextureView.
                if (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270) {
                    // We will apply a rotation 90/270 degree to the output texture of the TextureView.
                    // In this case, the output video's width and height will be swapped.
                    videoAspectRatio = 1 / videoAspectRatio;
                }
                if (textureViewRotation != 0) {
                    surfaceView.removeOnLayoutChangeListener(this);
                }
                textureViewRotation = unappliedRotationDegrees;
                if (textureViewRotation != 0) {
                    // The texture view's dimensions might be changed after layout step.
                    // So add an OnLayoutChangeListener to apply rotation after layout step.
                    surfaceView.addOnLayoutChangeListener(this);
                }
                applyTextureViewRotation((TextureView) surfaceView, textureViewRotation);
            }

            onContentAspectRatioChanged(videoAspectRatio, contentFrame, surfaceView);
        }

        @Override
        public void onRenderedFirstFrame() {
            if (shutterView != null) {
                shutterView.setVisibility(INVISIBLE);
            }
        }

        @Override
        public void onTracksChanged(TrackGroupArray tracks, TrackSelectionArray selections) {
            updateForCurrentTrackSelections(/* isNewPlayer= */ false);
        }

        // Player.EventListener implementation

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        }

        @Override
        public void onPositionDiscontinuity(@DiscontinuityReason int reason) {
        }

        // OnLayoutChangeListener implementation

        @Override
        public void onLayoutChange(
                View view,
                int left,
                int top,
                int right,
                int bottom,
                int oldLeft,
                int oldTop,
                int oldRight,
                int oldBottom) {
            applyTextureViewRotation((TextureView) view, textureViewRotation);
        }

        // SphericalSurfaceView.SurfaceTextureListener implementation

        @Override
        public void surfaceChanged(@Nullable Surface surface) {
            if (player != null) {
                VideoComponent videoComponent = player.getVideoComponent();
                if (videoComponent != null) {
                    videoComponent.setVideoSurface(surface);
                }
            }
        }

        // SingleTapListener implementation

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }
}
