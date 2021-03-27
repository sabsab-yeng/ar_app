package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    //3d model credit : google.poly.com
//    private String Model_URL = "https://sketchfab.com/3d-models/glb-sofa-7d9e1c9ebf3a4a2297f777fc24ca7a0b.glb"
    private String Model_URL = "https://modelviewer.dev/shared-assets/models/Astronaut.glb"
;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        setUpModel();
        setUpPlane();
    }

    //ສະແດງຂໍ້ມູນມາຈາກ URL ຫຼື ດຶງຂໍ້ມູນມາສະແດງ
    private void setUpModel() {
        ModelRenderable.builder()
                        .setSource(this,
                                 RenderableSource.builder().setSource(
                                this,
                                Uri.parse(Model_URL),
                                RenderableSource.SourceType.GLB)
                                         .setScale(0.75f)
                                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                 .build())

                .setRegistryId(Model_URL)



                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> {
                    Log.i("Model","cant load");
                    Toast.makeText(MainActivity.this,"ບໍສາມາດດາວໂຫຼດໄດ້", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

//    ສ້າງຕົວຮູບປັ້ນ
    private void setUpPlane(){
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            createModel(anchorNode);
        }));
    }

//    ປ້ບຮູບຕາມຄວາມຕ້ອງການ
    private void createModel(AnchorNode anchorNode){
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();
    }
}
