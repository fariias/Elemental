package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

/**
 * test
 *
 * @author normenhansen
 */

public class Main extends SimpleApplication implements ActionListener {
   
     
    private FilterPostProcessor fpp;
    private WaterFilter water;
    private Vector3f lightDir = new Vector3f(-4.9f, -1.3f, 5.9f); // same as light source
    private float initialWaterHeight = 0.2f; // choose a value for your scene
    private boolean uw = false;
    private float time = 0.0f;
    
    private CharacterControl player;
    private RigidBodyControl landscape;
    private BulletAppState bulletAppState;
    private Vector3f camDir = new Vector3f();
   
    private Vector3f camLeft = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    private Spatial cenario;
    
    private Geometry arma; 
    private Spatial rifle;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1f));
        flyCam.setMoveSpeed(200); //velocidade da camera
        Box b = new Box(1, 1, 1);
        arma = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        arma.setMaterial(mat);
        rifle = assetManager.loadModel("Models/mao6.j3o");
        rifle.setLocalScale(0.1f);
        
        
        
        fpp = new FilterPostProcessor(assetManager);
        water = new WaterFilter(rootNode, lightDir);
        water.setWaterHeight(initialWaterHeight);
        fpp.addFilter(water);
        viewPort.addProcessor(fpp);
        
        
         Node cenario = (Node) assetManager.loadModel("Scenes/island.j3o");
         cenario.setName("ilha");
         cenario.setLocalTranslation(0,-48,0);
         rootNode.attachChild(cenario);
         cam.setLocation(new Vector3f(0,0,0)); //posicao da camera
         
         
         Spatial sky = SkyFactory.createSky(assetManager, "Textures/FullskiesSunset0068.dds", false);
         sky.setLocalScale(350);
         cenario.attachChild(sky);
        
         
        comandos();


        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);

        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(50);
        player.setFallSpeed(200);
        player.setGravity(100);
        
        player.getPhysicsLocation(new Vector3f(0, 0, 0));
        player.setPhysicsLocation(new Vector3f(0,49,0));
        





        /*assetManager.registerLocator("town.zip", ZipLocator.class);
        sceneModel = assetManager.loadModel("main.scene");
        CollisionShape sceneshape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        landscape = new RigidBodyControl(sceneshape, 0);
        sceneModel.setLocalTranslation(-50, -50, -50);
        sceneModel.addControl(landscape);
        rootNode.attachChild(sceneModel);
*/
        CollisionShape sceneshape = CollisionShapeFactory.createMeshShape((Node) cenario);
        landscape = new RigidBodyControl(sceneshape, 0);
        cenario.addControl(landscape);
        //setando a fisica
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);



        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);



        AmbientLight luz = new AmbientLight();
        luz.setColor(ColorRGBA.White.mult(5f));
        rootNode.addLight(luz);
        
        
        //Criando o céu
        
        // Fim do céu
    }
    //Definindo comandos do teclado

    public void comandos() {
        inputManager.addMapping("acao", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("frente", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("tras", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("esquerda", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("direita", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("pular", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("correr", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("fire",new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "frente");
        inputManager.addListener(this, "tras");
        inputManager.addListener(this, "esquerda");
        inputManager.addListener(this, "direita");
        inputManager.addListener(this, "pular");
        inputManager.addListener(this, "fire");
    }
    //criando acao dos botoes

    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("acao")) {
            //acao do botao E
        }
        if (binding.equals("frente")) {
            up = isPressed;
        }
        if (binding.equals("tras")) {
            down = isPressed;
        }
        if (binding.equals("esquerda")) {
            left = isPressed;
        }
        if (binding.equals("direita")) {
            right = isPressed;
        }
        if (binding.equals("pular")) {
            player.jump();
        }
        if (binding.equals("fire")) {
            player.jump();
        }
        if (binding.equals("correr")) {
            player.setFallSpeed(150);
        }

    }

    @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);

        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());

        }
        if (up) {
            walkDirection.addLocal(camDir);

        }
        if (down) {
            walkDirection.addLocal(camDir.negate());

        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
        
        
         //codigo da posição das mãos
   Vector3f vectorDifference = new Vector3f(cam.getLocation().subtract(arma.getWorldTranslation())); 
   arma.setLocalTranslation(vectorDifference.addLocal(arma.getLocalTranslation())); 
   rifle.setLocalTranslation(vectorDifference.addLocal(arma.getLocalTranslation()));
   Quaternion worldDiff = new Quaternion(cam.getRotation().mult(arma.getWorldRotation().inverse()));
   arma.setLocalRotation(worldDiff.multLocal(arma.getLocalRotation()));
   rifle.setLocalRotation(worldDiff.multLocal(arma.getLocalRotation()));
   
   
   arma.move(cam.getDirection().mult(2));
   
   arma.move(cam.getUp().mult(-0.8f));
   arma.move(cam.getLeft().mult(-1f));
   arma.rotate(0.1f, FastMath.PI, 0);
   
   
    rifle.move(cam.getDirection().mult(4));
    rifle.move(cam.getUp().mult(-0.8f));
    rifle.move(cam.getLeft().mult(-1f));
    rifle.setLocalScale(0.5f);
    rifle.rotate(0.0f, FastMath.PI, 0);
    rifle.rotate(0.0f, FastMath.PI, 1);
    rootNode.attachChild(rifle);
    rifle.setLocalTranslation(arma.getLocalTranslation());
    rifle.setLocalRotation(arma.getLocalRotation());
        
         
        
        
        
    }
    
    
    
    
    
    
   
   
   //rootNode.attachChild(arma); rootNode.attachChild(rifle); rifle.setLocalTranslation(arma.getLocalTranslation()); rifle.setLocalRotation(arma.getLocalRotation());

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
