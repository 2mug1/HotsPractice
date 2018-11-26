package net.hotsmc.practice.knockback;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.ladder.Ladder;
import net.hotsmc.practice.match.Match;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class KnockbackListener implements Listener {

    private Field fieldPlayerConnection;
    private Method sendPacket;
    private Constructor<?> packetVelocity;

    public KnockbackListener()
    {
        try
        {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            Class<?> entityPlayerClass = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
            Class<?> packetVelocityClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutEntityVelocity");
            Class<?> playerConnectionClass = Class.forName("net.minecraft.server." + version + ".PlayerConnection");

            this.fieldPlayerConnection = entityPlayerClass.getField("playerConnection");
            this.sendPacket = playerConnectionClass.getMethod("sendPacket", new Class[] { packetVelocityClass.getSuperclass() });
            this.packetVelocity = packetVelocityClass.getConstructor(new Class[] { Integer.TYPE, Double.TYPE, Double.TYPE, Double.TYPE });
        }
        catch (ClassNotFoundException|NoSuchFieldException|SecurityException|NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event)
    {
        Player player = event.getPlayer();
        EntityDamageEvent lastDamage = player.getLastDamageCause();
        if ((lastDamage == null) || (!(lastDamage instanceof EntityDamageByEntityEvent))) {
            return;
        }
        if ((((EntityDamageByEntityEvent)lastDamage).getDamager() instanceof Player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if ((!(event.getEntity() instanceof Player)) || (!(event.getDamager() instanceof Player))) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        Player damaged = (Player)event.getEntity();
        Player damager = (Player)event.getDamager();
        if (damaged.getNoDamageTicks() > damaged.getMaximumNoDamageTicks() / 2.0D) {
            return;
        }

        double horizontalMultiplier = 1.0D;
        double verticalMultiplier = 1.0D;

        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(damager);

        if(practicePlayer == null)return;

        if(practicePlayer.isInMatch()){
            final Match match = practicePlayer.getInMatch();
            final Knockback knockback = HotsPractice.getKnockbackManager().getKnockbackByLadderType(match.getLadderType());
            horizontalMultiplier = knockback.getHorizontalMultiplier();
            verticalMultiplier = knockback.getVerticalMultiplier();
        }

        if(practicePlayer.isInEvent()){
            final Event eventGame = practicePlayer.getInEventGame();
            final Knockback knockback = HotsPractice.getKnockbackManager().getKnockbackByLadderType(eventGame.getLadderType());
            horizontalMultiplier = knockback.getHorizontalMultiplier();
            verticalMultiplier = knockback.getVerticalMultiplier();
        }

        double sprintMultiplier = damager.isSprinting() ? 0.8D : 0.5D;
        double kbMultiplier = damager.getItemInHand() == null ? 0.0D : damager.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK) * 0.2D;

        double airMultiplier = damaged.isOnGround() ? 1.0D : 0.5D;

        Vector knockback = damaged.getLocation().toVector().subtract(damager.getLocation().toVector()).normalize();
        knockback.setX((knockback.getX() * sprintMultiplier + kbMultiplier) * horizontalMultiplier);
        knockback.setY(0.35D * airMultiplier * verticalMultiplier);
        knockback.setZ((knockback.getZ() * sprintMultiplier + kbMultiplier) * horizontalMultiplier);
        try
        {
            Object entityPlayer = damaged.getClass().getMethod("getHandle", new Class[0]).invoke(damaged, new Object[0]);
            Object playerConnection = this.fieldPlayerConnection.get(entityPlayer);
            Object packet = this.packetVelocity.newInstance(new Object[] { Integer.valueOf(damaged.getEntityId()), Double.valueOf(knockback.getX()), Double.valueOf(knockback.getY()), Double.valueOf(knockback.getZ()) });
            this.sendPacket.invoke(playerConnection, new Object[] { packet });
        }
        catch (SecurityException|IllegalArgumentException|IllegalAccessException|InvocationTargetException|NoSuchMethodException|InstantiationException e)
        {
            e.printStackTrace();
        }
    }
}
