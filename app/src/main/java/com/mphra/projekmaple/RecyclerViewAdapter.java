package com.mphra.projekmaple;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ItemList> mData;
    private String username;

    public RecyclerViewAdapter (Context mContext, List<ItemList> mData, String username) {
        this.mContext = mContext;
        this.mData = mData;
        this.username = username;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.itemTittle.setText(mData.get(position).getTittle());
        holder.itemThumbnail.setImageResource(mData.get(position).getThumbnail());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String userOption= mData.get(position).getTittle();

                switch (userOption) {
                    case "User Management":
                        intent = new Intent(mContext, UserManagement.class);
                        intent.putExtra("username", username);
                        break;

                    case "Vehicle Sticker":
                        intent = new Intent(mContext, VehicleSticker.class);
                        intent.putExtra("username", username);
                        break;

                    case "Security Payment":
                        intent = new Intent(mContext, SecurityPayment.class);
                        break;

                    case "Community Item Booking":
                        intent = new Intent(mContext, CommunityItemBooking.class);
                        break;

                    case "Setting":
                        intent = new Intent(mContext, Setting.class);
                        break;

                    default:
                        intent = new Intent(mContext, UserManagement.class);
                }

                intent.putExtra("Tittle",mData.get(position).getTittle());
                intent.putExtra("Thumbnail",mData.get(position).getThumbnail());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemTittle;
        ImageView itemThumbnail;
        CardView cardview;

        public MyViewHolder (View itemView) {
            super(itemView);
            itemTittle = itemView.findViewById(R.id.dashboard_item_text);
            itemThumbnail = itemView.findViewById(R.id.dashboard_item_image);
            cardview = itemView.findViewById(R.id.card_view);
        }
    }
}

/**code explanation

 Line 31-32 : Catching the variable passed from previous activity.

 Line 35-39 : Setting the image for profile picture circular.

 Line 41-51 : Setting the ItemList for the User to be displayed on DashBoard.

 Line 53-56 : Creating ReecycleView and sending the ItemList that has been defined to adapter. The Adapter will determine how the itemList
 will be displayed and how it will react. Eg. go to which activity when pressed on one of the itemList.

 **/
