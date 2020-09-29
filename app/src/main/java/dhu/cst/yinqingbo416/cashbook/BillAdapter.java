package dhu.cst.yinqingbo416.cashbook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private List<Bill> mBillList;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View billView;
        int id;
        ImageView BillImage;
        TextView BillKind;
        TextView BillDate;
        TextView BillMoney;
        TextView BillKindMoney;
        public ViewHolder(View view){
            super(view);
            billView = view;
            BillImage = view.findViewById(R.id.bill_list_image);
            BillKind = view.findViewById(R.id.bill_list_kind);
            BillDate = view.findViewById(R.id.bill_list_date);
            BillMoney = view.findViewById(R.id.bill_list_money);
            BillKindMoney = view.findViewById(R.id.bill_list_kind_money);
        }
    }
    public BillAdapter(Context context,List<Bill>BillList){
        mBillList = BillList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_list_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.billView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DetailsActivity.class);
                intent.putExtra("ID",String.valueOf(holder.id));
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = mBillList.get(position);
        holder.id = bill.getId();
        holder.BillImage.setImageResource(bill.getImageId());
        holder.BillKind.setText(bill.getName());
        String s1 = (String.valueOf(bill.getMonth()+1)+"月"+String.valueOf(bill.getDay())+"日");
        String s2;
        holder.BillDate.setText(s1);
        if(bill.getKind() == -1){
            s1 = "支出:";
            s2 = "-";
        }
        else {
            s1 = "收入:";
            s2 = "+";
        }
        s1+=String.format("%.2f",bill.getMoney());
        holder.BillKindMoney.setText(s1);
        holder.BillMoney.setText(s2+String.format("%.2f",bill.getMoney()));
    }

    @Override
    public int getItemCount() {
        return mBillList.size();
    }
}
