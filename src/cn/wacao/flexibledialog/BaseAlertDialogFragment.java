package cn.wacao.flexibledialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import  cn.wacao.flexibledialog.R;

/**
 * Created by xujiexing on 13-10-11.
 */
public abstract class BaseAlertDialogFragment extends DialogFragment {
    private static final String TAG = "BaseAlertDialogFragment";
    public static final String DIALOG_STYLE = "dialog_style";
    public static final String DIALOG_ONCANCEL_LISTENER = "cancel_listener";
    public static final String DIALOG_ONKEY_LISTENER = "onkey_listener";

    private int dialog_style;
    private FlexibleOnDismissListener mOnDismissListener;
    private boolean mCanCancelTouchOutSide;
    private FlexibleOnKeyListener mKeyListener;

    public static abstract class FlexibleOnDismissListener implements DialogInterface.OnCancelListener, Parcelable {
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }

    public static abstract class FlexibleOnKeyListener implements DialogInterface.OnKeyListener, Parcelable {
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onCancel(dialog);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewBuilder builder = new ViewBuilder(getActivity(), inflater, container);
        return build(builder).build();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        restoreArgus();

        Dialog dialog = null;

        if (dialog_style > 0) {
            dialog = new Dialog(getActivity(), dialog_style);
        } else {
            dialog = new Dialog(getActivity(), R.style.Mixture_Dialog);
        }

        final TypedArray a = getActivity().getTheme().obtainStyledAttributes(null, R.styleable.DialogStyle, R.attr.mixtureDialogStyle, 0);
        Drawable dialogBackground = a.getDrawable(R.styleable.DialogStyle_dialogBackground);
        a.recycle();
        dialog.getWindow().setBackgroundDrawable(dialogBackground);

//        dialog.setOnDismissListener(mOnDismissListener);
        dialog.setCanceledOnTouchOutside(mCanCancelTouchOutSide);

        if (mKeyListener != null)
            dialog.setOnKeyListener(mKeyListener);

        return dialog;
    }

