package com.mcligeyo.rsmhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.mcligeyo.rsmhub.R;
import com.mcligeyo.rsmhub.RequisitionDetailsEditActivity;
import com.mcligeyo.rsmhub.model.RequisitionItem;

import java.util.List;
import java.util.Random;

public class RequisitionItemsExpensesAdapter extends RecyclerView.Adapter<RequisitionItemsExpensesAdapter.ViewHolder> {
    private Context mcontext;
    private List<RequisitionItem> requisitionItems;


    public RequisitionItemsExpensesAdapter(Context mcontext, List<RequisitionItem> requisitionItems) {
        this.mcontext = mcontext;
        this.requisitionItems = requisitionItems;
    }

    @NonNull
    @Override
    public RequisitionItemsExpensesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_requisition_expenses, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RequisitionItemsExpensesAdapter.ViewHolder holder, final int position) {
//get randomcolor to be se in profile textview
        int[] colorArrs = mcontext.getResources().getIntArray(R.array.color_array_1);
        int ranDomColor = createRandomColor(colorArrs);

        //Capitalize allwordss in title and get first character of title string
        final RequisitionItem requisitionItem = requisitionItems.get(position);
        String requisitionItemName = String.valueOf(requisitionItem.getItemname().toUpperCase().charAt(0));
        holder.requsitionNameTextView.setText(requisitionItem.getItemname().toUpperCase());
//        holder.requisitionSupplierTextView.setText(requisitionItem.getSupplier());
//        holder.requisitionUnitCostTextView.setText(String.valueOf(requisitionItem.getUnit_cost()));
//        holder.requisitionQuantityTextView.setText(requisitionItem.getQuantity());
        holder.requisitionTotalTextView.setText(String.valueOf(requisitionItem.getTotal()));

        //
        holder.profileTextView.setBackgroundColor(ranDomColor);
        holder.profileTextView.setText(requisitionItemName);

        // holder.deleteImageView.bringToFront();
//        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeItem(position);
//
//            }
//        });
    }

    private static int createRandomColor(int[] array) {
        //generate random number
        int randNum = new Random().nextInt(array.length);
        return array[randNum];

        //above shorter
//        int max = 39;
//        int min = 1;
//        int range = max - min + 1;
//
//        for(int i = 0; i < array.length; i++){
//           int rand = (int) ((Math.random()*range) + min);
//            return array[rand];
//        }


    }

    @Override
    public int getItemCount() {
        return ((requisitionItems != null) && (requisitionItems.size() != 0) ? requisitionItems.size() : 0);

//        if(requisitionItems != null && requisitionItems.size() != 0){
//            return requisitionItems.size();
//        } else {
//            return 0;
//        }
    }

    //overiden timeline method, included for timeline view
    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    //function called at response of
    public void loadNewData(List<RequisitionItem> requisitionItems1) {
        this.requisitionItems = requisitionItems1;
        notifyDataSetChanged();
    }

    //when a single item removed
    public void removeItem(int position) {
        this.requisitionItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.requisitionItems.size());
    }

    //when a single item added

    public void addItem(int position, RequisitionItem requisitionItem) {
        this.requisitionItems.add(position, requisitionItem);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, requisitionItems.size());
    }

    public void addItem1(RequisitionItem requisitionItem) {
        this.requisitionItems.add(requisitionItem);
        //notifyDataSetChanged();
        notifyItemInserted(requisitionItems.size() - 1);
//        notifyItemInserted(position);
        notifyItemRangeChanged(requisitionItems.size() - 1, requisitionItems.size());
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView profileTextView, requsitionNameTextView, requisitionSupplierTextView, requisitionUnitCostTextView, requisitionQuantityTextView, requisitionTotalTextView;
        //private ImageView deleteImageView;
        public TimelineView mTimelineView;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);


            profileTextView = itemView.findViewById(R.id.text_view_list_view_profile);
            requsitionNameTextView = itemView.findViewById(R.id.text_view_list_view_requisition_name);
            //deleteImageView = itemView.findViewById(R.id.list_view_req_expenses_img_delete);

//            requisitionSupplierTextView = itemView.findViewById(R.id.text_view_supplier);
//            requisitionUnitCostTextView = itemView.findViewById(R.id.text_view_unit_cost);
//            requisitionQuantityTextView = itemView.findViewById(R.id.text_view_quantity);
            requisitionTotalTextView = itemView.findViewById(R.id.text_view_total);

            mTimelineView = itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // if they click on an item, we need to know position of item clicked
                    //Get postion of rowclicked/tapped
                    int position = getAdapterPosition();

                    RequisitionItem requisitionItem = requisitionItems.get(position);

                    Intent intent = new Intent(mcontext, RequisitionDetailsEditActivity.class);
                    //from

                    intent.putExtra("reqItemName", requisitionItem.getItemname());
                    intent.putExtra("reqItemSupplier", requisitionItem.getSupplier());
                    intent.putExtra("reqItemUnitCost", requisitionItem.getUnit_cost());
                    intent.putExtra("reqItemQuantity", requisitionItem.getQuantity());
                    intent.putExtra("requisition_id", requisitionItem.getRequisition_id());
                    intent.putExtra("reqItemTotal", requisitionItem.getTotal());
                    intent.putExtra("id", requisitionItem.getId());
                    mcontext.startActivity(intent);

                }
            });
        }
    }


}
