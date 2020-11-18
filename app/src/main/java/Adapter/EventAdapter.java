package Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prinsafan.eventmanagement.R;

import java.util.List;

import model.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private final Context context;
    private final List<Event> events;
    private Bitmap bitmap=null;
    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.event_card_view,null);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        Event event=events.get(position);
        holder.title.setText(event.getTitle());
        holder.type.setText(event.getType());
        holder.date.setText(event.getDate());
        holder.guest.setText(event.getGuestId());
        holder.venue.setText(event.getVenue());
        holder.desc.setText(event.getDesc());
        bitmap= BitmapFactory.decodeByteArray(event.getPhoto(),0,event.getPhoto().length);
        holder.img.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder{

        TextView title,type,date,venue,guest,desc;
        ImageView img;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.evtTitle);
            type=itemView.findViewById(R.id.evtType);
            date=itemView.findViewById(R.id.evtDate);
            venue=itemView.findViewById(R.id.evtVenue);
            guest=itemView.findViewById(R.id.evtGuest);
            img=itemView.findViewById(R.id.evtImg);
            desc=itemView.findViewById(R.id.eventDesc);
        }
    }
}
