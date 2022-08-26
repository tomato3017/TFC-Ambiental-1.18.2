package com.lumintorious.tfcambiental;

import net.minecraft.world.damagesource.DamageSource;

public abstract class AmbientalDamage {
    public static final DamageSource HEAT = new DamageSource("hyperthermia").bypassArmor();
    public static final DamageSource COLD = new DamageSource("hypothermia").bypassArmor();
}
