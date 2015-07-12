package io.github.tehstoneman.zombiebrains.entity.ai;

import io.github.tehstoneman.zombiebrains.ZombieBrains;
import io.github.tehstoneman.zombiebrains.waypoints.Waypoint;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIGoToTorch extends EntityAIBase
{
	private final EntityCreature	entity;
	private double					xPosition;
	private double					yPosition;
	private double					zPosition;
	private final double			speed;

	public EntityAIGoToTorch( EntityCreature entity, double speed )
	{
		this.entity = entity;
		this.speed = speed;
		setMutexBits( 1 );
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		if( entity.getRNG().nextInt( 120 ) != 0 )
			return false;
		else
		{
			final int wpIndex = ZombieBrains.waypointManager.getClosestWaypoint( (int)entity.posX, (int)entity.posY, (int)entity.posZ,
					entity.dimension );
			if( wpIndex < 0 )
				return false;
			final Waypoint wp = ZombieBrains.waypointManager.getIndex( wpIndex );
			xPosition = wp.posX;
			yPosition = wp.posY;
			zPosition = wp.posZ;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting()
	{
		return !entity.getNavigator().noPath();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		entity.getNavigator().tryMoveToXYZ( xPosition, yPosition, zPosition, speed );
	}
}
