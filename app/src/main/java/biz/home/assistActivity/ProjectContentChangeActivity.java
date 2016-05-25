package biz.home.assistActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.api.ImageTools;
import biz.home.fragment.MainProjectFragment;

import static biz.home.fragment.MainProjectFragment.CONTENT;

/**
 * Created by admin on 2015/9/4.
 * 用于修改发布资源时候的资源内容
 */
public class ProjectContentChangeActivity extends Activity {
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP = 2;
    private static final int CROP_PICTURE = 3;
    private static final String TAG ="ProjectContentChangeActivity" ;
    public static int num=0;
    SpannableString mSpan1 ;
    public static Map<String,String> pics=new HashMap<String,String>();

    private static final int SCALE = 4;//照片缩小比例
    private ImageView iv_image = null;
//    private EditText content;
    private EditText content;   //可编辑文本域
    private Button insertPic,changeSure;       //插入图片和确认修改按钮
    public ProjectContentChangeActivity() {     //空构造函数

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_infomation_confirm);
        findById();
        setOnClick();
    }
    public void findById(){
        content= (EditText) findViewById(R.id.project_message_content);
        insertPic= (Button) findViewById(R.id.project_insert_pic);
        changeSure=(Button)findViewById(R.id.project_change_sure);
        content.requestFocus();
        Intent intent=getIntent();
        //从intent中取出资源内容，并显示在该界面上
        if(intent.hasExtra(CONTENT)){
            content.setText(intent.getStringExtra(MainProjectFragment.CONTENT));
            displayPicOnText(content,pics);
        }
    }
    public void setOnClick(){
        insertPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicturePicker(ProjectContentChangeActivity.this, false);
            }
        });
        changeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeakToitActivity.pics=pics;
                Intent data = new Intent();
                data.putExtra(MainProjectFragment.CONTENT, content.getText().toString());               //将修改过后的数据回发
                Bundle bundle=new Bundle();

                setResult(MainProjectFragment.CHANGE_CONTENT, data);    //设置返回码
                System.out.println("content.getText().toString():" + content.getText().toString());

                ProjectContentChangeActivity.this.finish();
            }
        });
    }
    //将图片的键值对存储在Map  pics中
    public void saveInMap(String key,String value){
        pics.put(key, value);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    //将处理过的图片显示在界面上，并保存到本地
//                    iv_image.setImageBitmap(newBitmap);
                    String path=Environment.getExternalStorageDirectory().getAbsolutePath();
                    String pathName=String.valueOf(System.currentTimeMillis());
                    ImageTools.savePhotoToSDCard(newBitmap, path, pathName);
                    displayBitmapOnText(newBitmap, path + "//" + pathName + ".png");
                    break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    System.out.println("originalUri" + originalUri);
                    try {
                        String img_path = getRealPathFromURI(originalUri);
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();

                            path=Environment.getExternalStorageDirectory().getAbsolutePath();
                            pathName=String.valueOf(System.currentTimeMillis());
                            ImageTools.savePhotoToSDCard(smallBitmap, path, pathName);
                            displayBitmapOnText(smallBitmap, path + "//" + pathName + ".png");
//                            iv_image.setImageBitmap(smallBitmap);
//						content.append(Html.fromHtml("<img src='" + smallBitmap + "'/>", imageGetter, null));
                            //将originalUri转化为路径img_path
//                            System.out.println("img_path:" + img_path);
//                            displayBitmapOnText(photo,img_path);
//                            displayBitmapOnText(smallBitmap);
//                            System.out.println("" + content.getText().toString());
//                            Log.i("EditText", content.getText().toString());
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void showPicturePicker(Context context,boolean isCrop){
        final boolean crop = isCrop;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
            //类型码
            int REQUEST_CODE;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE:
                        Uri imageUri = null;
                        String fileName = null;
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (crop) {
                            REQUEST_CODE = CROP;
                            //删除上一次截图的临时文件
                            SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE);
                            ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));

                            //保存本次截图临时文件名字
                            fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("tempName", fileName);
                            editor.commit();
                        } else {
                            REQUEST_CODE = TAKE_PICTURE;
                            fileName = "image.jpg";
                        }
                        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, REQUEST_CODE);
                        break;

                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        if (crop) {
                            REQUEST_CODE = CROP;
                        } else {
                            REQUEST_CODE = CHOOSE_PICTURE;
                        }
                        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(openAlbumIntent, REQUEST_CODE);
                        break;

                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }
    private  Bitmap getBitmapFromFilename(String fileName){
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        return bitmap;
    }
    private void displayPicOnText(EditText editText2,Map<String,String> map){
        //正则表达式为  ^\[pic\d{1}\]$
//        String regex="^\\[pic\\d{1}\\]$";
        SpannableString mSpan1;
        String text=editText2.getText().toString();
        for (String key : map.keySet()) {
//            System.out.println("key= "+ key + " and value= " + map.get(key));
            String value=map.get(key);
//            Log.i(TAG, "displayPicOnText key:"+key+";value"+value);
            System.out.println("" + "displayPicOnText key:"+key+";value"+value);
            if(editText2.getText().toString().contains(key)){
                int startIndex=text.indexOf(key);
                Bitmap thumbnailBitmap = BitmapFactory.decodeFile(value);
                if(thumbnailBitmap == null)
                    return;
                mSpan1 = new SpannableString(key);
                int length=key.length();
                mSpan1.setSpan(new ImageSpan(thumbnailBitmap), mSpan1.length() - length, mSpan1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                if(editText2 != null) {
                    Editable et = editText2.getText();
                    et.insert(startIndex, mSpan1);
                    et.replace(startIndex+length,startIndex+length*2,"");
                    editText2.setText(et);
                    editText2.setSelection(startIndex + mSpan1.length());
                }
                editText2.setLineSpacing(10f, 1f);
                //将图片的键值对信息存储在
//                saveInMap(mSpan1.toString(), value);
            }
        }
    }
    private void displayPicOnText(int start,String fileName){
        Bitmap thumbnailBitmap = BitmapFactory.decodeFile(fileName);
        if(thumbnailBitmap == null)
            return;
//        int start = content.getSelectionStart();

        mSpan1 = new SpannableString("-pic"+num+"-");
        //将图片的键值对信息存储在
        saveInMap("-pic"+num+"-",fileName);
        mSpan1.setSpan(new ImageSpan(thumbnailBitmap), mSpan1.length() - (num<10?6:7), mSpan1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        num++;
        if(content != null) {
            Editable et = content.getText();
            et.insert(start, mSpan1);
            content.setText(et);
            content.setSelection(start + mSpan1.length());
        }
        content.setLineSpacing(10f, 1f);

    }
    private void displayBitmapOnText(Bitmap thumbnailBitmap) {

        if(thumbnailBitmap == null)

            return;
//        String s=bitmaptoString(thumbnailBitmap,1);
////		System.out.print("bitmap转换成的字符串：" +s );
//        Log.i("bitmap转换成的字符串：", ""+s);
//        System.out.print("" + s);
//        System.out.println("" + s.length());
//        System.out.println("" + s.getBytes().length);
//        thumbnailBitmap=stringtoBitmap(s);
        int start = content.getSelectionStart();
        mSpan1 = new SpannableString("-pic"+num+"-");
        mSpan1.setSpan(new ImageSpan(thumbnailBitmap), mSpan1.length() - 6, mSpan1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        num++;
//        mSpan1.toString();
        if(content != null) {
            Editable et = content.getText();
//            et.insert(start, mSpan1);
            et.insert(start, mSpan1);
            content.setText(et);
            content.setSelection(start + mSpan1.length());
        }
        content.setLineSpacing(10f, 1f);
    }
    private void displayBitmapOnText(Bitmap thumbnailBitmap,String fileName) {

        if(thumbnailBitmap == null)
            return;
//        String s=bitmaptoString(thumbnailBitmap,1);
////		System.out.print("bitmap转换成的字符串：" +s );
//        Log.i("bitmap转换成的字符串：", ""+s);
//        System.out.print("" + s);
//        System.out.println("" + s.length());
//        System.out.println("" + s.getBytes().length);
//        thumbnailBitmap=stringtoBitmap(s);
        int start = content.getSelectionStart();
        mSpan1 = new SpannableString("-pic"+num+"-");
        //将图片的键值对信息存储在
        saveInMap("-pic"+num+"-",fileName);
        mSpan1.setSpan(new ImageSpan(thumbnailBitmap), mSpan1.length() - (num<10?6:7), mSpan1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        num++;
//        mSpan1.toString();
        if(content != null) {
            Editable et = content.getText();
//            et.insert(start, mSpan1);
            et.insert(start, mSpan1);
            content.setText(et);
            content.setSelection(start + mSpan1.length());
        }
        content.setLineSpacing(10f, 1f);
        //将图片的键值对信息存储在
//        saveInMap(mSpan1.toString(),fileName);
    }

    /**
     　　* 将bitmap转换成base64字符串
     　　*
     　　* @param bitmap
     　　* @return base64 字符串
     　　*/
    public String bitmaptoString(Bitmap bitmap, int bitmapQuality) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }
    /**
     　　* 将base64转换成bitmap图片
     　　*
     　　* @param string base64字符串
     　　* @return bitmap
     　　*/
    public Bitmap stringtoBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
