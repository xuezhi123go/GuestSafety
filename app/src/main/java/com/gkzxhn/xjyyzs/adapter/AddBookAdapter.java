package com.gkzxhn.xjyyzs.adapter;

/**
 * Author: Huang ZN
 * Date: 2016/11/9
 * Email:943852572@qq.com
 * Description:
 */

public class AddBookAdapter
//        extends RecyclerView.Adapter<AddBookAdapter.MyViewHolder>
{

//    private static final String TAG = "AddBookAdapter";
//    private List<FamilyBean> list;
//    private Context mContext;
//
//    public AddBookAdapter(Context context, List<FamilyBean> list){
//        this.mContext = context;
//        this.list = new ArrayList<>();
//        this.list.addAll(list);
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.i(TAG, "onCreateViewHolder()");
//        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext)
//                .inflate(R.layout.add_item, parent, false));
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        Log.i(TAG, list.get(position).toString());
//        holder.tv_name.setText(list.get(position).getName());
//        holder.tv_id_card_number.setText("身份证：" + list.get(position).getUuid());
//        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                list.remove(position);
//                AddBookAdapter.this.notifyItemRemoved(position);
//                AppBus.getInstance().post(new ItemRemovedEvent(position, list.get(position)));
//            }
//        });
//    }
//
//    /**
//     * 添加item
//     * @param familyBean
//     */
//    public void insert(FamilyBean familyBean){
//        Log.i(TAG, "insert book info");
//        list.add(familyBean);
//        notifyItemInserted(getItemCount());
//    }
//
//    @Override
//    public int getItemCount() {
//        Log.i(TAG, "getItemCount() == " + list.size());
//        return list.size();
//    }
//
//    public void removeAllItem() {
//        list.clear();
//        notifyDataSetChanged();
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder{
//
//        @BindView(R.id.iv_delete)
//        ImageView iv_delete;
//        @BindView(R.id.tv_name)
//        TextView tv_name;
//        @BindView(R.id.tv_id_card_number)
//        TextView tv_id_card_number;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            Log.i("MyViewHolder", "MyViewHolder");
//            ButterKnife.bind(this, itemView);
//        }
//    }
}
