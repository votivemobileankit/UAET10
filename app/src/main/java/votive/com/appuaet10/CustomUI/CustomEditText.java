package votive.com.appuaet10.CustomUI;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import votive.com.appuaet10.Utilities.AppData;

public class CustomEditText extends android.support.v7.widget.AppCompatEditText{
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context,  attrs);
        init(context);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context aContext) {
        Typeface myTypeface = AppData.getInstance().getTypeFaceForType(aContext, AppData.FONT_OPEN_SANS_REGULAR);
        setTypeface(myTypeface);
    }
}
