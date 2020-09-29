package dhu.cst.yinqingbo416.cashbook;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class mDatePickerDialog extends DatePickerDialog {
    public mDatePickerDialog(@NonNull Context context, int themeResId, @Nullable OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, DatePickerDialog.THEME_HOLO_LIGHT, listener, year, monthOfYear, dayOfMonth);
        this.setTitle(year+"年"+(monthOfYear+1)+"月");
        //设置日不可见
        ((ViewGroup)((ViewGroup)(getDatePicker().getChildAt(0))).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
    }
    public mDatePickerDialog(@NonNull Context context, @Nullable OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, listener, year, month, dayOfMonth);
    }
    @Override
    public void onDateChanged(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
        super.onDateChanged(view, year, month, dayOfMonth);
        this.setTitle(year+"年"+(month+1)+"月");
    }
}
