package hunre.it.app_bc_chat.adapters.User;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.models.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        Order order = orderList.get(position);
        String ngayDat = order.getNgayDat();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(ngayDat));
        String ngay_dat = DateFormat.format("HH:mm dd/MM/yyyy", calendar).toString();
        holder.orderDateTextView.setText("Ngày đặt mua: " + ngay_dat);
        holder.orderIdTextView.setText("Mã hóa đơn: " + order.getMaHd());
//        holder.orderDateTextView.setText("Date: " + order.getNgayDat());
        holder.orderTotalTextView.setText("Số tiền phải thu: " + order.getTongHd() + "00đ");
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView orderIdTextView, orderDateTextView, orderTotalTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderDateTextView = itemView.findViewById(R.id.orderDateTextView);
            orderTotalTextView = itemView.findViewById(R.id.orderTotalTextView);
        }
    }
}

