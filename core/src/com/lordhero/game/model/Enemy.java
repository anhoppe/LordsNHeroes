package com.lordhero.game.model;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;

public class Enemy extends CreatureBase implements INonPlayer {
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
	
	private int _hitPoints = 20;
	
	private int _maxHitPoints = 20;

	private int _xp;
	
	public Enemy(float xPos, float yPos) {
		_xPos = xPos;
		_yPos = yPos;

		_xEndPos = getRandomStartPosition();
		_yEndPos = getRandomStartPosition();
		
		_xp = 5;

		restore();
	}
	
	public Enemy(Element enemyNode) {
		super(enemyNode.getChildByName("CreatureBase"));

		try {
			_xEndPos = enemyNode.getIntAttribute("XEndPos");
			_yEndPos = enemyNode.getIntAttribute("YEndPos");
			_mode = Mode.valueOf(enemyNode.getAttribute("Mode"));
			_hitPoints = enemyNode.getIntAttribute("HitPoints");		
			_xp = enemyNode.getIntAttribute("Xp");
		} catch (GdxRuntimeException e) {
			
		}
		
		restore();
	}

	@Override
	public void update(IPlayer player, IGameMode gameMode) {
		if (_mode == Mode.Wander) {
			Vector2 dir = new Vector2((float)(_xEndPos - _xPos), (float)(_yEndPos - _yPos));
			
			dir.nor();
			_viewDirectionDeg = dir.angle();
			dir.scl(TranslationSpeed);
			_xPos += dir.x;
			_yPos += dir.y;
			
			if (spotsPlayer(player) && gameMode.is(GameMode.Play)) {
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
			if (Math.abs(dir.angle() - _viewDirectionDeg) > 45f) {
				if (dir.angle() > _viewDirectionDeg) {
					_viewDirectionDeg += RotationSpeedDeg;
				}
				else {
					_viewDirectionDeg -= RotationSpeedDeg;
				}
			}
			
			// Move if enemy is already looking into player direction
			else if (Math.abs(dir.angle() - _viewDirectionDeg) < 180f && dist > FightingDist) {
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
				
				rotMatrix.setToRotation(_viewDirectionDeg);
				vec.mul(rotMatrix);
				vec.scl(_weapon.getRange());

				player.hit((int)(_xPos + vec.x), (int)(_yPos + vec.y), _weapon);
			}
		}
						
		_sprite.setCenter((float)_xPos, (float)_yPos);
		_sprite.setRotation(_viewDirectionDeg);
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

	public void terminate() {
		_mode = Mode.Terminate;		
	}

	public boolean hit(int hit) {
		boolean killed = false;
		
		_hitPoints -= hit;
		
		if (_hitPoints < 0) {
			killed = true;
			terminate();
		}		
		
		return killed;
	}
	
	public void addHitPoints(int hitPoints) {
		_hitPoints = Math.min(_hitPoints + hitPoints, _maxHitPoints);
	}
	
	@Override
	public void write(XmlWriter writer) throws IOException {
		writer.element("Enemy").
		attribute("XEndPos", _xEndPos).
		attribute("YEndPos", _yEndPos).
		attribute("Mode", _mode).
		attribute("HitPoints", _hitPoints).
		attribute("Xp", _xp);

		super.write(writer);
		writer.pop();
	}

	@Override
	public void dispose() {
	}

	public int getXp() {
		return _xp;
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
}
