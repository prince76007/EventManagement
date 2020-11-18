package Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prinsafan.eventmanagement.R;

import java.util.List;

import model.Guest;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.GuestViewHolder>{

    private final Context context;
    private final List<Guest> guests;
    public GuestAdapter(Context context, List<Guest> guests) {
        this.context = context;
        this.guests = guests;
    }
    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.guest_card_view,null);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        Guest guest=guests.get(position);
        holder.gName.setText(guest.getFullName());
        holder.gEmail.setText(guest.getEmail());
        holder.imageView.setImageResource(R.drawable.ic_login);
    }

    @Override
    public int getItemCount() {
        return guests.size();
    }

    static class GuestViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView gEmail,gName;
        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            gEmail=itemView.findViewById(R.id.gMail);
            gName=itemView.findViewById(R.id.gName);
            imageView=itemView.findViewById(R.id.guestImg);
        }
    }
}
