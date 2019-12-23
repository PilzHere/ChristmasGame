package mysko.pilzhere.christmasgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.christmasgame.screens.GameScreen;

public class Island extends Entity implements Disposable {
	private ModelBuilder mdlBuilder;

	private Texture texture;
	private Model mdl;
	private ModelInstance mdlInstance;
	public BoundingBox bb;

	public Island(GameScreen screen, Vector3 position) {
		super(screen, position);

		texture = screen.assMan.get("island256.png", Texture.class);

		mdlBuilder = new ModelBuilder();
		mdl = mdlBuilder.createBox(100, 0, 100, new Material(TextureAttribute.createDiffuse(texture)),
				Usage.Position | Usage.TextureCoordinates);
		mdlInstance = new ModelInstance(mdl);

		bb = new BoundingBox();
		mdlInstance.calculateBoundingBox(bb).mul(mdlInstance.transform);
	}

	@Override
	public void tick(float delta) {
		bb.mul(mdlInstance.transform);

//		System.out.println("bbWidth: " + bb.getWidth() + " | bbHeight: " + bb.getHeight() + " | bbDepth: " + bb.getDepth());
	}

	@Override
	public void render3D(ModelBatch batch, float delta) {
		batch.render(mdlInstance);
	}

	@Override
	public void dispose() {
		mdl.dispose();
	}

}
