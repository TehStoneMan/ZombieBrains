package io.github.tehstoneman.zombiebrains.waypoints;

import io.github.tehstoneman.zombiebrains.ModInfo;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Waypoint
{
	public int						posX, posY, posZ;
	public int						dimensionId;

	public ArrayList< Waypoint >	neighbors;
	public ArrayList< Float >		distances;

	public int						id;

	public float					cdist;
	public Waypoint					cpre;

	public Waypoint( int x, int y, int z, int dimId )
	{
		neighbors = new ArrayList< Waypoint >();
		distances = new ArrayList< Float >();
		dimensionId = dimId;
		posX = x;
		posY = y;
		posZ = z;
	}

	public void addNeighbor( Waypoint w, float dist )
	{
		if( w.id == id )
			return;
		for( final Waypoint p : neighbors )
			if( p.id == w.id )
				return;

		for( int i = 0; i < neighbors.size(); i++ )
			if( dist < distances.get( i ) )
			{
				neighbors.add( i, w );
				distances.add( i, dist );
				return;
			}

		neighbors.add( w );
		distances.add( dist );
	}

	public void removeNeighbor( Waypoint w )
	{
		final int i = neighbors.indexOf( w );
		if( i != -1 )
		{
			distances.remove( i );
			neighbors.remove( i );
		}
	}

    public double distanceTo( int x, int y, int z )
    {
        int d0 = x - this.posX;
        int d1 = y - this.posY;
        int d2 = z - this.posZ;
        return (double)MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }


	/**
	 * Draw a marker at the location of this waypoint
	 * 
	 * @param mc
	 * @param x = viewer x location
	 * @param y = viewer y location
	 * @param z = viewer z location
	 */
	@SideOnly(Side.CLIENT)
	public void draw( Minecraft mc, double x, double y, double z )
	{
		mc.renderEngine.bindTexture( new ResourceLocation( ModInfo.MODID, "textures/waypoint.png" ) );
		final Tessellator tessellator = Tessellator.instance;

		GL11.glEnable( GL11.GL_BLEND );
		OpenGlHelper.glBlendFunc( 770, 771, 1, 0 );
		GL11.glColor4f( 1.0F, 0.0F, 0.0F, 0.4F );
		GL11.glLineWidth( 8.0F );
		GL11.glDisable( GL11.GL_TEXTURE_2D );
		GL11.glDepthMask( false );

		tessellator.startDrawing( GL11.GL_LINE_LOOP );
		tessellator.setTranslation( -x, -y, -z );
		
		int num_segments = 32;
		double theta = 2.0 * Math.PI / (float)num_segments;

		double tangetial_factor = Math.tan( theta );
		double radial_factor = Math.cos( theta );
		
		double rx = 8.0;
		double ry = 0.0;

		for( int i = 0; i < num_segments; i++ )
		{
			tessellator.addVertex( ( posX + 0.5 ) + rx, posY + 0.25, ( posZ + 0.5 ) + ry );
			
			double tx = -ry;
			double ty = rx;
			
			rx = ( rx + tx * tangetial_factor ) * radial_factor;
			ry = ( ry + ty * tangetial_factor ) * radial_factor;
		}
		tessellator.draw();

		GL11.glDepthMask( true );
		GL11.glEnable( GL11.GL_TEXTURE_2D );
		GL11.glDisable( GL11.GL_BLEND );

		tessellator.setTranslation( 0, 0, 0 );

	}

	public void readFromNBT( NBTTagCompound compound )
	{
		dimensionId = compound.getInteger( "Dimension" );
		posX = compound.getInteger( "x" );
		posY = compound.getInteger( "y" );
		posZ = compound.getInteger( "z" );
	}

	public void writeToNBT( NBTTagCompound compound )
	{
		compound.setInteger( "Dimension", dimensionId );
		compound.setInteger( "x", posX );
		compound.setInteger( "y", posY );
		compound.setInteger( "z", posZ );
	}
	
	@Override
	public String toString()
	{
		return "Waypoint[ " + posX + ", " + posY + ", " + posZ + " ]";
	}
}