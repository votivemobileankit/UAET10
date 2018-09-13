package votive.com.appuaet10.CustomUI;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import votive.com.appuaet10.Utilities.AppData;


public class CustomBoldTextView extends android.support.v7.widget.AppCompatTextView{

    public CustomBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomBoldTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context aContext) {
        Typeface myTypeface = AppData.getInstance().getTypeFaceForType(aContext, AppData.FONT_OPEN_SANS_BOLD);
        setTypeface(myTypeface);
    }
}
