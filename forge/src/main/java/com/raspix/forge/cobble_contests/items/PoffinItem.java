package com.raspix.forge.cobble_contests.items;

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
import com.cobblemon.mod.common.util.DataKeys;
import com.raspix.forge.cobble_contests.pokemon.CVs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
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

    public PoffinItem(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BagItem getBagItem() {
        return new BagItem() {
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
        System.out.println("Used item");
        CVs cvs;
        CompoundTag tag = pokemon.getPersistentData();
        if(tag.getCompound(cvsKey) == null){
            cvs = CVs.createNewCVs();
            //System.out.println("missing CVs, generated");
        }else {
            cvs = CVs.getFromTag(tag.getCompound(cvsKey));
            System.out.println(cvs.getAsString());
        }

        if(cvs.getSheen() < 255) {
            // TODO: spicy, dry, sweet, bitter, sour, sheen placeholder for poffin flavors until get CVs/poffin vals working
            int[] flavors = {0, 0, 0, 0, 0, 0};
            if (itemStack.getTag() != null) {
                for (String tagInfo : itemStack.getTag().getAllKeys()) {
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
            }
            System.out.println("Applying: " + Arrays.toString(flavors));

            Nature nature = pokemon.getNature();
            int disliked = getIndexFromFlavor(nature.getDislikedFlavor());
            int liked = getIndexFromFlavor(nature.getFavoriteFlavor());

            // need to change this so that all get buff if the primary flavor of the poffin is fav/hated
            for (int i = 0; i < 5; i++) {
                int valBonus = flavors[i];
                if (i == liked) {
                    valBonus = (int) ((float) valBonus * 1.1f);
                } else if (i == disliked) {
                    valBonus = (int) ((float) valBonus * 0.9f);
                }
                cvs.increaseCVFromFlavorIndex(i, valBonus);
            }
            cvs.increaseSheen(flavors[5]);

            Map<String, CompoundTag> myData = new HashMap<String, CompoundTag>() {};
            //CompoundTag dat = cvs.saveToNBT();
            myData.put(cvsKey, cvs.saveToNBT());
            saveCVs(pokemon, myData);
            pokemon.incrementFriendship(1, true);

            if (!serverPlayer.isCreative()) {
                itemStack.shrink(1);
            }
            //CVs testCVs = CVs.getFromTag(dat);
            return InteractionResultHolder.success(itemStack);
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
        System.out.println("use");

        //PokemonSelectingItem.super.use(serverPlayer, itemStack);
        InteractionResultHolder<ItemStack> result = interactGeneral(serverPlayer, itemStack);//use(serverPlayer, itemStack);
        return result;
        /**if (result.result != ActionResult.PASS) {
            return result;
        }
        return InteractionResultHolder.success(itemStack);*/
        //return InteractionResultHolder.success(itemStack);
        //return TypedActionResult.success(user.getStackInHand(hand))
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide()){
            return super.useOn(context);
        }
        /**if(context.getPlayer() instanceof ServerPlayer){
            System.out.println("is server player");
        }*/
        ServerPlayer player = (ServerPlayer) context.getPlayer();
        ItemStack itemStack = context.getItemInHand();



        return interactGeneral(player, itemStack).getResult();//super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level arg, Player player, InteractionHand hand) {
        if(player instanceof ServerPlayer){
            System.out.println("is server player");
            this.use((ServerPlayer) player, player.getItemInHand(hand));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> interactGeneral(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack) {
        System.out.println("interactGeneral");
        try {
            List<Pokemon> pokeList1 = Cobblemon.INSTANCE.getStorage().getParty(serverPlayer.getUUID()).toGappyList();
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
                                            pk.getSpecies().resourceIdentifier, ItemInit.POFFIN_DOUGH_BASE.getId()));// Registries.ITEM.getId(itemStack.getItem())  itemStack.getItem().
                        }
                        //itemStack.getItem().
                        //Registries.ITEM..getId(stack.item
                        return null;
                    }
            );
        } catch (NoPokemonStoreException e) {
            throw new RuntimeException(e);
        }

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            //pTooltipComponents.add(Component.translatable("tooltip.snekcraft.snake_bag.tooltip.has_snakes", (snakeNum + "")).withStyle(ChatFormatting.GRAY));

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
            pTooltipComponents.add(Component.translatable("tooltip.cobble_contests.poffin_item.tooltip.poffin_stats", 0, 0, 0, 0, 0).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.LIGHT_PURPLE));
        }

    }
}
