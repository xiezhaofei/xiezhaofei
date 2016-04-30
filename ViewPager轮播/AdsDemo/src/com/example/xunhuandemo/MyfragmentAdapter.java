package com.example.xunhuandemo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyfragmentAdapter<T> extends FragmentPagerAdapter {

	List<Fragment> fragments;
	List<T> datas;
	/**
	 * Fragmentʵ��ֻ��Zero��One��Two������
	 */
	public MyfragmentAdapter(FragmentManager fm,List<T> mDatas) {
		super(fm);
		this.datas = mDatas;
		fragments = new ArrayList<Fragment>();
		//��������һ��indexΪ0��fragment����Դdata�����һ����
		//indexΪlast��fragment����Դdata�ŵ�һ��
		/*fragments.add(new FragmentTwo());
		fragments.add(new FragmentZero());
		fragments.add(new FragmentOne());
		fragments.add(new FragmentTwo());
		fragments.add(new FragmentZero());*/
		for(int i = 0;i<datas.size()+2;i++){
			if(i==0){
				MyFragment<T> f0 = new MyFragment<T>();
				Bundle bundle = new Bundle();
				bundle.putSerializable("data", (Serializable) datas.get(datas.size()-1));
				f0.setArguments(bundle);
				f0.setData(datas.get(datas.size()-1));
				fragments.add(f0);
			}else if(i==datas.size()+1){
				MyFragment<T> fl = new MyFragment<T>();
				Bundle bundle = new Bundle();
				bundle.putSerializable("data", (Serializable) datas.get(0));
				fl.setArguments(bundle);
				fl.setData(datas.get(0));
				fragments.add(fl);
			}else{
				MyFragment<T> f = new MyFragment<T>();
				Bundle bundle = new Bundle();
				bundle.putSerializable("data", (Serializable) datas.get(i-1));
				f.setArguments(bundle);
				f.setData(datas.get(i-1));
				fragments.add(f);
			}
		}
		
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}