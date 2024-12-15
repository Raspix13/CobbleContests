package com.raspix.fabric.cobble_contests.items;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.advancement.CobblemonCriteria;
import com.cobblemon.mod.common.advancement.criterion.PokemonInteractContext;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.berry.Flavor;
import com.cobblemon.mod.common.api.callback.PartySelectCallbacks;
import com.cobblemon.mod.common.api.item.PokemonSelectingItem;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.item.CobblemonItem;
import com.cobblemon.mod.common.item.battle.BagItem;
import com.cobblemon.mod.common.pokemon.Nature;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.fabric.cobble_contests.pokemon.CVs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PoffinItem extends CobblemonItem implements PokemonSelectingItem {


    public static final String cvsKey = "CVs";
    private final String coolKey = "cool";
    private int mainFlavor = -1;
    private int secFlavor = -1;

    public PoffinItem(Properties properties) {
        super(properties);
        this.mainFlavor = -1;
        this.secFlavor = -1;
    }

    public PoffinItem(Properties properties, int mainFlavor, int secFlavor) {
        super(properties);
        this.mainFlavor = mainFlavor;
        this.secFlavor = secFlavor;
    }

    private void setBasicFlavors(){
    }

    @Nullable
    @Override
    public BagItem getBagItem() {
        return new BagItem() {
            @Override
            public @NotNull Item getReturnItem() {
                return ItemInit.DRY_POFFIN;
            }

            @NotNull
            @Override
            public String getItemName() {
                return "item.cobblemon.poffin";
            }

            @Override
            public boolean canUse(@NotNull PokemonBattle pokemonBattle, @NotNull BattlePokemon battlePokemon) {
                return true;
            }

            @NotNull
            @Override
            public String getShowdownInput(@NotNull BattleActor battleActor, @NotNull BattlePokemon battlePokemon, @Nullable String s) {
                return null;
            }

            @Override
            public boolean canStillUse(@NotNull ServerPlayer serverPlayer, @NotNull PokemonBattle pokemonBattle, @NotNull BattleActor battleActor, @NotNull BattlePokemon battlePokemon, @NotNull ItemStack itemStack) {
                return  itemStack.getCount() > 0 && canUse(pokemonBattle, battlePokemon) && battleActor.canFitForcedAction();
            }
        };
    }

    @Override
    public void applyToBattlePokemon(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack, @NotNull BattlePokemon battlePokemon) {

    }

    @Nullable
    @Override
    public InteractionResultHolder<ItemStack> applyToPokemon(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack, @NotNull Pokemon pokemon) {
        CVs cvs;
        CompoundTag tag = pokemon.getPersistentData();
        if(tag.getCompound(cvsKey) == null){
            cvs = CVs.createNewCVs();
        }else {
            cvs = CVs.getFromTag(tag.getCompound(cvsKey));
            System.out.println(cvs.getAsString());
        }

        if(cvs.getSheen() < 255 || itemStack.getItem() == ItemInit.FOUL_POFFIN) {
            // spicy, dry, sweet, bitter, sour, sheen
            int[] flavors = {0, 0, 0, 0, 0, 0};
            //String uwu = itemStack.getTags().toList();
            //TODO
            /**if (itemStack.getTags() != null) {
                for (String tagInfo : itemStack.getTags().getAllKeys()) {
                    if (tagInfo.contains("Flavors")) {
                        CompoundTag poffinTag = itemStack.getTag().getCompound(tagInfo);
                        try {
                            flavors[0] = poffinTag.getInt("spicy");
                            flavors[1] = poffinTag.getInt("dry");
                            flavors[2] = poffinTag.getInt("sweet");
                            flavors[3] = poffinTag.getInt("bitter");
                            flavors[4] = poffinTag.getInt("sour");
                            flavors[5] = poffinTag.getInt("sheen");
                        } catch (ClassCastException e) {

                        }
                    }
                }
            }else {
                flavors = getBaseFlavors();
            }*/
            System.out.println(itemStack.getTags().toList().size());
            for(TagKey key : itemStack.getTags().toList()){
                System.out.println(key);
            }
            System.out.println("Applying: " + Arrays.toString(flavors));

            Nature nature = pokemon.getNature();
            int disliked = getIndexFromFlavor(nature.getDislikedFlavor());
            int liked = getIndexFromFlavor(nature.getFavoriteFlavor());
            float valMultiplier = 1.0f;
            if(this.mainFlavor == liked && mainFlavor >= 0 && mainFlavor != secFlavor){ //liked and has 2 flavors
                System.out.println("liked flavor");
                valMultiplier = 1.1f;
                pokemon.incrementFriendship(5, true);
            }else if(this.mainFlavor == disliked && mainFlavor >= 0 && mainFlavor != secFlavor){ //disliked and has 2 flavors
                System.out.println("disliked flavor");
                valMultiplier = 0.9f;
                pokemon.decrementFriendship(1, true);
            }else if(this.mainFlavor == -1 && this.secFlavor == -1){ //no flavor so foul
                pokemon.decrementFriendship(20, true);
            }else {
                pokemon.incrementFriendship(1, true); //no opinion on flavor
            }

            // need to change this so that all get buff if the primary flavor of the poffin is fav/hated
            for (int i = 0; i < 5; i++) {
                int valBonus = (int) ((float)flavors[i] * valMultiplier);
                /**int valBonus = flavors[i];
                if (i == liked) {
                    valBonus = (int) ((float) valBonus * 1.1f);
                } else if (i == disliked) {
                    valBonus = (int) ((float) valBonus * 0.9f);
                }*/

                cvs.increaseCVFromFlavorIndex(i, valBonus);
            }
            cvs.increaseSheen(flavors[5]);

            Map<String, CompoundTag> myData = new HashMap<String, CompoundTag>() {};
            //CompoundTag dat = cvs.saveToNBT();
            myData.put(cvsKey, cvs.saveToNBT());
            saveCVs(pokemon, myData);

            if (!serverPlayer.isCreative()) {
                itemStack.shrink(1);
            }
            //CVs testCVs = CVs.getFromTag(dat);
            return InteractionResultHolder.success(itemStack);
        }else {
            serverPlayer.displayClientMessage(Component.literal(pokemon.getDisplayName().getString() + " already has max sheen and can not eat any more").withStyle(ChatFormatting.LIGHT_PURPLE), false);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    private void saveMyData(final Pokemon pokemon, final Map<String, Integer> myData) {

        final CompoundTag tag = pokemon.getPersistentData();
        myData.forEach((key, value) -> {
            tag.put(key, IntTag.valueOf(value));
        });
        // basically a vanilla "markAsDirty"
        if (pokemon.getChangeObservable() instanceof SimpleObservable<Pokemon>) { //TODO
            ((SimpleObservable<Pokemon>) pokemon.getChangeObservable()).emit(pokemon);
        }else {
            System.out.println("error, not simple observable (PoffinItem)");
        }
    }

    private void saveCVs(final Pokemon pokemon, final Map<String, CompoundTag> myData) {
        final CompoundTag tag = pokemon.getPersistentData();
        myData.forEach((key, value) -> {
            tag.put(key, value);
        });
        // basically a vanilla "markAsDirty"
        if (pokemon.getChangeObservable() instanceof SimpleObservable<Pokemon>) { //TODO
            ((SimpleObservable<Pokemon>) pokemon.getChangeObservable()).emit(pokemon);
        }else {
            System.out.println("error, not simple observable (PoffinItem)");
        }
    }

    private void getMyData(Pokemon pokemon, String key){

    }

    private int getIndexFromFlavor(Flavor flavor){
        int idx = -1;
        if(flavor != null){
            switch (flavor) {
                case SPICY:
                    idx = 0;
                    break;
                case DRY:
                    idx = 1;
                    break;
                case SWEET:
                    idx = 2;
                    break;
                case BITTER:
                    idx = 3;
                    break;
                case SOUR:
                    idx = 4;
                    break;
                default:
                    System.out.println("Unknown flavor.");
                    break;
            }
        }
        return idx;
    }


    @Override
    public boolean canUseOnBattlePokemon(@NotNull BattlePokemon battlePokemon) {
        return false;
    }

    @Override
    public boolean canUseOnPokemon(@NotNull Pokemon pokemon) {
        return true;
    }


    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> interactGeneralBattle(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack, @NotNull BattleActor battleActor) {
        return null;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> interactWithSpecificBattle(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack, @NotNull BattlePokemon battlePokemon) {
        return null;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack) {
        InteractionResultHolder<ItemStack> result = interactGeneral(serverPlayer, itemStack);//use(serverPlayer, itemStack);
        return result;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide()){
            return super.useOn(context);
        }
        ServerPlayer player = (ServerPlayer) context.getPlayer();
        ItemStack itemStack = context.getItemInHand();



        return interactGeneral(player, itemStack).getResult();//super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level arg, Player player, InteractionHand hand) {
        if(player instanceof ServerPlayer){
            this.use((ServerPlayer) player, player.getItemInHand(hand));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> interactGeneral(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack) {
        List<Pokemon> pokeList1 = Cobblemon.INSTANCE.getStorage().getParty(serverPlayer).toGappyList();
        if(pokeList1.isEmpty()){
            return InteractionResultHolder.fail(itemStack);
        }
        List<Pokemon> pokeList = new ArrayList<Pokemon>();
        for (Pokemon poke: pokeList1) {
            if (poke != null){
                pokeList.add(poke);
            }
        }
        PartySelectCallbacks.INSTANCE.createFromPokemon(
                serverPlayer,
                pokeList,
                this::canUseOnPokemon,
                pk -> {
                    if (true) {
                        applyToPokemon(serverPlayer, itemStack, pk);
                        CobblemonCriteria.INSTANCE.getPOKEMON_INTERACT().trigger(serverPlayer,
                                new PokemonInteractContext(
                                        pk.getSpecies().resourceIdentifier, Registries.ITEM.registry()));// Registries.ITEM.getId(itemStack.getItem())  itemStack.getItem().
                    }
                    return null;
                }
        );
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        //if(itemStack.getTags() != null) {
            /**for (TagKey tagInfo : itemStack.getTags().toList()) {
                if (tagInfo.toString().contains("Flavors")) {
                    CompoundTag poffinTag = itemStack.getTags().getCompound(tagInfo);
                    try {
                        int spicy = poffinTag.getInt("spicy");
                        int dry = poffinTag.getInt("dry");
                        int sweet = poffinTag.getInt("sweet");
                        int bitter = poffinTag.getInt("bitter");
                        int sour = poffinTag.getInt("sour");
                        int smoothness = poffinTag.getInt("sheen");
                        list.add(Component.translatable("tooltip.cobble_contests.poffin_item.tooltip.poffin_stats", spicy, dry, sweet, bitter, sour, smoothness).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.LIGHT_PURPLE));
                    } catch (ClassCastException e) {
                    }
                }
            }*/
        //}else {
            int[] baseFlavors = getBaseFlavors();
            list.add(Component.translatable("tooltip.cobble_contests.poffin_item.tooltip.poffin_stats", baseFlavors[0], baseFlavors[1], baseFlavors[2], baseFlavors[3], baseFlavors[4], baseFlavors[5]).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.LIGHT_PURPLE));
        //}
    }


    /**public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if(pStack.getTag() != null) {
            for (String tagInfo : pStack.getTag().getAllKeys()) {
                if (tagInfo.contains("Flavors")) {
                    CompoundTag poffinTag = pStack.getTag().getCompound(tagInfo);
                    try {
                        int spicy = poffinTag.getInt("spicy");
                        int dry = poffinTag.getInt("dry");
                        int sweet = poffinTag.getInt("sweet");
                        int bitter = poffinTag.getInt("bitter");
                        int sour = poffinTag.getInt("sour");
                        int smoothness = poffinTag.getInt("sheen");
                        pTooltipComponents.add(Component.translatable("tooltip.cobble_contests.poffin_item.tooltip.poffin_stats", spicy, dry, sweet, bitter, sour, smoothness).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.LIGHT_PURPLE));
                    } catch (ClassCastException e) {
                    }
                }
            }
        }else {
            int[] baseFlavors = getBaseFlavors();
            pTooltipComponents.add(Component.translatable("tooltip.cobble_contests.poffin_item.tooltip.poffin_stats", baseFlavors[0], baseFlavors[1], baseFlavors[2], baseFlavors[3], baseFlavors[4], baseFlavors[5]).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.LIGHT_PURPLE));
        }

    }*/

    /**
     * For poffins that were not assigned data, like those in creative
     * @return
     */
    private int[] getBaseFlavors(){
        int[] baseFlavors = {0, 0, 0, 0, 0, 20};
        if(mainFlavor >= 0 && mainFlavor <6 && secFlavor == -1){ // only one flavor
            baseFlavors[mainFlavor] = 15;
        }else if(mainFlavor >= 0 && mainFlavor <6){ //main flavor with sec
            baseFlavors[mainFlavor] = 10;
        }
        if(secFlavor >= 0 && secFlavor <6){ //sec flavor
            baseFlavors[secFlavor] = 5;
        }
        if(mainFlavor == -1 && secFlavor == -1){ //foul
            baseFlavors = new int[]{-10, -10, -10, -10, -10, -30};
        }
        return baseFlavors;
    }
}
