package mysko.pilzhere.christmasgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import mysko.pilzhere.christmasgame.Utils;
import mysko.pilzhere.christmasgame.screens.GameScreen;

public class Seawave extends Entity implements IEntity {
	private Texture texture;
	private Sprite sprite;

	public Seawave(GameScreen screen, Vector3 position) {
		super(screen, position);

		texture = screen.assMan.get("seaWave.png", Texture.class);
		sprite = new Sprite(texture);
//		rect = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());

		System.out.println("Seawave added!");
	}

	private final Vector3 projPos = new Vector3();
	private final Vector3 screenPos = new Vector3();

	@Override
	public void tick(float delta) {
		screenPos.set(Utils.calculateScreenPosition(position.cpy(), projPos.cpy()));
		
		setSpriteSize(sprite);		
		setSpritePosition(sprite);		
//		setRectanglePosition(rect);
//		setRectangleSize(rect);

		super.tick(delta);
	}

	@Override
	public void onTouch(float delta) {
		System.out.println("Seawave touched!");
	}

	@Override
	public void render2D(SpriteBatch batch, float delta) {
		sprite.draw(batch);
	}

	@Override
	public void destroy() {

	}

	@Override
	public void setSpritePosition(Sprite sprite) {
		sprite.setX(screenPos.x - sprite.getWidth() / 2);
		sprite.setY(screenPos.y - sprite.getHeight() / 2);
	}

	@Override
	public void setRectanglePosition(Rectangle rect) {		
		rect.setX(sprite.getX());
		rect.setY(sprite.getY());
	}

	@Override
	public void setSpriteScale(Sprite sprite) {
		Utils.setSpriteScale(sprite);
	}

	@Override
	public void setSpriteSize(Sprite sprite) {
		sprite.setSize(sprite.getTexture().getWidth() / 4 * screen.game.windowScale, sprite.getTexture().getWidth() / 4 * screen.game.getWindowScale());
	}

	@Override
	public void setRectangleSize(Rectangle rect) {
		rect.setWidth(sprite.getWidth() * sprite.getScaleX());
		rect.setHeight(sprite.getHeight() * sprite.getScaleY());
	}
}