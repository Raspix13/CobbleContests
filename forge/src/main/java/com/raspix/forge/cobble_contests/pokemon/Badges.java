package com.raspix.forge.cobble_contests.pokemon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class Badges {

    private boolean[] rankedCool;
    private boolean[] rankedBeauty;
    private boolean[] rankedCute;
    private boolean[] rankedSmart;
    private boolean[] rankedTough;

    public Badges(){
        rankedCool = new boolean[5];
        rankedBeauty = new boolean[5];
        rankedCute = new boolean[5];
        rankedSmart = new boolean[5];
        rankedTough = new boolean[5];
    }

    public CompoundTag saveToNBT(){
        CompoundTag tag = new CompoundTag();
        tag.put("cool", getRankedCool());
        tag.put("beauty", getRankedBeauty());
        tag.put("cute", getRankedCute());
        tag.put("smart", getRankedSmart());
        tag.put("tough", getRankedTough());
        return tag;
    }



    public static Badges getFromTag(CompoundTag tag){
        Badges badges = new Badges();
        if (tag != null){
            badges.setRankedCool(tag.getCompound("cool"));
            badges.setRankedBeauty(tag.getCompound("beauty"));
            badges.setRankedCute(tag.getCompound("cute"));
            badges.setRankedSmart(tag.getCompound("smart"));
            badges.setRankedTough(tag.getCompound("tough"));
        }
        return badges;
    }


    private Tag getRankedCool() {
        CompoundTag coolTag = new CompoundTag();
        for(int i = 0; i < 5; i++){
            coolTag.putBoolean("cool" + i, rankedCool[i]);
        }
        return coolTag;
    }
    private void setRankedCool(CompoundTag cool) {
        for(int i = 0; i < 5; i++){
            rankedCool[i] = cool.getBoolean("cool" + i);
        }
    }
    public void setRankedCool(int level, boolean val){
        this.rankedCool[level] = val;
    }
    public boolean getCoolRanked(int i){
        return this.rankedCool[i];
    }


    private Tag getRankedBeauty() {
        CompoundTag beautyTag = new CompoundTag();
        for(int i = 0; i < 5; i++){
            beautyTag.putBoolean("beauty" + i, rankedBeauty[i]);
        }
        return beautyTag;
    }
    private void setRankedBeauty(CompoundTag beauty) {
        for(int i = 0; i < 5; i++){
            rankedBeauty[i] = beauty.getBoolean("beauty" + i);
        }
    }
    public void setRankedBeauty(int level, boolean val){
        this.rankedBeauty[level] = val;
    }
    public boolean getBeautyRanked(int i) {
        return this.rankedBeauty[i];
    }

    private Tag getRankedCute() {
        CompoundTag cuteTag = new CompoundTag();
        for(int i = 0; i < 5; i++){
            cuteTag.putBoolean("cute" + i, rankedCute[i]);
        }
        return cuteTag;
    }
    private void setRankedCute(CompoundTag cute) {
        for(int i = 0; i < 5; i++){
            rankedCute[i] = cute.getBoolean("cute" + i);
        }
    }
    public void setRankedCute(int level, boolean val){
        this.rankedCute[level] = val;
    }
    public boolean getCuteRanked(int i) {
        return this.rankedCute[i];
    }

    private Tag getRankedSmart() {
        CompoundTag smartTag = new CompoundTag();
        for(int i = 0; i < 5; i++){
            smartTag.putBoolean("smart" + i, rankedSmart[i]);
        }
        return smartTag;
    }
    private void setRankedSmart(CompoundTag smart) {
        for(int i = 0; i < 5; i++){
            rankedSmart[i] = smart.getBoolean("smart" + i);
        }
    }
    public void setRankedSmart(int level, boolean val){
        this.rankedSmart[level] = val;
    }
    public boolean getSmartRanked(int i) {
        return this.rankedSmart[i];
    }

    private Tag getRankedTough() {
        CompoundTag toughTag = new CompoundTag();
        for(int i = 0; i < 5; i++){
            toughTag.putBoolean("tough" + i, rankedTough[i]);
        }
        return toughTag;
    }
    private void setRankedTough(CompoundTag tough) {
        for(int i = 0; i < 5; i++){
            rankedTough[i] = tough.getBoolean("tough" + i);
        }
    }
    public void setRankedTough(int level, boolean val){
        this.rankedTough[level] = val;
    }
    public boolean getToughRanked(int i) {
        return this.rankedTough[i];
    }

    public int getNextContestLevel(int contestType){
        int result = 0;
        switch (contestType){
            case 0 -> result = highestContest(rankedCool);
            case 1 -> result = highestContest(rankedBeauty);
            case 2 -> result = highestContest(rankedCute);
            case 3 -> result = highestContest(rankedSmart);
            case 4 -> result = highestContest(rankedTough);
            default -> {
            }
        }
        return result;


    }

    private int highestContest(boolean[] selected){
        for (int i = 0; i < selected.length; i++){
            if(!selected[i]){
                return i;
            }
        }
        return selected.length;
    }

}
