package com.example.gowthamprasath.scribby;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;


public class MainPage extends AppCompatActivity {
    private static final int CROP_PIC = 3;
    private Button upload_button,capture_button,convert_button;
private ImageView image_displayer;
public static final int PICK_IMAGE = 1;
private static final int REQUEST_IMAGE_CAPTURE = 2;
private Bitmap gray_scale_bitmap=null,selected_image_bitmap=null,inverse_blur_bitmap=null,final_btmap=null;
private ProgressDialog progressDialog;
//private File crop_file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        upload_button=findViewById(R.id.upload);
        image_displayer=findViewById(R.id.image);
        capture_button=findViewById(R.id.capture);
        convert_button=findViewById(R.id.convert);
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadimage();

            }
        });
        capture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureimage();

            }
        });
        convert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertimage();
                //AsyncRunner asyncRunner=new AsyncRunner();
                //asyncRunner.execute(selected_image_bitmap)
                // image_displayer.setImageBitmap(inverse_blur_bitmap);
                // progressDialog.dismiss();
              //progressDialog.dismiss();
                // image_displayer.setImageBitmap(bit1);
               //  imgpre.setImageBitmap(bit1);
                // Toast.makeText(MainPage.this, "half", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri urri = data.getData();
            // String path = urri.getPath();
            // Toast.makeText(this,  getRealPathFromURI(urri), Toast.LENGTH_SHORT).show();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), urri);
                // bitmap=createContrast(bitmap,50);
                selected_image_bitmap=bitmap;//copying the bitmap
                image_displayer.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK && data!=null && data.getExtras()!=null){

      /* Bundle extras = data.getExtras();
           Bitmap imageBitmap = (Bitmap) extras.get("data");
           selected_image_bitmap=imageBitmap;
           image_displayer.setImageBitmap(imageBitmap);*/
            Uri picUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
               // bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
               // image_displayer.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            performCrop(picUri);

        }
        else if (requestCode == CROP_PIC) {
            // get the returned data
            Bundle extras = data.getExtras();
            // get the cropped bitmap
            Bitmap thePic = extras.getParcelable("data");
            selected_image_bitmap=thePic;
            image_displayer.setImageBitmap(thePic);
            /* BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(crop_file.getAbsolutePath(),
                     bitmapOptions);
            image_displayer.setImageBitmap(bitmap);*/
        }
    }
    private void captureimage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      /* crop_file = new File(android.os.Environment.getExternalStorageDirectory(), "makegifimage.jpg");
         picUri = Uri.fromFile(crop_file);
         takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);*/
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void convertimage() {
        progressDialog = ProgressDialog.show(MainPage.this,
                "Wait Pannu Pa",
                "Converting ");
        Thread t=new Thread(){
            @Override
            public void run() {
                gray_scale_bitmap=createContrast(selected_image_bitmap,50);
                inverse_blur_bitmap=createnegative(gray_scale_bitmap);
                inverse_blur_bitmap=Blur.blur(MainPage.this,inverse_blur_bitmap);
                final_btmap=ColorDodgeBlend(inverse_blur_bitmap,gray_scale_bitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image_displayer.setImageBitmap(final_btmap);
                    }
                });
                progressDialog.dismiss();
                }
        };t.start();
    }



    private void uploadimage(){ //opens documents for image uploading
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }
   private void performCrop(Uri picUri) {  // private void performCrop(Uri picUri) {
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 1920);
            cropIntent.putExtra("outputY", 1080);
         cropIntent.putExtra("ScaleUpIfNeeded",true);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    

    /*  public String getRealPathFromURI(Uri contentUri) {
          Cursor cursor = null;
          try {
              String[] proj = { MediaStore.Images.Media.DATA };
              cursor = getApplicationContext().getContentResolver().query(contentUri, proj, null, null, null);
              int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
              cursor.moveToFirst();
              return cursor.getString(column_index);
          } catch (Exception e) {

          } finally {
              if (cursor != null) {
                  cursor.close();
              }
          }
          return null;
      }*/
    public static Bitmap createContrast(Bitmap src, double value) {
// image size

     /*  int width = src.getWidth();
        int height = src.getHeight();
// create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
// color information
       /* int length = src.getWidth()*src.getHeight();
        int[] array = new int[length];
        src.getPixels(array,0,src.getWidth(),0,0,src.getWidth(),src.getHeight());
        int A, R, G, B;
        int pixel;
// get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

// scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.red(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.red(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return bmOut;*/
        int width, height;
        height =src.getHeight();
        width =src.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(src, 0, 0, paint);
        return bmpGrayscale;
    }
   /* public Bitmap invert(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, bitmap.getConfig());
        int length = bitmap.getWidth()*bitmap.getHeight();
        int[] array = new int[length];
        bitmap.getPixels(array,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        for (int i=0;i<length;i++){
// If the bitmap is in ARGB_8888 format
            if (array[i] == 0xff000000){
                array[i] = 0xffffffff;


        }
            if (array[i] == 0xffffffff) {
                array[i] = 0xff000000;
            }
    }
        bmOut.setPixels(array,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
      //  imgpre.setImageBitmap(bmOut);
       // Toast.makeText(this, "awesome", Toast.LENGTH_SHORT).show();
        return bmOut;
    }*/
    public Bitmap createnegative(Bitmap src){
        // image size

      /* int width = src.getWidth();
        int height = src.getHeight();
// create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
// color information
       /* int length = src.getWidth()*src.getHeight();
        int[] array = new int[length];
        src.getPixels(array,0,src.getWidth(),0,0,src.getWidth(),src.getHeight());
        int A, R, G, B;
        int pixel;
// get contrast value
// scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);


                G = Color.red(pixel);


                B = Color.red(pixel);


                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, 255-R,255- G, 255-B));
            }
        }
        imgpre.setImageBitmap(bmOut);

return bmOut;*/
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        -1,  0,  0,  0, 255,
                        0, -1,  0,  0, 255,
                        0,  0, -1,  0, 255,
                        0,  0,  0,  1,   0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);

        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();

        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(src, 0, 0, paint);

        return bitmap;
    }
    private int colordodge(int in1, int in2) {
        float image = (float)in2;
        float mask = (float)in1;
        return ((int) ((image == 255) ? image:Math.min(255, (((long)mask << 8 ) / (255 - image)))));

    }

    /**
     * Blends 2 bitmaps to one and adds the color dodge blend mode to it.
     */
    public Bitmap ColorDodgeBlend(Bitmap source, Bitmap layer) {
        Bitmap base = source.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap blend = layer.copy(Bitmap.Config.ARGB_8888, false);

        IntBuffer buffBase = IntBuffer.allocate(base.getWidth() * base.getHeight());
        base.copyPixelsToBuffer(buffBase);
        buffBase.rewind();

        IntBuffer buffBlend = IntBuffer.allocate(blend.getWidth() * blend.getHeight());
        blend.copyPixelsToBuffer(buffBlend);
        buffBlend.rewind();

        IntBuffer buffOut = IntBuffer.allocate(base.getWidth() * base.getHeight());
        buffOut.rewind();

        while (buffOut.position() < buffOut.limit()) {

            int filterInt = buffBlend.get();
            int srcInt = buffBase.get();

            int redValueFilter = Color.red(filterInt);
            int greenValueFilter = Color.green(filterInt);
            int blueValueFilter = Color.blue(filterInt);

            int redValueSrc = Color.red(srcInt);
            int greenValueSrc = Color.green(srcInt);
            int blueValueSrc = Color.blue(srcInt);

            int redValueFinal = colordodge(redValueFilter, redValueSrc);
            int greenValueFinal = colordodge(greenValueFilter, greenValueSrc);
            int blueValueFinal = colordodge(blueValueFilter, blueValueSrc);


            int pixel = Color.argb(255, redValueFinal, greenValueFinal, blueValueFinal);


            buffOut.put(pixel);
        }

        buffOut.rewind();

        base.copyPixelsFromBuffer(buffOut);
        blend.recycle();

        return base;
    }



}
/*class AsyncRunner extends AsyncTask<Bitmap,String,String>{
    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}*/
