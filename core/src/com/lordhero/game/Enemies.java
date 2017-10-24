package com.lordhero.game;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemies {
	private Hashtable<String, List<Enemy>> _enemies;
	
	public Enemies()
	{
		_enemies = new Hashtable<String, List<Enemy>>();
	}
	
	public void update() {
		String site = createEnemyOnSite();
		
		if (site != null) {
			addEnemy(site);
		}
	}
	
	public void render(SpriteBatch spriteBatch, String site) {		
		if (_enemies.containsKey(site)) {
			List<Enemy> enemies = _enemies.get(site);
			
			for (Enemy enemy : enemies) {
				enemy.render(spriteBatch);
			}
			
			boolean removedEnemy = false;
			
			do {
				removedEnemy = false;
				for (Enemy enemy : enemies) {
					if (enemy.isTerminated()) {
						enemies.remove(enemy);
						removedEnemy = true;
						break;
					}
				}				
			} while (removedEnemy);
		}
	}
	
	private void addEnemy(String site) {
		List<Enemy> enemies;
		
		if (!_enemies.containsKey(site)) {
			enemies = new LinkedList<Enemy>();
			_enemies.put(site,  enemies);
		}
		else {
			enemies = _enemies.get(site);
		}				
		
		enemies.add(new Enemy());
	}

	private String createEnemyOnSite() {
		String site = null;
		
		if (Math.random() < 0.01) {
			site = "baseMap";		
		}
		
		return site; 
	}
}
