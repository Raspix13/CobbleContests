package com.raspix.fabric.cobble_contests.pokemon;

import net.minecraft.nbt.CompoundTag;

public class CVs {


    private int cool;
    private int beauty;
    private int cute;
    private int smart;
    private int tough;
    private int sheen;



    public CVs(){
    }

    public static CVs getFromTag(CompoundTag cvsTag) {
        CVs cvs = new CVs();
        if (cvsTag != null){
            cvs.setCool(cvsTag.getInt("cool"));
            cvs.setBeauty(cvsTag.getInt("beauty"));
            cvs.setCute(cvsTag.getInt("cute"));
            cvs.setSmart(cvsTag.getInt("smart"));
            cvs.setTough(cvsTag.getInt("tough"));
            cvs.setSheen(cvsTag.getInt("sheen"));
        }
        return cvs;
    }

    public void loadCVs(){

    }

    public static CVs createNewCVs(){
        CVs newCV = new CVs();
        newCV.setCool(0);
        newCV.setBeauty(0);
        newCV.setCute(0);
        newCV.setSmart(0);
        newCV.setTough(0);
        newCV.setSheen(0);
        return newCV;
    }

    public CompoundTag saveToNBT(){
        CompoundTag tag = new CompoundTag();
        tag.putInt("cool", cool);
        tag.putInt("beauty", beauty);
        tag.putInt("cute", cute);
        tag.putInt("smart", smart);
        tag.putInt("tough", tough);
        tag.putInt("sheen", sheen);
        return tag;
    }

    public String getAsString(){
        String out = "CVS:[";
        out += this.cool + ", " + this.beauty + ", " + this.cute + ", " + this.smart + ", " + this.tough + "]/" + this.sheen;
        return out;
    }

    public int getCool(){return cool;}

    private void setCool(int val){this.cool = Math.max(0, Math.min(val, 255));}

    public void increaseCool(int val){
        this.cool += val;
        this.cool = Math.max(0, Math.min(this.cool, 255));
    }

    public int getBeauty(){return beauty;}
    private void setBeauty(int val){this.beauty = Math.max(0, Math.min(val, 255));;}
    public void increaseBeauty(int val){
        this.beauty += val;
        this.beauty = Math.max(0, Math.min(this.beauty, 255));
    }

    public int getCute(){return cute;}
    private void setCute(int val){this.cute = Math.max(0, Math.min(val, 255));;}
    public void increaseCute(int val){
        this.cute += val;
        this.cute = Math.max(0, Math.min(this.cute, 255));
    }

    public int getSmart(){return smart;}
    private void setSmart(int val){this.smart = Math.max(0, Math.min(val, 255));;}
    public void increaseSmart(int val){
        this.smart += val;
        this.smart = Math.max(0, Math.min(this.smart, 255));
    }

    public int getTough(){return tough;}
    public void setTough(int val){this.tough = Math.max(0, Math.min(val, 255));;}
    public void increaseTough(int val){
        this.tough += val;
        this.tough = Math.max(0, Math.min(this.tough, 255));
    }

    public int getSheen(){return sheen;}
    public void setSheen(int val){this.sheen = Math.max(0, Math.min(val, 255));;}
    public void increaseSheen(int val){
        this.sheen += val;
        this.sheen = Math.max(0, Math.min(this.sheen, 255));
    }
    public int getSheenStars(){
        return sheen == 255? 12: (int)((12f*(float)sheen)/256f);
    }


    public void increaseCVFromFlavorIndex(int flavorIdx, int bonus){
        switch (flavorIdx){
            case 0:
                increaseCool(bonus);
                break;
            case 1:
                increaseBeauty(bonus);
                break;
            case 2:
                increaseCute(bonus);
                break;
            case 3:
                increaseSmart(bonus);
                break;
            case 4:
                increaseTough(bonus);
                break;
            default:
                System.out.println("Error Increasing CV from flavor index out of range");
                break;
        }
    }


}
