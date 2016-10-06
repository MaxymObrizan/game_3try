/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author Max
 */
import engine.graph.Renderer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Scene;
import engine.SceneLight;
import engine.Timer;
import engine.Utils;
import engine.items.SkyBox;
import engine.Window;
import engine.graph.Camera;
import engine.graph.Material;
import engine.graph.Mesh;
import engine.graph.Texture;
import engine.graph.anim.AnimGameItem;
import engine.loaders.OBJLoader;
import engine.graph.lights.DirectionalLight;
import engine.graph.particles.FlowParticleEmitter;
import engine.graph.particles.Particle;
import engine.graph.weather.Fog;
import engine.items.GameItem;
import engine.items.Terrain;
import engine.loaders.md5.MD5AnimModel;
import engine.loaders.md5.MD5Loader;
import engine.loaders.md5.MD5Model;

public class DummyGame implements IGameLogic {
 
    private Timer timer = new Timer();
    private static final float GRAVITY = -50f;
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private Hud hud;

    private float lightAngle;
    
    private GameItem cubeGameItem;

    private static final float CAMERA_POS_STEP = 0.05f;

    private Terrain terrain;
    
     private AnimGameItem monster;
     
     
    private FlowParticleEmitter particleEmitter;
    
    private float upwardsSpeed = 0;
    
    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        float skyBoxScale = 50.0f;        
        float terrainScale = 100;
        //int terrainSize = 3;
        int terrainSize = 1;
        float minY = -0.1f;
        float maxY = 0.1f;
        int textInc = 1;
        terrain = new Terrain(terrainSize, terrainScale, minY, maxY, "/resources/textures/heightmap_none.png", "/resources/textures/terrain.png", textInc);
        //terrain = new Terrain(terrainSize, terrainScale, minY, maxY, "/textures/heightmap_test.png", "/textures/terrain.png", textInc);
        float reflectance = 1f;
        Mesh cubeMesh = OBJLoader.loadMesh("/resources/models/cube.obj");
        Material cubeMaterial = new Material(new Vector3f(0, 1, 0), reflectance);
        cubeMesh.setMaterial(cubeMaterial);
        cubeGameItem = new GameItem(cubeMesh);
        cubeGameItem.setPosition(0, -1, 0);
        cubeGameItem.setScale(0.5f);

        Mesh quadMesh = OBJLoader.loadMesh("/resources/models/plane.obj");
        Material quadMaterial = new Material(new Vector3f(0.0f, 0.0f, 1.0f), reflectance);
        quadMesh.setMaterial(quadMaterial);
        GameItem quadGameItem = new GameItem(quadMesh);
        quadGameItem.setPosition(0, -2, 0);
        quadGameItem.setScale(2.5f);

        // Setup  GameItems
        MD5Model md5Meshodel = MD5Model.parse("/resources/models/boblamp.md5mesh");
        MD5AnimModel md5AnimModel = MD5AnimModel.parse("/resources/models/boblamp.md5anim");
        //MD5Model md5Meshodel = MD5Model.parse("/models/boblamp.md5mesh");
        //MD5AnimModel md5AnimModel = MD5AnimModel.parse("/models/boblamp.md5anim");
        
        monster = MD5Loader.process(md5Meshodel, md5AnimModel, new Vector3f(1, 1, 1));
        monster.setPosition(0, 2, 0);
        monster.setScale(0.05f);
        monster.setRotation(90, 0, 0);
        
        scene.setGameItems(new GameItem[]{cubeGameItem, quadGameItem, monster});
        
        scene.setGameItems(terrain.getGameItems());

        scene.setFog(new Fog(false, new Vector3f(0f, 0f, 0f), 0.9f));
        
        Vector3f particleSpeed = new Vector3f(0, 1, 0);
        particleSpeed.mul(1f);
        long ttl = 500;
        int maxParticles = 200;
        long creationPeriodMillis = 50;
        float range = 0.5f;
        float scale = 0.001f;
        Mesh partMesh = OBJLoader.loadMesh("/resources/models/particle.obj");
        Texture texture = new Texture("/resources/textures/particle_anim.png", 4, 4);
        Material partMaterial = new Material(texture, reflectance);
        partMesh.setMaterial(partMaterial);
        Particle  particle = new Particle(partMesh, particleSpeed, ttl, 100);
        particle.setScale(scale);
        particleEmitter = new FlowParticleEmitter(particle, maxParticles, creationPeriodMillis);
        particleEmitter.setActive(true);
        particleEmitter.setPositionRndRange(range);
        particleEmitter.setSpeedRndRange(range);
        particleEmitter.setAnimRange(10);
        this.scene.setParticleEmitters(new FlowParticleEmitter[] {particleEmitter});
        
        // Setup  SkyBox
        SkyBox skyBox = new SkyBox("/resources/models/skybox.obj", "/resources/textures/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();

        // Create HUD
        hud = new Hud("DEMO");

        camera.getPosition().x = 0.0f;
        camera.getPosition().y = 2f;
        camera.getPosition().z = 2.5f;
        camera.getRotation().x = 0;
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(1, 1, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE) ) {
            monster.nextFrame(); 
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

            // Update HUD compass
            hud.rotateCompass(camera.getRotation().y);
        }

        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);        
      
        // Check if there has been a collision. If true, set the y position to
        // the maximum height
        float height = terrain.getHeight(camera.getPosition());
        if ( camera.getPosition().y <= height )  {
//            upwardsSpeed = 0;
            camera.setPosition(prevPos.x, prevPos.y, prevPos.z);
        }

        // Update directional light direction, intensity and colour
        SceneLight sceneLight = scene.getSceneLight();
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        lightAngle += 0.5f;// day time
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
            sceneLight.getSkyBoxLight().set(0.3f, 0.3f, 0.3f);
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            sceneLight.getSkyBoxLight().set(factor, factor, factor);
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            sceneLight.getSkyBoxLight().set(1.0f, 1.0f, 1.0f);
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
        
        StringBuilder builder = new StringBuilder();
        builder.append("Position: " );
        builder.append(String.valueOf(camera.getPosition().x) + "; ");
        builder.append(String.valueOf(camera.getPosition().y) + "; ");
        builder.append(String.valueOf(camera.getPosition().z));
        
        hud.setStatusText(builder.toString());
        
        particleEmitter.update((long)(interval * 1000));
    }

    @Override
    public void render(Window window) {
        if (hud != null) {
            hud.updateSize(window);
        }
        renderer.render(window, camera, scene, hud);
    }

   @Override
    public void cleanup() {
        renderer.cleanup();
        scene.cleanup();
        if (hud != null) {
            hud.cleanup();
        }
    }

}