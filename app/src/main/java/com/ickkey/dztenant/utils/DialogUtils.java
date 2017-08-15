package com.ickkey.dztenant.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;

import com.ickkey.dztenant.R;

public class DialogUtils {
	static AlertDialog dialog;
	private static ProgressDialog progressDialog;
	static AlertDialog customProgressDialog;
	private static AlertDialog infoDialog;

	public static AlertDialog showDialog(@NonNull Activity activity, int dialogLayoutId, boolean cancelable, final CustomizeAction customizeAction) {
		dialog = new AlertDialog.Builder(activity).create();
		View view = View.inflate(activity, dialogLayoutId, null);
		if (customizeAction != null) {
			customizeAction.setCustomizeAction(dialog, view);
		}
		//http://blog.csdn.net/lzt623459815/article/details/8479745
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
		if(!activity.isFinishing())
		dialog.show();

		dialog.getWindow().setContentView(view);
		dialog.setCanceledOnTouchOutside(cancelable);
		return dialog;
	}
	/**
	 * 在showdialog后去做一些事情的回调
	 *
	 * @param activity
	 * @param dialogLayoutId
	 * @param cancelable
	 * @param customizeAction
	 * @return
	 */
	public static AlertDialog showDialog(@NonNull Activity activity, int dialogLayoutId, boolean cancelable, final CustomizeAction2 customizeAction) {
		dialog = new AlertDialog.Builder(activity).create();
		View view = View.inflate(activity, dialogLayoutId, null);
		if (customizeAction != null) {
			customizeAction.setCustomizeAction(dialog, view);
		}
		//http://blog.csdn.net/lzt623459815/article/details/8479745
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
		dialog.show();
		customizeAction.setCustomizeActionAfterShow(dialog);
		dialog.getWindow().setContentView(view);
		dialog.setCanceledOnTouchOutside(cancelable);
		return dialog;
	}
	public static void showProgressDialog(Context ctx){
		//必须每次建立新对象
		closeProgressDialog();
		customProgressDialog = new AlertDialog.Builder(ctx).create();
		View view = View.inflate(ctx, R.layout.progress_dialog, null);
		//http://blog.csdn.net/lzt623459815/article/details/8479745
		customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
		customProgressDialog.setCanceledOnTouchOutside(true);
		if(ctx instanceof Activity){
			Activity activity= (Activity) ctx;
			if(activity.isFinishing()){
					return;
			}

		}
		customProgressDialog.show();
		customProgressDialog.getWindow().setContentView(view);

	}
	/*public static void showProgressDialog(Context ctx){
		    //必须每次建立新对象
		    closeProgressDialog();
			progressDialog = new ProgressDialog(ctx);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.setMessage("加载中...");
		   if(ctx instanceof Activity){
			if(((Activity)ctx).isFinishing()) {
				LogUtil.info(DialogUtils.class, "showProgressDialog--Activity-isFinishing");
				return;
			}

		}
		progressDialog.show();

	}*/
	public static void showProgressDialog(Context ctx,String msg){
		    closeProgressDialog();
			progressDialog = new ProgressDialog(ctx);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.setMessage(msg);
			progressDialog.show();
}
	public static void showProgressDialog(Context ctx,String msg,boolean cancelable){
		closeProgressDialog();
		 progressDialog = new ProgressDialog(ctx);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(cancelable); 
		progressDialog.setMessage(msg);  

if(progressDialog!=null&&!progressDialog.isShowing())
progressDialog.show();

}
	
	public static void showInfoDialog(Context ctx,String msg,DialogInterface.OnClickListener positive,DialogInterface.OnClickListener negative){
		
		  AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		   builder.setTitle("提示")    //标题
           .setCancelable(true).setMessage(msg).setPositiveButton("确定", positive).setNegativeButton("取消", negative) ;   //不响应back按钮
		   infoDialog=builder.create();


		   if(infoDialog!=null&&!infoDialog.isShowing())
			   infoDialog.show();
		   
		   
}
	public static void showInfoDialog(Context ctx,String msg,DialogInterface.OnClickListener positive,DialogInterface.OnClickListener negative,String positiveStr,String negativeStr){
		
		  AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		   builder.setTitle("提示")    //标题
         .setCancelable(true).setMessage(msg).setPositiveButton(positiveStr, positive).setNegativeButton(negativeStr, negative) ;   //不响应back按钮
		   infoDialog=builder.create();
		   if(infoDialog!=null&&!infoDialog.isShowing())
			   infoDialog.show();
		   
		   
}
	public static void closeInfoDialog(){
		if(infoDialog!=null&&infoDialog.isShowing())
			infoDialog.dismiss();
		    infoDialog=null;
		
	}
	public static void closeProgressDialog(){
		try {
			if(customProgressDialog!=null&&customProgressDialog.isShowing()) {
				customProgressDialog.dismiss();
			}
			customProgressDialog=null;
		}catch (Exception e){

		}


	}


	public interface CustomizeAction {
		void setCustomizeAction(AlertDialog dialog, View view);
	}

	public interface CustomizeAction2 extends CustomizeAction {
		void setCustomizeActionAfterShow(AlertDialog dialog);

	}
}
