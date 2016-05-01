package com.example.xunhuandemo;


import java.lang.reflect.Field;

import com.example.adsdemo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment<T> extends Fragment{

	T data;
	
	int imgResource;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// 第一个参数是这个Fragment将要显示的界面布局,第二个参数是这个Fragment所属的Activity,第三个参数是决定此fragment是否附属于Activity
    	View view=inflater.inflate(R.layout.fragment_rootview, container, false);
    	TextView txtView = (TextView) view.findViewById(R.id.img);
    	txtView.setBackgroundResource(imgResource);
    	System.out.println("FragmentOne onCreateView");
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	if(getArguments()!=null){
    		data = (T) getArguments().getSerializable("data");
        	try {
    			Field file = data.getClass().getDeclaredField("imgResource");
    			file.setAccessible(true);
    			imgResource = file.getInt(data);
    		} catch (NoSuchFieldException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}catch (IllegalArgumentException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IllegalAccessException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	
        System.out.println("FragmentOne onCreate");
    }
    
	public void onResume(){
        super.onResume();
        System.out.println("FragmentOne onResume");
    }
    
    @Override
    public void onPause(){
        super.onPause();
        System.out.println("FragmentOne onPause");
    }
    
    @Override
    public void onStop(){
        super.onStop();
        System.out.println("FragmentOne onStop");
    }

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
    
}
