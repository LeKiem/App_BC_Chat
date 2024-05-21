package hunre.it.app_bc_chat.Helper;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hunre.it.app_bc_chat.Domain.SanPhamUser;

public class ManagmentCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertItem(SanPhamUser item) {
        ArrayList<SanPhamUser> listfood = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int y = 0; y < listfood.size(); y++) {
            if (listfood.get(y).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = y;
                break;
            }
        }
        if (existAlready) {
            listfood.get(n).setNumberinCart(item.getNumberinCart());
        } else {
            listfood.add(item);
        }
        tinyDB.putListObject("CartList", listfood);
        Toast.makeText(context, "Sản phảm đã được thêm vào giỏ hàng của bạn", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<SanPhamUser> getListCart() {
        return tinyDB.getListObject("CartList", SanPhamUser.class);
    }

    public void minusItem(ArrayList<SanPhamUser> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listfood.get(position).getNumberinCart() == 1) {
            listfood.remove(position);
        } else {
            listfood.get(position).setNumberinCart(listfood.get(position).getNumberinCart() - 1);
        }
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }

    public void plusItem(ArrayList<SanPhamUser> listfood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listfood.get(position).setNumberinCart(listfood.get(position).getNumberinCart() + 1);
        tinyDB.putListObject("CartList", listfood);
        changeNumberItemsListener.changed();
    }

    public Double getTotalFee() {
        List<SanPhamUser> cartItems = getListCart();
        double totalFee = 0;
        for (SanPhamUser item : cartItems) {
            try {
                double giaGoc = Double.parseDouble(item.getGiaGoc()); // Chuyển chuỗi sang double
                int numberInCart = item.getNumberinCart(); // Số lượng trong giỏ hàng

                totalFee += giaGoc * numberInCart;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return totalFee;
    }
}
