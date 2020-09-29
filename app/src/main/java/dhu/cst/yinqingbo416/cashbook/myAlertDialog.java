package dhu.cst.yinqingbo416.cashbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class myAlertDialog {
    Context context;
    public myAlertDialog(Context context){
        this.context = context;
    }
    public void show(String str){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("提示");
        dialog.setMessage(str);
        dialog.setCancelable(true);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    public void show(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("提示");
        dialog.setMessage("该功能尚未完成，程序员正在加班加点中。。。");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
