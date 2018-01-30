package com.lordhero.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.lordhero.game.IPlayer;
import com.lordhero.game.model.items.Weapon;

public class Enemy extends EntityBase {
	enum Mode {
		None,
		Wander,
		Hunt,
		Terminate
	}
	
	private static final double MinTargetDistance = 5.0;

	private static final float TranslationSpeed = 2.5f;
	private static final float RotationSpeedDeg = 2f;
	private static final float MeeleAttackDist = 50f;
	private static final float FightingDist = 30f;
	
	private static final float SpotRange = 250f;

	private static Texture _monsterTileSet = new Texture(Gdx.files.internal("data/tileset/Monsters.png"));
	
	private int _xEndPos;
	private int _yEndPos;
	
	private Mode _mode = Mode.Wander;
	
	private Weapon _weapon = Weapon.Create(0);
	
	public Enemy() {		 
		_xPos = getRandomStartPosition();
		_yPos = getRandomStartPosition();
		 
		_xEndPos = getRandomStartPosition();
		_yEndPos = getRandomStartPosition();

		restore();
	}
	
	@Override
	public void update(IPlayer player) {
		if (_mode == Mode.Wander) {
			Vector2 dir = new Vector2((float)(_xEndPos - _xPos), (float)(_yEndPos - _yPos));
			
			dir.nor();
			_viewDirection = dir.angle();
			dir.scl(TranslationSpeed);
			_xPos += dir.x;
			_yPos += dir.y;
			
			if (spotsPlayer(player)) {
				_mode = Mode.Hunt;
			}
			else if ((Math.abs(_xPos - _xEndPos) * Math.abs(_xPos - _xEndPos) + Math.abs(_yPos - _yEndPos) * Math.abs(_yPos - _yEndPos)) < MinTargetDistance) {
				_mode = Mode.Terminate;
			}			
		}
		else if (_mode == Mode.Hunt) {
			Vector2 dir = new Vector2((float)(player.getX() - _xPos), (float)(player.getY() - _yPos));
						
			float dist = dir.len();
			dir.nor();
			
			// Rotate into player direction if angle is too large
			if (Math.abs(dir.angle() - _viewDirection) > 45f) {
				if (dir.angle() > _viewDirection) {
					_viewDirection += RotationSpeedDeg;
				}
				else {
					_viewDirection -= RotationSpeedDeg;
				}
			}
			
			// Move if enemy is already looking into player direction
			else if (Math.abs(dir.angle() - _viewDirection) < 180f && dist > FightingDist) {
				dir.scl(TranslationSpeed);
				_xPos += dir.x;
				_yPos += dir.y;				
			}
		
			// hit player if already in range and the distance is ok
			if (dist <= MeeleAttackDist && !_weapon.attacks()) {
				_weapon.startAttack();
			}
			
			// check if attack is already going on, then evaluate the damage
			else if (_weapon.attacks()) {
				Vector2 vec = new Vector2(0, 1);
				Matrix3 rotMatrix = new Matrix3();
				rotMatrix.idt();
				
				rotMatrix.setToRotation(_viewDirection);
				vec.mul(rotMatrix);
				vec.scl(_weapon.getRange());

				player.hit((int)(_xPos + vec.x), (int)(_yPos + vec.y), _weapon);
			}
		}
						
		_sprite.setCenter((float)_xPos, (float)_yPos);
		_sprite.setRotation(_viewDirection);
	}


	@Override
	public TextureRegion getWeaponAnimationFrame() {
		TextureRegion animation = null;
		
		if (_weapon.attacks()) {
			animation = _weapon.getWeaponAnimation();
		}
		
		return animation;
	}

	public boolean isTerminated() {
		return _mode == Mode.Terminate;		
	}

	// Can be overwritten by derived class in order to restore non-serializable members
	@Override
	public void restore() {
		TextureRegion region = new TextureRegion(_monsterTileSet, 0, 0, 32, 32);
		 
		_sprite = new Sprite(region);
		 
		_sprite.setCenter((float)_xPos, (float)_yPos);
	}

	private int getRandomStartPosition() {
		return (int) (Math.random() * 128 * 32);
	}
	
	private boolean spotsPlayer(IPlayer player) {
		Vector2 vec = new Vector2(player.getX() - _xPos, player.getY() - _yPos);
		
		if (vec.len() < SpotRange) {
			return true;
		}
		
		return false;
	}

	public void terminate() {
		_mode = Mode.Terminate;		
	}
}
