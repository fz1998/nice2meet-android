package com.threebody.conference.ui.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.threebody.conference.R;


public class FragmentUtil {
	
	
	public static void moveToRightFragment(ActionBarActivity activity, int containerViewId, Fragment fragment){
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.translate_in_to_left, R.anim.translate_out_to_left);
		transaction.addToBackStack(null);
		transaction.replace(containerViewId, fragment);
		transaction.commit();
	}
	
	public static void moveToLeftFragment(ActionBarActivity activity,int containerViewId, Fragment fragment){
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.translate_in_to_right, R.anim.translate_out_to_right);
		transaction.addToBackStack(null);
		transaction.replace(containerViewId, fragment);
		transaction.commit();
	}
}
