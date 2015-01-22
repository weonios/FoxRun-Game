package javagame;


import org.newdawn.slick.Image;

public class EnemyTree {

	Image enemyTree;
	
	int treeX;
	int treeY;
	
	
	
	public EnemyTree(int treeX, int treeY, Image enemyTree) {
		
		this.treeX = treeX; 
		this.treeY = treeY;
		this.enemyTree = enemyTree;		
		
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + treeX;
		result = prime * result + treeY;
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnemyTree other = (EnemyTree) obj;
		if (treeX != other.treeX)
			return false;
		if (treeY != other.treeY)
			return false;
		return true;
	}
	
	
	
}