    @Override
    public void onDestroyView() {
        // bug in the compatibility library
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    private void restoreArgus() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            dialog_style = bundle.getInt(DIALOG_STYLE);
            mCanCancelTouchOutSide = bundle.getBoolean(BaseAlertDialogBuilder.CANCELABLE_TOUCH_OUTSIDE);
            mOnDismissListener = bundle.getParcelable(DIALOG_ONCANCEL_LISTENER);
            mKeyListener = bundle.getParcelable(DIALOG_ONKEY_LISTENER);
            Log.v(TAG, "mCanCancelTouchOutSide = " + mCanCancelTouchOutSide + ", mOnDismissListener = " + mOnDismissListener);
        }
    }

    protected abstract ViewBuilder build(ViewBuilder builder);

    /**
     * Dialog content view builder.
     * It's used to create custom view;
     */
    public static class ViewBuilder {
        private Context mContext;
        private LayoutInflater mInflater;
        private final Resources mResources;
        private ViewGroup mContainer;

        private TextView mTitle;
        private LinearLayout mContentView;
        private View mSeperator;
        private int mTitleTextColor;
        private int mTitleSeparatorColor;
        private int mMessageTextColor;
        private ColorStateList mButtonTextColor;
        private int mButtonSeparatorColor;
        private int mButtonBackgroundColorNormal;
        private int mButtonBackgroundColorPressed;
        private int mButtonBackgroundColorFocused;

        private Drawable mButtonBackgroundDrawableNormal;
        private Drawable mButtonBackgroundDrawablePressed;
        private Drawable mButtonBackgroundDrawableFocused;
        private boolean actionBtnUseColor;

        private String mTitleString;
        private Drawable mTitleIcon;
        private Drawable mCloseIcon;
        private String mPositiveTitle;
        private View.OnClickListener mPositiveListener;
        private String mNegativeTitle;
        private View.OnClickListener mNegativeListener;
        private String mNeutralTitle;
        private View.OnClickListener mNeutralListener;
        private CharSequence mMessageString;
        private View mCustomView;
        private boolean mViewSpacingSpecified;
        private int mViewSpacingLeft;
        private int mViewSpacingTop;
        private int mViewSpacingRight;
        private int mViewSpacingBottom;
        private ImageView mTitleCloseIcon;
        private View.OnClickListener mCloseListener;

        public ViewBuilder(Context context, LayoutInflater inflater, ViewGroup container) {
            mContext = context;
            mInflater = inflater;
            mContainer = container;
            mResources = context.getResources();
        }

        public LayoutInflater getLayoutInflater() {
            return mInflater;
        }

        public View build() {
            this.getResource();
            View rootView = this.setDialogView();
            this.setInformation();
            return rootView;
        }

        /**
         * Set all information, including title, message, action button.
         */
        private void setInformation() {
            this.setTitleInfo();
            this.setMessage();
            this.setCustomView();
            this.setActionButton();
        }

        private void setActionButton() {
            if (mNegativeTitle != null || mNeutralTitle != null || mPositiveTitle != null) {
                View buttonPanel = mInflater.inflate(R.layout.dialog_button_panel, mContentView, false);
                LinearLayout horizontalButtonPanel = (LinearLayout) buttonPanel.findViewById(R.id.dialog_button_horizontal_panel);
                buttonPanel.findViewById(R.id.dialog_horizontal_separator).setBackgroundColor(mButtonSeparatorColor);

                boolean addDivider = false;

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    addDivider = addPositiveButton(horizontalButtonPanel, addDivider);
                } else {
                    addDivider = addNegativeButton(horizontalButtonPanel, addDivider);
                }
                addDivider = addNeutralButton(horizontalButtonPanel, addDivider);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    addNegativeButton(horizontalButtonPanel, addDivider);
                } else {
                    addPositiveButton(horizontalButtonPanel, addDivider);
                }

                mContentView.addView(buttonPanel);
            }
        }

        private boolean addNegativeButton(ViewGroup parent, boolean addDivider) {
            if (mNegativeTitle != null) {
                if (addDivider) {
                    addDivider(parent);
                }
                Button btn = (Button) mInflater.inflate(R.layout.dialog_action_button, parent, false);
                btn.setId(R.id.dialog__negative_button);
                btn.setText(mNegativeTitle);
                btn.setTextColor(mButtonTextColor);
                btn.setOnClickListener(mNegativeListener);
                this.setActionButtonBackground(btn);
                parent.addView(btn);
                return true;
            }
            return addDivider;
        }

        private boolean addPositiveButton(ViewGroup parent, boolean addDivider) {
            if (mPositiveTitle != null) {
                if (addDivider) {
                    addDivider(parent);
                }
                Button btn = (Button) mInflater.inflate(R.layout.dialog_action_button, parent, false);
                btn.setId(R.id.dialog__positive_button);
                btn.setText(mPositiveTitle);
                btn.setTextColor(mButtonTextColor);
                btn.setOnClickListener(mPositiveListener);
                this.setActionButtonBackground(btn);
                parent.addView(btn);
                return true;
            }
            return addDivider;
        }

        private boolean addNeutralButton(ViewGroup parent, boolean addDivider) {
            if (mNeutralTitle != null) {
                if (addDivider) {
                    addDivider(parent);
                }
                Button btn = (Button) mInflater.inflate(R.layout.dialog_action_button, parent, false);
                btn.setId(R.id.dialog__neutral_button);
                btn.setText(mNeutralTitle);
                btn.setTextColor(mButtonTextColor);
                btn.setOnClickListener(mNeutralListener);
                this.setActionButtonBackground(btn);
                parent.addView(btn);
                return true;
            }
            return addDivider;
        }

        private void addDivider(ViewGroup parent) {
            View view = mInflater.inflate(R.layout.dialog_aciton_button_separator, parent, false);
            view.findViewById(R.id.dialog_button_separator).setBackgroundColor(mButtonSeparatorColor);
            parent.addView(view);
        }

        /**
         * Set simple message to content view
         */
        private void setMessage() {
            if (mMessageString != null && mMessageString.length() > 0) {
                View viewMessage = mInflater.inflate(R.layout.dialog_message_layout, mContentView, false);
                TextView tvMessage = (TextView) viewMessage.findViewById(R.id.dilog_message);
                tvMessage.setText(mMessageString);
                tvMessage.setTextColor(mMessageTextColor);
                tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
                mContentView.addView(viewMessage);
            }
        }

        /**
         * Set a custom view to content view
         */
        private void setCustomView() {
            if (mCustomView != null) {
                FrameLayout customPanel = (FrameLayout) mInflater.inflate(R.layout.dialog_custom_view, mContentView, false);
                FrameLayout custom = (FrameLayout) customPanel.findViewById(R.id.dialog_custom_content);
                custom.addView(mCustomView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                if (mViewSpacingSpecified) {
                    custom.setPadding(mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
                }
                mContentView.addView(customPanel);
            }
        }


        private void setTitleInfo() {
            if (mTitleString != null && mTitleString.length() > 0) {
                this.mTitle.setText(mTitleString);
                this.mTitle.setTextColor(mTitleTextColor);
                this.mSeperator.setBackgroundColor(mTitleSeparatorColor);

                if (mTitleIcon != null) {
                    this.mTitle.setCompoundDrawablesWithIntrinsicBounds(mTitleIcon, null, null, null);
                }

                if (mCloseIcon != null) {
                    mTitleCloseIcon.setImageDrawable(mCloseIcon);
                    mTitleCloseIcon.setOnClickListener(mCloseListener);
                }
            } else {
                this.mTitle.setVisibility(View.GONE);
                this.mSeperator.setVisibility(View.GONE);
            }
        }

        /**
         * Inflate layout from xml
         *
         * @return The content vie of dialog
         */
        private View setDialogView() {
            View view = mInflater.inflate(R.layout.mixture_dialog_layout, mContainer, false);
            mTitle = (TextView) view.findViewById(R.id.dialog_title);
            mTitleCloseIcon = (ImageView) view.findViewById(R.id.dialog_title_close);
            mContentView = (LinearLayout) view.findViewById(R.id.dialog_content);
            mSeperator = view.findViewById(R.id.dialog__titleDivider);
            return view;
        }

        /**
         * Get all resource related to dialog
         */
        public void getResource() {
            final int defaultTitleTextColor = mResources.getColor(R.color.sdl_title_text_dark);
            final int defaultTitleSeparatorColor = mResources.getColor(R.color.sdl_title_separator_dark);
            final int defaultMessageTextColor = mResources.getColor(R.color.sdl_message_text_dark);
            final ColorStateList defaultButtonTextColor = mResources.getColorStateList(R.color.sdl_button_text_dark);
            final Drawable defaultButtonBackgroundDrawableNormal = mResources.getDrawable(R.drawable.ic_launcher);
            final Drawable defaultButtonBackgroundDrawablePressed = mResources.getDrawable(R.drawable.ic_launcher);
            final Drawable defaultButtonBackgroundDrawableFocused = mResources.getDrawable(R.drawable.ic_launcher);

            final int defaultButtonSeparatorColor = mResources.getColor(R.color.sdl_button_separator_dark);
            final int defaultButtonBackgroundColorNormal = mResources.getColor(R.color.sdl_button_normal_dark);
            final int defaultButtonBackgroundColorPressed = mResources.getColor(R.color.sdl_button_pressed_dark);
            final int defaultButtonBackgroundColorFocused = mResources.getColor(R.color.sdl_button_focused_dark);

            final TypedArray a = mContext.getTheme().obtainStyledAttributes(null, R.styleable.DialogStyle, R.attr.mixtureDialogStyle, 0);
            actionBtnUseColor = a.getBoolean(R.styleable.DialogStyle_actionButtonColorOrDrawable, true);

            mTitleTextColor = a.getColor(R.styleable.DialogStyle_titleTextColor, defaultTitleTextColor);
            mTitleSeparatorColor = a.getColor(R.styleable.DialogStyle_titleSeparatorColor, defaultTitleSeparatorColor);
            mMessageTextColor = a.getColor(R.styleable.DialogStyle_messageTextColor, defaultMessageTextColor);
            mButtonTextColor = a.getColorStateList(R.styleable.DialogStyle_buttonTextColor);
            mCloseIcon = mCloseIcon == null ? a.getDrawable(R.styleable.DialogStyle_titleCloseIcon) : mCloseIcon;
            mTitleIcon = mTitleIcon == null ? a.getDrawable(R.styleable.DialogStyle_titleIcon) : mTitleIcon;

            if (mButtonTextColor == null) {
                mButtonTextColor = defaultButtonTextColor;
            }

            if (actionBtnUseColor) {
                mButtonSeparatorColor = a.getColor(R.styleable.DialogStyle_buttonSeparatorColor, defaultButtonSeparatorColor);
                mButtonBackgroundColorNormal = a.getColor(R.styleable.DialogStyle_buttonBackgroundColorNormal, defaultButtonBackgroundColorNormal);
                mButtonBackgroundColorPressed = a.getColor(R.styleable.DialogStyle_buttonBackgroundColorPressed, defaultButtonBackgroundColorPressed);
                mButtonBackgroundColorFocused = a.getColor(R.styleable.DialogStyle_buttonBackgroundColorFocused, defaultButtonBackgroundColorFocused);
            } else {
                mButtonBackgroundDrawableNormal = a.getDrawable(R.styleable.DialogStyle_buttonBackgroundDrawableNormal);
                mButtonBackgroundDrawableFocused = a.getDrawable(R.styleable.DialogStyle_buttonBackgroundDrawableNormal);
                mButtonBackgroundDrawablePressed = a.getDrawable(R.styleable.DialogStyle_buttonBackgroundDrawableNormal);
                if (mButtonBackgroundDrawableNormal == null) {
                    mButtonBackgroundDrawableNormal = defaultButtonBackgroundDrawableNormal;
                }

                if (mButtonBackgroundDrawablePressed == null) {
                    mButtonBackgroundDrawablePressed = defaultButtonBackgroundDrawablePressed;
                }

                if (mButtonBackgroundDrawableFocused == null) {
                    mButtonBackgroundDrawableFocused = defaultButtonBackgroundDrawableFocused;
                }
            }

            a.recycle();
        }

        public ViewBuilder setTitle(String title) {
            mTitleString = title;
            return this;
        }

        public ViewBuilder setTitlte(int resId) {
            return setTitle(mResources.getString(resId));
        }

        public ViewBuilder setTitleIcon(int resId) {
            if (resId > 0) {
                mTitleIcon = mResources.getDrawable(resId);
            }
            return this;
        }

        public ViewBuilder setTitleCloseIcon(int resId, View.OnClickListener closeListener) {
            if (resId > 0) {
                mCloseIcon = mResources.getDrawable(resId);
                mCloseListener = closeListener;
            }
            return this;
        }

        public ViewBuilder setMessage(CharSequence message) {
            mMessageString = message;
            return this;
        }

        public ViewBuilder setMessage(int resId) {
            mMessageString = mResources.getString(resId);
            return this;
        }

        public ViewBuilder setPositiveButton(String positiveTitle, final View.OnClickListener positiveListener) {
            mPositiveTitle = positiveTitle;
            mPositiveListener = positiveListener;
            return this;
        }

        public ViewBuilder setPositiveButton(int resId, final View.OnClickListener positiveListener) {
            mPositiveTitle = mResources.getString(resId);
            mPositiveListener = positiveListener;
            return this;
        }

        public ViewBuilder setNegativeButton(String negativeTitle, final View.OnClickListener negativeListener) {
            mNegativeTitle = negativeTitle;
            mNegativeListener = negativeListener;
            return this;
        }

        public ViewBuilder setNegativeButton(int resId, final View.OnClickListener negativeListener) {
            mNegativeTitle = mResources.getString(resId);
            mNegativeListener = negativeListener;
            return this;
        }

        public ViewBuilder setNeutralButton(String neutralTitle, final View.OnClickListener neutralListener) {
            mNeutralTitle = neutralTitle;
            mNeutralListener = neutralListener;
            return this;
        }

        public ViewBuilder setNeutralButton(int resId, final View.OnClickListener neutralListener) {
            mNeutralTitle = mResources.getString(resId);
            mNeutralListener = neutralListener;
            return this;
        }

        public ViewBuilder setView(View view) {
            mCustomView = view;
            mViewSpacingSpecified = false;
            return this;
        }

        public ViewBuilder setView(int viewId) {
            mCustomView = LayoutInflater.from(mContext).inflate(viewId, null);
            mViewSpacingSpecified = false;
            return this;
        }

        public View getCustomView() {
            return mCustomView;
        }

        public ViewBuilder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
            mCustomView = view;
            mViewSpacingSpecified = true;
            mViewSpacingLeft = viewSpacingLeft;
            mViewSpacingTop = viewSpacingTop;
            mViewSpacingRight = viewSpacingRight;
            mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        private void setActionButtonBackground(Button btn) {
            if (actionBtnUseColor) {
                btn.setBackgroundDrawable(getButtonColorBackground());
            } else {
                btn.setBackgroundDrawable(getButtonDrawableBackground());
            }
        }

        private StateListDrawable getButtonColorBackground() {
            int[] pressedState = {android.R.attr.state_pressed};
            int[] focusedState = {android.R.attr.state_focused};
            int[] defaultState = {android.R.attr.state_enabled};
            ColorDrawable colorDefault = new ColorDrawable(mButtonBackgroundColorNormal);
            ColorDrawable colorPressed = new ColorDrawable(mButtonBackgroundColorPressed);
            ColorDrawable colorFocused = new ColorDrawable(mButtonBackgroundColorFocused);
            StateListDrawable background = new StateListDrawable();
            background.addState(pressedState, colorPressed);
            background.addState(focusedState, colorFocused);
            background.addState(defaultState, colorDefault);
            return background;
        }

        private StateListDrawable getButtonDrawableBackground() {
            int[] pressedState = {android.R.attr.state_pressed};
            int[] focusedState = {android.R.attr.state_focused};
            int[] defaultState = {android.R.attr.state_enabled};
            StateListDrawable background = new StateListDrawable();
            background.addState(pressedState, mButtonBackgroundDrawablePressed);
            background.addState(focusedState, mButtonBackgroundDrawableFocused);
            background.addState(defaultState, mButtonBackgroundDrawableNormal);
            return background;
        }
    }
}
