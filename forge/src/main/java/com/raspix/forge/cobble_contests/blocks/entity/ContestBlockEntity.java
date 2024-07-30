package com.raspix.forge.cobble_contests.blocks.entity;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.forge.cobble_contests.menus.ContestMenu;
import com.raspix.forge.cobble_contests.pokemon.Badges;
import com.raspix.forge.cobble_contests.pokemon.CVs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ContestBlockEntity extends BlockEntity implements MenuProvider {

    private static final Component TITLE = Component.translatable("container." + CobbleContests.MOD_ID + ".contest_block");

    private boolean isHost;
    private UUID hostId;
    private int contestType;
    private Map<UUID, ContestParticipation> participants;



    public ContestBlockEntity(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
    }

    public ContestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.CONTEST_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, @NotNull Inventory playerInv, Player player) {
        return new ContestMenu(containerID, playerInv, this);
    }

    public boolean tryHosting(UUID id){
        if(!isHost){
            this.hostId = id;
            this.isHost = true;
            return true;
        }else {
            return false;
        }
    }

    public boolean setContestType(UUID id, int type){
        if(this.isHost && this.hostId == id){
            contestType = type;
            return true;
        }else if(!this.isHost){
            tryHosting(id);
            contestType = type;
            return true;
        }else {
            return false;
        }
    }

    private void removeHost(){
        this.hostId = null;
        this.isHost = false;
    }

    public void startContest(int color, UUID player){

    }

    public void addContestant(UUID player, int pokemonIdx){

    }

    public String getContestResults(){
        return "cobble_contests.contest_type.cool";
    }

    public void clearContest(){
        participants.clear();
        hostId = null;
        isHost = false;
        contestType = 0;
    }

    public void kickContestent(UUID id){
        if(participants.containsKey(id)){
            participants.remove(id);
        }
    }


    public class ContestParticipation{
        private UUID id;
        private int pokemonIdx;

        public ContestParticipation(UUID id, int pokemonIdx){
            this.id = id;
            this.pokemonIdx = pokemonIdx;
        }
    }

    public String getCurrentContestInfo(){
        String result = "";
        if(isHost){
            result += "Host: " + Minecraft.getInstance().level.getPlayerByUUID(hostId).getDisplayName().getString();
            result += ", Type: " + contestType;
        }else {
            result += "No current host";
        }
        return result;
    }

    public void runStubContest(){
        assert Minecraft.getInstance().level != null;
        if (!Minecraft.getInstance().level.isClientSide){
            System.out.println("Running contest");
        }
    }

    public void runStatAssesment(UUID id, int pokeIdx, int contestType, int contestLevel1){
        String contestOutput = "";
        try {
            System.out.println("Hello there, you should see this in CBE");
            Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty(id).get(pokeIdx);
            CompoundTag badgeTag = poke.getPersistentData().getCompound("Badges");
            String pokeName = poke.getDisplayName().getString();
            int contestLevel = getNextContestLevel(badgeTag, contestType);
            if(contestLevel < 5) {
                boolean result = runContest(poke, contestType, contestLevel);
                if (result) {
                    contestOutput = pokeName + " Won the " + getContestLevelString(contestLevel) + " " + getContestTypeString(contestType) + " Contest";
                } else {
                    contestOutput = pokeName + " Lost the " + getContestLevelString(contestLevel) + " " + getContestTypeString(contestType) + " Contest";
                }
            }else{
                contestOutput = pokeName + " has already beaten all " + getContestTypeString(contestType) + " Contests";
            }
            System.out.println("do you see this");
            ServerPlayer sPlayer = poke.getOwnerPlayer();
            if (!sPlayer.level().isClientSide()) {
                //sPlayer.sendSystemMessage(Component.literal(contestOutput));
                System.out.println("Is server side");
                //sPlayer.displayClientMessage(Component.literal(contestOutput), true);
                sPlayer.displayClientMessage(Component.literal(contestOutput).withStyle(ChatFormatting.LIGHT_PURPLE), false);
                System.out.println("after message");
            }


            //Objects.requireNonNull().sendSystemMessage(Component.literal(contestOutput));
        }catch (NoPokemonStoreException e){
        }


    }

    public String getContestTypeString(int contestType){
        return switch (contestType) {
            case 0 -> "Cool";
            case 1 -> "Beauty";
            case 2 -> "Cute";
            case 3 -> "Smart";
            case 4 -> "Tough";
            default -> "ERROR";
        };
    }

    public String getContestLevelString(int contestLevel){
        return switch (contestLevel) {
            case 0 -> "Normal";
            case 1 -> "Super";
            case 2 -> "Hyper";
            case 3 -> "Ultra";
            case 4 -> "Master";
            default -> "ERROR";
        };
    }

    private int getNextContestLevel(CompoundTag badgeTag, int contestType) {
        Badges badges = Badges.getFromTag(badgeTag);
        return badges.getNextContestLevel(contestType);
    }

    private boolean runContest(Pokemon poke, int contestType, int contestLevel) {
        boolean result = false;
        CVs cvs = CVs.getFromTag(poke.getPersistentData().getCompound("CVs"));
        Badges badges = Badges.getFromTag(poke.getPersistentData().getCompound("Badges"));
        switch (contestType) {
            case 0:
                if(runAppContest(poke, contestLevel, cvs.getCool()))
                    badges.setRankedCool(contestLevel, true);
                break;
            case 1:
                if(runAppContest(poke, contestLevel, cvs.getBeauty()))
                    badges.setRankedBeauty(contestLevel, true);
                break;
            case 2:
                if(runAppContest(poke, contestLevel, cvs.getCute()))
                    badges.setRankedCute(contestLevel, true);
                break;
            case 3:
                if(runAppContest(poke, contestLevel, cvs.getSmart()))
                    badges.setRankedSmart(contestLevel, true);
                break;
            case 4:
                if(runAppContest(poke, contestLevel, cvs.getTough()))
                    badges.setRankedTough(contestLevel, true);
                break;
            default:
                break;
        }
        Map<String, CompoundTag> myData = new HashMap<String, CompoundTag>() {};
        myData.put("Badges", badges.saveToNBT());
        saveBadges(poke, myData);
        return result;
    }

    private void saveBadges(final Pokemon pokemon, final Map<String, CompoundTag> myData) {
        final CompoundTag tag = pokemon.getPersistentData();
        myData.forEach((key, value) -> {
            tag.put(key, value);
        });
        // basically a vanilla "markAsDirty"
        if (pokemon.getChangeObservable() instanceof SimpleObservable<Pokemon>) { //TODO
            ((SimpleObservable<Pokemon>) pokemon.getChangeObservable()).emit(pokemon);
        }else {
            System.out.println("error, not simple observable (ContestBlockEntity)");
        }
    }


    private int[] thresholds = {5, 40, 100, 175, 245};
    private boolean runAppContest(Pokemon poke, int level, int typeVal){
        boolean result = false;
        if (level < 5 &&
                typeVal >= thresholds[level]){
            result = true;
        }
        return result;
    }

    private boolean runBeautyContest(Pokemon poke, int level) {
        boolean result = false;
        CVs cvs = CVs.getFromTag(poke.getPersistentData().getCompound("CVs"));
        if (cvs.getBeauty() >= thresholds[level]){
            result = true;
            Badges badges = Badges.getFromTag(poke.getPersistentData().getCompound("Badges"));
            badges.setRankedBeauty(level, true);
            Map<String, CompoundTag> myData = new HashMap<String, CompoundTag>() {};
            //CompoundTag dat = cvs.saveToNBT();
            myData.put("Badges", badges.saveToNBT());
            saveBadges(poke, myData);

        }
        return result;
    }



}
