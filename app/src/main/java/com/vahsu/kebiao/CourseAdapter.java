package com.vahsu.kebiao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vahsu.kebiao.DBUtil.CourseLNRTL;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private List<CourseLNRTL> mCourseLNRTLList;
    private int parentWidth;

    public interface OnItemClickListener{
        void onClick(View v,int postion);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseView;

        ViewHolder(View view) {
            super(view);
            courseView = (TextView)view.findViewById(R.id.course);
            courseView.setWidth(parentWidth/7);

        }
    }

    CourseAdapter(List<CourseLNRTL> courseLNRTLList) {

        mCourseLNRTLList = courseLNRTLList;

    }

    // 创建新视图（由布局管理器调用）
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建新视图
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        parentWidth = parent.getMeasuredWidth();
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // 替换视图的内容（由布局管理器调用）
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        CourseLNRTL courseEntity = mCourseLNRTLList.get(position);
        if(null != courseEntity.getCourseName()&&!"".equals(courseEntity.getCourseName())){
            String text = courseEntity.getCourseName();
            if (null != courseEntity.getClassroom()&&!"".equals(courseEntity.getClassroom())){
                text = text + "\n" + courseEntity.getClassroom();
            }
            holder.courseView.setText(text);
            holder.courseView.setBackgroundColor(0xFF3CAFFF);
            holder.courseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v,position);
                }
            });
        } else{
            holder.courseView.setText(null);
            holder.courseView.setBackground(null);
            holder.courseView.setOnClickListener(null);
        }

    }

    // 返回数据集的大小（由布局管理器调用）
    @Override
    public int getItemCount() {
        return mCourseLNRTLList.size();
    }
}