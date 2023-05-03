package com.mcligeyo.rsmhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcligeyo.rsmhub.R;
import com.mcligeyo.rsmhub.model.RequisitionItem;

import java.util.List;
import java.util.Random;

public class RequisitionItemsExpensesAdapter2 extends RecyclerView.Adapter<RequisitionItemsExpensesAdapter2.ViewHolder> {
    private Context mcontext;
    private List<RequisitionItem> requisitionItems;

    public RequisitionItemsExpensesAdapter2(Context mcontext, List<RequisitionItem> requisitionItems) {
        this.mcontext = mcontext;
        this.requisitionItems = requisitionItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.list_view_requisiton_expenses2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get randomcolor to be se in profile textview
        int[] colorsArray = mcontext.getResources().getIntArray(R.array.color_array_1);
        int randomColor = createRandomColor(colorsArray);


        //Capitalize allwordss in title and get first character of title string
        RequisitionItem requisitionItem = requisitionItems.get(position);
        String title = String.valueOf(requisitionItem.getItemname().charAt(0));
        holder.requsitionNameTextView.setText(requisitionItem.getItemname().toUpperCase());
        holder.requisitionTotalTextView.setText(String.valueOf(requisitionItem.getTotal()));
        holder.profileTextView.setBackgroundColor(randomColor);
        holder.profileTextView.setText(title);

    }

    public static int createRandomColor(int[] array) {
        int rand = new Random().nextInt(array.length);
        return array[rand];
    }

    @Override
    public int getItemCount() {
        return requisitionItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView profileTextView, requsitionNameTextView, requisitionTotalTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileTextView = itemView.findViewById(R.id.text_view_list_view_profile2);
            requsitionNameTextView = itemView.findViewById(R.id.text_view_list_view_requisition_name2);
            requisitionTotalTextView = itemView.findViewById(R.id.text_view_total2);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    RequisitionItem requisitionItem1 = requisitionItems.get(position);
//                    Intent intent = new Intent(mcontext, RequisitionDetailsEditActivity.class);
//                    intent.putExtra("reqItemViewGetIdItemName",requisitionItem1.getItemname());
//                    intent.putExtra("reqItemViewGetIdItemrequisition_id",requisitionItem1.getRequisition_id());
//                    intent.putExtra("reqItemViewGetIdItemreqItemTotal",requisitionItem1.getTotal());
//                    intent.putExtra("eqItemViewGetIdItemid",requisitionItem1.getId());
//                    mcontext.startActivity(intent);
//
//
//                }
//            });
        }
    }
}
