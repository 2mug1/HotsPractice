package net.hotsmc.practice.arena;

import lombok.Getter;

@Getter
public class IgnoreWorldLocation {

    private double x, y, z;
    private float yaw, pitch;

    /**
     * @param x
     * @param y
     * @param z
     * @param yaw
     * @param pitch
     */
    public IgnoreWorldLocation(double x, double y, double z, double yaw, double pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = (float) yaw;
        this.pitch = (float) pitch;
    }
}
