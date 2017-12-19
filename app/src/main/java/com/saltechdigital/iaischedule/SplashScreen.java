package com.saltechdigital.iaischedule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SplashScreen extends AppCompatActivity {

    GLSurfaceView view;
    MyRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GLSurfaceView(this);
        renderer = new MyRenderer();
        view.setRenderer(renderer);
        setContentView(view);
        //setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        view.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        view.onResume();
        super.onResume();
    }

    public class MyRenderer implements GLSurfaceView.Renderer {

        private Cube mCube;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            mCube = new Cube(gl);
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); //GL10.GL_FASTEST);
            gl.glClearColor(1, 1, 1, 1);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glEnable(GL10.GL_CULL_FACE);
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glDisable(GL10.GL_DITHER);
            gl.glShadeModel(GL10.GL_SMOOTH);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

            float ratio = (float) width / height;
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

        }

        @Override
        public void onDrawFrame(GL10 gl) {

            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            mCube.onDraw(gl);

        }

    }

    public class Cube {

        private FloatBuffer mTextureBuffer;
        private IntBuffer mVertexBuffer;
        private IntBuffer mColorBuffer;
        private ByteBuffer mIndexBuffer;
        private Bitmap mTexture;
        private int[] mTextIDs;
        private float mAngle;

        Cube(GL10 gl) {

            int one = 0x10000;

            int vertices[] = {
                    -one, -one, -one,
                    one, -one, -one,
                    one, one, -one,
                    -one, one, -one,
                    -one, -one, one,
                    one, -one, one,
                    one, one, one,
                    -one, one, one,
            };

            int colors[] = {
                    0, 0, 0, one,
                    one, 0, 0, one,
                    one, one, 0, one,
                    0, one, 0, one,
                    0, 0, one, one,
                    one, 0, one, one,
                    one, one, one, one,
                    0, one, one, one,
            };

            byte indices[] = {
                    0, 4, 5, 0, 5, 1,
                    1, 5, 6, 1, 6, 2,
                    2, 6, 7, 2, 7, 3,
                    3, 7, 4, 3, 4, 0,
                    4, 7, 6, 4, 6, 5,
                    3, 0, 1, 3, 1, 2,
            };

            float texCoords[] = {

                    // FRONT
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    // BACK
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    // LEFT
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    // RIGHT
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    // TOP
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    // BOTTOM
                    1.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 0.0f,
                    0.0f, 1.0f

            };

            ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            mVertexBuffer = vbb.asIntBuffer();
            mVertexBuffer.put(vertices);
            mVertexBuffer.position(0);

            ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
            cbb.order(ByteOrder.nativeOrder());
            mColorBuffer = cbb.asIntBuffer();
            mColorBuffer.put(colors);
            mColorBuffer.position(0);

            mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
            mIndexBuffer.put(indices);
            mIndexBuffer.position(0);

            ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
            tbb.order(ByteOrder.nativeOrder());
            mTextureBuffer = tbb.asFloatBuffer();
            mTextureBuffer.put(texCoords);
            mTextureBuffer.position(0);

            mTexture = ((BitmapDrawable) SplashScreen.this.getResources().getDrawable(R.drawable.ic_splashlogo)).getBitmap();

            mTextIDs = new int[1];
            gl.glGenTextures(mTextIDs.length, mTextIDs, 0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextIDs[0]);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mTexture, 0);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);


        }

        public void onDraw(GL10 gl) {

            mAngle += 1.2f;

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0, -3.0f);
            gl.glRotatef(mAngle, 0, 1, 0);
            gl.glRotatef(mAngle * 0.25f, 1, 0, 0);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            gl.glActiveTexture(GL10.GL_TEXTURE0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextIDs[0]);

            gl.glFrontFace(GL10.GL_CW);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
            gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
            gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
            gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);


        }

    }
}
