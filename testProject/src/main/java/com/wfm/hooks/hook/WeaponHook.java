package com.wfm.hooks.hook;

import com.wfm.hooks.base.Weapon;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName WeaponHook
 * @Descripyion TODO
 * @date 2020/4/4 23:55
 */
public class WeaponHook extends Weapon {
    private OnUseWeaponAttackListener onUseWeaponAttackListener;

    @Override
    public void attack() {
        super.attack();
        if (onUseWeaponAttackListener != null){
            onUseWeaponAttackListener.onUseWeaponAttack(damage);
        }
    }

    public void setOnUseWeaponAttackListener(OnUseWeaponAttackListener onUseWeaponAttackListener) {
        this.onUseWeaponAttackListener = onUseWeaponAttackListener;
    }

    public static interface OnUseWeaponAttackListener{
        public int onUseWeaponAttack(int damage);
    }
}
