package mysko.pilzhere.christmasgame.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import mysko.pilzhere.christmasgame.entities.Player;
import mysko.pilzhere.christmasgame.entities.enemies.Yeti;

public class YetiAI {
	private final Yeti yeti;
	private AIStates state;
	public Player player;

	public YetiAI(Yeti yeti) {
		this.yeti = yeti;
		state = AIStates.IDLE;
	}

	private float distFromPlayer;
	private final float minDistFromPlayer = 3f;
	private final float detectionDistFromPlayer = 15f;
	private final Vector3 direction = new Vector3();
	
	private boolean waitTimerSet;
	private long actualTimeToWait;
	private long timeToWait = 1;
	private long currentTime;	

	public void tick(float delta) {
		if (player != null) {
			distFromPlayer = Vector3.dst(yeti.position.x, yeti.position.y, yeti.position.z, player.position.x,
					player.position.y, player.position.z);
		}

		switch (state) {
		case IDLE:
			waitTimerSet = false;
			
			yeti.movementSpeed = yeti.walkSpeed;

			if (player != null) {
				if (distFromPlayer < detectionDistFromPlayer)
					state = AIStates.CHASING_PLAYER;
			} else {
				state = AIStates.BORED;
			}
			break;
		case BORED:
			yeti.movementSpeed = yeti.boredSpeed;

			if (player != null) {
				if (distFromPlayer < detectionDistFromPlayer) {
					state = AIStates.CHASING_PLAYER;
				} else {

					direction.x = (yeti.position.x + MathUtils.random(-5f, 5f) - yeti.position.x);
					direction.y = 0;
					direction.z = (yeti.position.z + MathUtils.random(-5f, 5f) - yeti.position.z);

					direction.nor().scl(yeti.movementSpeed * delta);

					yeti.position.add(direction.cpy());
				}
			} else {
				direction.x = (yeti.position.x + MathUtils.random(-5f, 5f) - yeti.position.x);
				direction.y = 0;
				direction.z = (yeti.position.z + MathUtils.random(-5f, 5f) - yeti.position.z);

				direction.nor().scl(yeti.movementSpeed * delta);

				yeti.position.add(direction.cpy());
			}
			break;
		case CHASING_PLAYER:
			yeti.movementSpeed = yeti.runSpeed;

			if (player != null) {
				direction.x = (player.position.x - yeti.position.x);
				direction.y = 0;
				direction.z = (player.position.z - yeti.position.z);

				direction.nor().scl(yeti.movementSpeed * delta);

				if (distFromPlayer > minDistFromPlayer)
					yeti.position.add(direction.cpy());
				
				if (distFromPlayer < minDistFromPlayer)
					state = AIStates.ATTACK;

				if (distFromPlayer > detectionDistFromPlayer)
					state = AIStates.IDLE;
			}
			break;
		case ATTACK:
			yeti.attack = true;
			state = AIStates.WAIT;
			break;
		case WAIT:
			if (!waitTimerSet) {
				actualTimeToWait = System.currentTimeMillis() + (timeToWait * 1500L); // 1000
				waitTimerSet = true;
			} else {
				currentTime = System.currentTimeMillis();
				if (currentTime >= actualTimeToWait) {
					state = AIStates.IDLE;
				}
			}
				
				
			break;
		case DEAD:

			break;
		}
	}
}
