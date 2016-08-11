package com.ammy.waterreflection;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ammy.waterreflection.haoran.ImageFilter.Image;
import com.ammy.waterreflection.haoran.ImageFilter.WaterReflection;

public class MainActivity extends AppCompatActivity {

    Bitmap bit;
    private Bitmap blurBitmap;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bit = BitmapFactory.decodeResource(getResources(), R.drawable.img);
        image = (ImageView) findViewById(R.id.image);

            processImageTask processImage = new processImageTask(new WaterReflection(30));
            processImage.execute();
    }

    public class processImageTask extends AsyncTask<Void, Void, Bitmap> {
        private WaterReflection filter;
        private ProgressDialog pd;

        public processImageTask(WaterReflection imageFilter) {
            this.filter = imageFilter;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.pd = ProgressDialog.show(MainActivity.this, "", "Please wait.. ", true, true);
            this.pd.setCancelable(false);
        }

        public Bitmap doInBackground(Void... params) {
            Image img = null;
            try {
                Image img2 = new Image(bit);
                try {
                    if (this.filter != null) {
                        img = this.filter.process(img2);
                        img.copyPixelsFromBuffer();
                    } else {
                        img = img2;
                    }
                    Bitmap image = img.getImage();
                    if (img == null || !img.image.isRecycled()) {
                        return image;
                    }
                    img.image.recycle();
                    img.image = null;
                    System.gc();
                    return image;
                } catch (Exception e) {
                    img = img2;
                    if (img != null) {
                        try {
                            if (img.destImage.isRecycled()) {
                                img.destImage.recycle();
                                img.destImage = null;
                                System.gc();
                            }
                        } catch (Throwable th2) {
                            img.image.recycle();
                            img.image = null;
                            System.gc();
                        }
                    }
                    img.image.recycle();
                    img.image = null;
                    System.gc();
                    return null;
                } catch (Throwable th3) {
                    img = img2;
                    if (img != null && img.image.isRecycled()) {
                        img.image.recycle();
                        img.image = null;
                        System.gc();
                    }
                }
            } catch (Exception e2) {
                if (img != null) {
                    if (img.destImage.isRecycled()) {
                        img.destImage.recycle();
                        img.destImage = null;
                        System.gc();
                    }
                }
                if (img != null && img.image.isRecycled()) {
                    img.image.recycle();
                    img.image = null;
                    System.gc();
                }
                return null;
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            if (this.pd.isShowing()) {
                this.pd.cancel();
            }
            if (result != null) {
                super.onPostExecute(result);
                MainActivity.this.blurBitmap = result.copy(result.getConfig(), true);
               Bitmap resultImage = MainActivity.this.combineImages(bit,blurBitmap);

                image.setImageBitmap(resultImage);
            }
        }
    }


    public Bitmap combineImages(Bitmap orignalbmp, Bitmap blurbmp) {
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        blurbmp = Bitmap.createBitmap(blurbmp, 0, 0, blurbmp.getWidth(), blurbmp.getHeight(), matrix, true);
        Bitmap cs = Bitmap.createBitmap(orignalbmp.getWidth(), orignalbmp.getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(orignalbmp, 0.0f, 0.0f, null);
        comboImage.drawBitmap(blurbmp, 0.0f, (float) orignalbmp.getHeight(), null);
        return cs;
    }
}
