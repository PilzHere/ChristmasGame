package mysko.pilzhere.christmasgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.christmasgame.screens.GameScreen;

public class Tree extends Entity implements Disposable {
	private Texture texture;
	private Sprite sprite;

	public Tree(GameScreen screen, Vector3 position) {
		super(screen, position);

		texture = new Texture("tree01.png");
		sprite = new Sprite(texture);

		System.out.println("Tree added!");
	}

	private final Vector3 projPos = new Vector3();
	private final Vector3 screenPos = new Vector3();
	
	@Override
	public void tick(float delta) {
		projPos.set(screen.viewport.project(position.cpy()));
		screenPos.set(projPos.x, projPos.y, 0);
		
		sprite.setX(screenPos.x - sprite.getWidth() / 2);
		sprite.setY(screenPos.y - sprite.getHeight() / 2);
		
		super.tick(delta);
	}

	@Override
	public void render2D(SpriteBatch batch, float delta) {
		sprite.draw(batch);
	}

	@Override
	public void destroy() {

	}

	@Override
	public void dispose() {

	}
}
