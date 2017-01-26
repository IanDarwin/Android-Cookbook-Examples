package com.dzinelabs.game;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.hardware.SensorManager;
import android.util.DisplayMetrics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class SimplePool extends BaseGameActivity implements IAccelerometerListener, IOnSceneTouchListener, IOnAreaTouchListener
  {

    private Camera mCamera;
    private Texture mTexture;
    private Texture mBallYellowTexture;
    private Texture mBallRedTexture;
    private Texture mBallBlackTexture;
    private Texture mBallBlueTexture;
    private Texture mBallGreenTexture;
    private Texture mBallOrangeTexture;
    private Texture mBallPinkTexture;
    private Texture mBallPurpleTexture;
    private Texture mBallWhiteTexture;

    private TiledTextureRegion mBallYellowTextureRegion;
    private TiledTextureRegion mBallRedTextureRegion;
    private TiledTextureRegion mBallBlackTextureRegion;
    private TiledTextureRegion mBallBlueTextureRegion;
    private TiledTextureRegion mBallGreenTextureRegion;
    private TiledTextureRegion mBallOrangeTextureRegion;
    private TiledTextureRegion mBallPinkTextureRegion;
    private TiledTextureRegion mBallPurpleTextureRegion;
    private TiledTextureRegion mBallWhiteTextureRegion;

    private Texture mBackgroundTexture;
    private TextureRegion mBackgroundTextureRegion;

    private PhysicsWorld mPhysicsWorld;

    private float mGravityX;
    private float mGravityY;
    private Scene mScene;

    private final int mFaceCount = 0;

    private final int CAMERA_WIDTH = 720;
    private final int CAMERA_HEIGHT = 480;

    @Override
    public Engine onLoadEngine()
      {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
      }

    @Override
    public void onLoadResources()
      {
        this.mTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBallBlackTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBallBlueTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBallGreenTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBallOrangeTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBallPinkTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBallPurpleTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBallYellowTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBallRedTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBallWhiteTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        TextureRegionFactory.setAssetBasePath("gfx/");
        mBallYellowTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallYellowTexture, this, "ball_yellow.png", 0, 0, 1, 1); // 64x32
        mBallRedTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallRedTexture, this, "ball_red.png", 0, 0, 1, 1); // 64x32
        mBallBlackTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallBlackTexture, this, "ball_black.png", 0, 0, 1, 1); // 64x32
        mBallBlueTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallBlueTexture, this, "ball_blue.png", 0, 0, 1, 1); // 64x32
        mBallGreenTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallGreenTexture, this, "ball_green.png", 0, 0, 1, 1); // 64x32
        mBallOrangeTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallOrangeTexture, this, "ball_orange.png", 0, 0, 1, 1); // 64x32
        mBallPinkTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallPinkTexture, this, "ball_pink.png", 0, 0, 1, 1); // 64x32
        mBallPurpleTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallPurpleTexture, this, "ball_purple.png", 0, 0, 1, 1); // 64x32
        mBallWhiteTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mBallWhiteTexture, this, "ball_white.png", 0, 0, 1, 1); // 64x32

        this.mBackgroundTexture = new Texture(512, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.mBackgroundTextureRegion = TextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "table_bkg.png", 0, 0);

        this.enableAccelerometerSensor(this);

        this.mEngine.getTextureManager().loadTextures(this.mBackgroundTexture, mBallYellowTexture, mBallRedTexture, mBallBlackTexture, mBallBlueTexture, mBallGreenTexture, mBallOrangeTexture, mBallPinkTexture, mBallPurpleTexture);
      }

    @Override
    public Scene onLoadScene()
      {
        this.mEngine.registerUpdateHandler(new FPSLogger());

        this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

        this.mScene = new Scene();
        this.mScene.attachChild(new Entity());

        this.mScene.setBackgroundEnabled(false);
        this.mScene.setOnSceneTouchListener(this);
        Sprite backgrund = new Sprite(0, 0, this.mBackgroundTextureRegion);
        backgrund.setWidth(CAMERA_WIDTH);
        backgrund.setHeight(CAMERA_HEIGHT);
        backgrund.setPosition(0, 0);
        this.mScene.getChild(0).attachChild(backgrund);

        final Shape ground = new Rectangle(0, CAMERA_HEIGHT, CAMERA_WIDTH, 0);
        final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 0);
        final Shape left = new Rectangle(0, 0, 0, CAMERA_HEIGHT);
        final Shape right = new Rectangle(CAMERA_WIDTH, 0, 0, CAMERA_HEIGHT);

        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

        this.mScene.attachChild(ground);
        this.mScene.attachChild(roof);
        this.mScene.attachChild(left);
        this.mScene.attachChild(right);

        this.mScene.registerUpdateHandler(this.mPhysicsWorld);
        this.mScene.setOnAreaTouchListener(this);

        return this.mScene;
      }

    @Override
    public void onLoadComplete()
      {
        setupBalls();

      }

    @Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY)
      {
        if (pSceneTouchEvent.isActionDown())
          {
            final AnimatedSprite face = (AnimatedSprite) pTouchArea;
            this.jumpFace(face);
            return true;
          }

        return false;
      }

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent)
      {
        if (this.mPhysicsWorld != null)
          {
            if (pSceneTouchEvent.isActionDown())
              {
                // this.addFace(pSceneTouchEvent.getX(),
                // pSceneTouchEvent.getY());
                return true;
              }
          }
        return false;
      }

    @Override
    public void onAccelerometerChanged(final AccelerometerData pAccelerometerData)
      {
        this.mGravityX = pAccelerometerData.getX();
        this.mGravityY = pAccelerometerData.getY();

        final Vector2 gravity = Vector2Pool.obtain(this.mGravityX, this.mGravityY);
        this.mPhysicsWorld.setGravity(gravity);
        Vector2Pool.recycle(gravity);
      }

    private void setupBalls()
      {
        final AnimatedSprite[] balls = new AnimatedSprite[9];

        final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

        AnimatedSprite redBall = new AnimatedSprite(10, 10, this.mBallRedTextureRegion);
        AnimatedSprite yellowBall = new AnimatedSprite(20, 20, this.mBallYellowTextureRegion);
        AnimatedSprite blueBall = new AnimatedSprite(30, 30, this.mBallBlueTextureRegion);
        AnimatedSprite greenBall = new AnimatedSprite(40, 40, this.mBallGreenTextureRegion);
        AnimatedSprite orangeBall = new AnimatedSprite(50, 50, this.mBallOrangeTextureRegion);
        AnimatedSprite pinkBall = new AnimatedSprite(60, 60, this.mBallPinkTextureRegion);
        AnimatedSprite purpleBall = new AnimatedSprite(70, 70, this.mBallPurpleTextureRegion);
        AnimatedSprite blackBall = new AnimatedSprite(70, 70, this.mBallBlackTextureRegion);
        AnimatedSprite whiteBall = new AnimatedSprite(70, 70, this.mBallWhiteTextureRegion);

        balls[0] = redBall;
        balls[1] = yellowBall;
        balls[2] = blueBall;
        balls[3] = greenBall;
        balls[4] = orangeBall;
        balls[5] = pinkBall;
        balls[6] = purpleBall;
        balls[7] = blackBall;
        balls[8] = whiteBall;

        for (int i = 0; i < 9; i++)
          {
            Body body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, balls[i], BodyType.DynamicBody, objectFixtureDef);
            this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balls[i], body, true, true));

            balls[i].animate(new long[] { 200, 200 }, 0, 1, true);
            balls[i].setUserData(body);
            this.mScene.registerTouchArea(balls[i]);
            this.mScene.attachChild(balls[i]);
          }
      }

    private void jumpFace(final AnimatedSprite face)
      {
        final Body faceBody = (Body) face.getUserData();

        final Vector2 velocity = Vector2Pool.obtain(this.mGravityX * -50, this.mGravityY * -50);
        faceBody.setLinearVelocity(velocity);
        Vector2Pool.recycle(velocity);
      }
  }