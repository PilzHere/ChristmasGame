package mysko.pilzhere.christmasgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.christmasgame.Utils;
import mysko.pilzhere.christmasgame.screens.GameScreen;

public class Candycane extends Entity implements IEntity, Disposable {
	private Texture texture;
	private Sprite sprite;

	public Candycane(GameScreen screen, Vector3 position) {
		super(screen, position);

		texture = screen.assMan.get("candyCane.png", Texture.class);
		sprite = new Sprite(texture);
		rect = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
		
		System.out.println("Candy added!");
	}

	private final Vector3 projPos = new Vector3();
	private final Vector3 screenPos = new Vector3();
	
	@Override
	public void tick(float delta) {
		screenPos.set(Utils.calculateScreenPosition(position.cpy(), projPos.cpy()));
		
		setSpritePosition();
		setRectanglePosition();		
		
		super.tick(delta);
	}

	@Override
	public void onTouch(float delta) {
		System.out.println("Candy touched!");
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

	@Override
	public void setSpritePosition() {
		sprite.setX(screenPos.x - sprite.getWidth() / 2);
		sprite.setY(screenPos.y - sprite.getHeight() / 2);
	}

	@Override
	public void setRectanglePosition() {
		rect.x = sprite.getX();
		rect.y = sprite.getY();
	}
}