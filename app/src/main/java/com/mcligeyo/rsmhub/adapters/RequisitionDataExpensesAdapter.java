package com.mcligeyo.rsmhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mcligeyo.rsmhub.R;
import com.mcligeyo.rsmhub.RequisitionDataEditActivity;
import com.mcligeyo.rsmhub.model.RequisitionData;

import java.util.List;
import java.util.Random;

public class RequisitionDataExpensesAdapter extends RecyclerView.Adapter<RequisitionDataExpensesAdapter.ViewHolder> {
    private Context mcontext;
    private List<RequisitionData> requisitionDatas;

    //reqItemDataEditImageView.setVisibility(View.GONE); //make imageview in req items list gone, when item is submited.
    public RequisitionDataExpensesAdapter(Context mcontext, List<RequisitionData> requisitionData) {
        this.mcontext = mcontext;
        this.requisitionDatas = requisitionData;
    }


    @NonNull
    @Override
    public RequisitionDataExpensesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_requisition_list_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RequisitionDataExpensesAdapter.ViewHolder holder, final int position) {

        //get randomcolor to be se in profile textview
        int[] colorArrs = mcontext.getResources().getIntArray(R.array.color_array_1);
        int ranDomColor = createRandomColor(colorArrs);

        //Capitalize allwordss in title and get first character of title string
        RequisitionData requisitionData1 = requisitionDatas.get(position);
        String requisitionDataName = String.valueOf(requisitionData1.getName().toUpperCase().charAt(0));
        // holder.pendingExpensesProfileTextView.setText(requisitionData1.getName().toUpperCase());
        holder.pendingExpensesReqDetsTitleTextView.setText(requisitionData1.getName().toUpperCase());
        holder.pendingExpensesReqSideDateTextView.setText(requisitionData1.getDate());
        holder.pendingExpensesReqIdReqDetailsSideTextView.setText(String.valueOf(requisitionData1.getId()));
//        holder.pendingExpensesStatusReqDetailsSideTextView.setText(String.valueOf(requisitionData1.getStatus()));

        //
        holder.pendingExpensesProfileTextView.setBackgroundColor(ranDomColor);
        holder.pendingExpensesProfileTextView.setText(requisitionDataName);

        String status = String.valueOf(requisitionData1.getStatus());
        switch (status) {
            case "1":  //1 is submitted
                //            holder.pendingExpensesStatusReqDetailsSideTextView.setText("PENDING");// gave warning String literal in 'setText' cannot be translated. Use Android resources instead. Still worked despite warning. both methods below are andri0d resources
//            holder.pendingExpensesStatusReqDetailsSideTextView.setText(R.string.requisition_data_expenses_adapter_pending);
                holder.pendingExpensesStatusReqDetailsSideTextView.setText(mcontext.getResources().getString(R.string.requisition_data_expenses_adapter_pending)); //method 2

                //holder.requisitionDataDeleteImageView.setVisibility(View.GONE);
                break;
            case "2":  //2 is approved
                holder.pendingExpensesStatusReqDetailsSideTextView.setText(R.string.requisition_data_expenses_adapter_approved);
                //holder.requisitionDataDeleteImageView.setVisibility(View.GONE);
                break;
            case "3":  //3 is declined
                holder.pendingExpensesStatusReqDetailsSideTextView.setText(R.string.requisition_data_expenses_adapter_declined);
                // holder.requisitionDataDeleteImageView.setVisibility(View.VISIBLE);
                break;
        }
//        holder.requisitionDataDeleteImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeItem(position);
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

    public void loadNewData(List<RequisitionData> requisitionDataList) {
        this.requisitionDatas = requisitionDataList;
        notifyDataSetChanged();
    }

    //when a single item removed
    public void removeItem(int position) {
        this.requisitionDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.requisitionDatas.size());
    }

    @Override
    public int getItemCount() {
        return this.requisitionDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pendingExpensesProfileTextView, pendingExpensesReqDetsTitleTextView, pendingExpensesReqSideDateTextView, pendingExpensesReqIdReqDetailsSideTextView, pendingExpensesStatusReqDetailsSideTextView;

        //ImageView requisitionDataDeleteImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pendingExpensesProfileTextView = itemView.findViewById(R.id.pendingRequisitionListViewReqDetsProfileTextView);
            pendingExpensesReqDetsTitleTextView = itemView.findViewById(R.id.pendingRequisitionListViewReqDetsTitleTextView);
            pendingExpensesReqSideDateTextView = itemView.findViewById(R.id.pendingRequisitionListViewReqSideDateTextView);
            pendingExpensesReqIdReqDetailsSideTextView = itemView.findViewById(R.id.pendingRequisitionListViewReqIdReqDetailsSideTextView);
            pendingExpensesStatusReqDetailsSideTextView = itemView.findViewById(R.id.pendingRequisitionListViewStatusReqDetailsSideTextView);

            //requisitionDataDeleteImageView = itemView.findViewById(R.id.list_view_pending_requisition_img_delete);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    RequisitionData requisitionData = requisitionDatas.get(position);
                    Intent intent = new Intent(mcontext, RequisitionDataEditActivity.class);
                    intent.putExtra("requisitonName", requisitionData.getName());
                    intent.putExtra("requisitionDate", requisitionData.getDate());
                    intent.putExtra("requisitionId", requisitionData.getId());
                    intent.putExtra("requisitionStatus", requisitionData.getStatus());
                    mcontext.startActivity(intent);
                }
            });


        }
    }


}
