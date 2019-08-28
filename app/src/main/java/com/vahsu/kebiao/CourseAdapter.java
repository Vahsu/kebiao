package com.vahsu.kebiao;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vahsu.kebiao.DBUtil.CourseLNRTL;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private List<CourseLNRTL> mCourseLNRTLList;
    private List<Integer> colorList;
    private Map<String, Integer> colorMap = new HashMap<>();
    private int count = -1;
    //重置可用颜色，相邻位置颜色不重复
    public void resetColor(){
        count = -1;
        colorMap.clear();
    }

    public interface OnItemClickListener{
        void onClick(View v,int position);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseView;

        ViewHolder(View view, int parentWidth) {
            super(view);
            courseView = (TextView)view.findViewById(R.id.course);
            courseView.setWidth(parentWidth/7);

        }
    }

    CourseAdapter(List<CourseLNRTL> courseLNRTLList) {

        mCourseLNRTLList = courseLNRTLList;

    }

    // 创建新视图（由布局管理器调用）
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (colorList == null){
            colorList = new LinkedList<>();
            int[] colors = parent.getContext().getResources().getIntArray(R.array.course_background_color);
            for (int colorTemp : colors) {
                colorList.add(colorTemp);
            }
        }
        // 创建新视图
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        int parentWidth = parent.getMeasuredWidth();
        return new ViewHolder(view, parentWidth);
    }

    // 替换视图的内容（由布局管理器调用）
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        CourseLNRTL courseEntity = mCourseLNRTLList.get(position);
        if(null != courseEntity.getCourseName() && !"".equals(courseEntity.getCourseName())){
            String text = courseEntity.getCourseName();
            if (null != courseEntity.getClassroom() && !"".equals(courseEntity.getClassroom())){
                text = text + "\n" + courseEntity.getClassroom();
            }
            if (count < 1){
                count = count == -1 ? colorList.size() : colorList.size() / 2;
            }
            Integer color = colorMap.get(text);
            //如果颜色映射包含text
            if (color == null){
                Random r = new Random();
                int random = r.nextInt(count);
                count--;
                color = colorList.get(random);
                colorList.remove(random);
                colorList.add(color);
                colorMap.put(text, color);
            }

            Drawable shape = holder.courseView.getContext().getDrawable(R.drawable.course_background);
            shape.setColorFilter(color, PorterDuff.Mode.SRC);
            holder.courseView.setText(text);
            holder.courseView.setBackground(shape);
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