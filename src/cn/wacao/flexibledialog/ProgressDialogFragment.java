package cn.wacao.flexibledialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.wacao.flexibledialog.R;

/**
 * Created by xujiexing on 13-12-4.
 */
public class ProgressDialogFragment extends BaseAlertDialogFragment {

    protected static String ARG_MESSAGE = "message";
    protected static String ARG_TITLE = "title";

    public static ProgressDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new ProgressDialogBuilder(context, fragmentManager, ProgressDialogFragment.class);
    }

    @Override
    protected ViewBuilder build(ViewBuilder builder) {
        final int defaultMessageTextColor = getResources().getColor(R.color.sdl_message_text_dark);
        final TypedArray a = getActivity().getTheme().obtainStyledAttributes(null, R.styleable.DialogStyle, R.attr.mixtureDialogStyle, 0);
        final int messageTextColor = a.getColor(R.styleable.DialogStyle_messageTextColor, defaultMessageTextColor);
        a.recycle();

        final LayoutInflater inflater = builder.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_progress, null, false);
        final TextView tvMessage = (TextView) view.findViewById(R.id.dilog_message);
        tvMessage.setText(getArguments().getString(ARG_MESSAGE));
        tvMessage.setTextColor(messageTextColor);

        builder.setView(view);

        builder.setTitle(getArguments().getString(ARG_TITLE));

        return builder;
    }

    public static class ProgressDialogBuilder extends BaseAlertDialogBuilder<ProgressDialogBuilder> {
        private String mTitle;
        private String mMessage;

        public ProgressDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseAlertDialogFragment> clazz) {
            super(context, fragmentManager, clazz);
        }

        @Override
        protected ProgressDialogBuilder self() {
            return this;
        }

        public ProgressDialogBuilder setTitle(int titleResourceId) {
            mTitle = mContext.getString(titleResourceId);
            return this;
        }


        public ProgressDialogBuilder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public ProgressDialogBuilder setMessage(int messageResourceId) {
            mMessage = mContext.getString(messageResourceId);
            return this;
        }

        public ProgressDialogBuilder setMessage(String message) {
            mMessage = message;
            return this;
        }

        @Override
        protected Bundle prepareArguments() {
            Bundle args = new Bundle();
            args.putString(ProgressDialogFragment.ARG_MESSAGE, mMessage);
            args.putString(ProgressDialogFragment.ARG_TITLE, mTitle);

            return args;
        }
    }
}
