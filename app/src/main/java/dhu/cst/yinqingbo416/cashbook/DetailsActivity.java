package dhu.cst.yinqingbo416.cashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    ImageView imageView;
    TextView name;
    TextView kind;
    TextView date;
    TextView remark;
    Button btn1,btn2;
    myAlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imageView = findViewById(R.id.detailiamge);
        name = findViewById(R.id.detailname);
        kind = findViewById(R.id.detailkind);
        date = findViewById(R.id.detaildate);
        remark = findViewById(R.id.detailremark);
        btn1 = findViewById(R.id.detail_btn1);
        btn2 = findViewById(R.id.detail_btn2);
        dialog = new myAlertDialog(DetailsActivity.this);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        List<Bill> bills =  DataSupport.where("id = ?",id).find(Bill.class);
        Bill bill = bills.get(0);
        imageView.setImageResource(bill.getImageId());
        name.setText(bill.getName());
        if(bill.getKind() == -1){
            kind.setText("支出");
        }else {
            kind.setText("收入");
        }
        date.setText(bill.getYear()+"年"+(bill.getMonth()+1)+"月"+bill.getDay()+"日");
        remark.setText(bill.getRemark());
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }
}
