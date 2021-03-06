/**
    Copyright (C) 2014 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.magicbeans.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.entities.IEntityMagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.utilities.MagicBeansUtilities;

/**
 * @author jabelar
 *
 */
public class MessageSyncEntityToClient implements IMessage 
{
    private int entityId ;
    private NBTTagCompound entitySyncDataCompound;

    public MessageSyncEntityToClient() 
    { 
    	// need this constructor
    }

    public MessageSyncEntityToClient(int parEntityId, NBTTagCompound parTagCompound) 
    {
    	entityId = parEntityId;
        entitySyncDataCompound = parTagCompound;
        // DEBUG
        System.out.println("SyncEntityToClient constructor");
    }

    @Override
    public void fromBytes(ByteBuf buf) 
    {
    	entityId = ByteBufUtils.readVarInt(buf, 4);
    	entitySyncDataCompound = ByteBufUtils.readTag(buf); // this class is very useful in general for writing more complex objects
    	// DEBUG
    	System.out.println("fromBytes");
    }

    @Override
    public void toBytes(ByteBuf buf) 
    {
    	ByteBufUtils.writeVarInt(buf, entityId, 4);
    	ByteBufUtils.writeTag(buf, entitySyncDataCompound);
        // DEBUG
        System.out.println("toBytes encoded");
    }

    public static class Handler implements IMessageHandler<MessageSyncEntityToClient, IMessage> 
    {
        @Override
        public IMessage onMessage(MessageSyncEntityToClient message, MessageContext ctx) 
        {
        	EntityPlayer thePlayer = MagicBeans.proxy.getPlayerEntityFromContext(ctx);
        	IEntityMagicBeans theEntity = (IEntityMagicBeans)MagicBeansUtilities.getEntityByID(message.entityId, thePlayer.worldObj);
        	// DEBUG
        	if (theEntity == null)
        	{
        		System.out.println("The entity is null for entity ID ="+message.entityId);
        	}
        	else
        	{
        		System.out.println("MessageSyncEnitityToClient onMessage(), entity ID = "+message.entityId);
        	}
        	theEntity.setSyncDataCompound(message.entitySyncDataCompound);
            return null; // no response in this case
        }
    }
}
