package com.example.gowthamprasath.scribby;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

public class Blur
{
    private static final float BITMAP_SCALE=0.4f;
    private static final float BLUR_RADIUS=3f;
    public static Bitmap blur(Context context, Bitmap image) {
        Bitmap photo = image.copy(Bitmap.Config.ARGB_8888, true);

        try {
            final RenderScript rs = RenderScript.create( context );
            final Allocation input = Allocation.createFromBitmap(rs, photo, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius( BLUR_RADIUS ); /* e.g.    3.f */
            script.setInput( input );
            script.forEach( output );
            output.copyTo( photo );
        }catch (Exception e){
            e.printStackTrace();
        }
        return photo;
    }
    /* CC:ssaurel
     int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;*/
}
