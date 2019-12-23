package mysko.pilzhere.christmasgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.christmasgame.Utils;
import mysko.pilzhere.christmasgame.screens.GameScreen;

public class Comet extends Entity implements IEntity, Disposable {
	private Texture texture;
	private Sprite sprite;

	private final Vector3 targetPos = new Vector3();

	public Comet(GameScreen screen, Vector3 position) {
		super(screen, position);

		texture = screen.assMan.get("comet.png", Texture.class);
		sprite = new Sprite(texture);
		rect = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());

		targetPos.set(MathUtils.random(-10, 10), 0, MathUtils.random(-10, 10));

		System.out.println("Comet added!");
	}

	private final Vector3 projPos = new Vector3();
	private final Vector3 screenPos = new Vector3();
//	private final Vector3 direction = new Vector3();

	@Override
	public void tick(float delta) {

		if (position.y > 0.5f) {
			final float destX = targetPos.x - position.x;
			final float destY = targetPos.y - position.y;
			final float destZ = targetPos.z - position.z;

			final float dist = (float) Math.sqrt(destX * destX + destY * destY);

//			final destX = destX / dist; // for slowing down
//			final destY = destY / dist; // for slowing down

			final float travelX = destX * 2.25f * delta;
			final float travelY = destY * 2.25f * delta;
			final float travelZ = destZ * 2.25f * delta;

			final float distTravel = (float) Math.sqrt(travelX * travelX + travelY * travelY);

			if (distTravel > dist) {
				position.x = destX;
				position.y = destY;
				position.z = destZ;
			} else {
				position.x += travelX;
				position.y += travelY;
				position.z += travelZ;
			}
		} else {
			position.y = 0;
			explode();
		}
		
		screenPos.set(Utils.calculateScreenPosition(position.cpy(), projPos.cpy()));

		setSpritePosition();
		setRectanglePosition();

		super.tick(delta);
	}
	
	private void explode() {
		destroy = true;
	}

	@Override
	public void onTouch(float delta) {
		System.out.println("Comet touched!");
	}

	@Override
	public void render2D(SpriteBatch batch, float delta) {
		sprite.draw(batch);
	}

	@Override
	public void destroy() {
		System.out.println("Comet removed!");
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